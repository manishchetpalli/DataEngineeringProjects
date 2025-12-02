from fastapi import FastAPI, HTTPException, Request
from kafka import KafkaProducer
import logging
from logging.handlers import RotatingFileHandler
import json
from prometheus_fastapi_instrumentator import Instrumentator
from pydantic import BaseModel

# Initialize FastAPI app
app = FastAPI()

# Kafka configuration
bootstrap_servers = ["kafka:6667"]
username = "kafkaapi"
password = "kafkapi#123"

producer = KafkaProducer(
    bootstrap_servers=bootstrap_servers,
    acks='all',
    retries=5,
    batch_size=16384,
    linger_ms=10,
    api_version=(0, 10, 1),
    # security_protocol="SASL_SSL",
    # sasl_mechanism="SCRAM-SHA-512",
    # sasl_plain_username=username,
    # sasl_plain_password=password,
    # ssl_cafile="/app/truststore.pem",
    value_serializer=lambda v: v.encode("utf-8"),
    # ssl_check_hostname=False
)

# Logging configuration
log_handler = RotatingFileHandler(
    "/var/log/fastapi_kafka_api.log",  # Log file location
    maxBytes=10 * 1024 * 1024 * 1024,  # Log size 10 GB
    backupCount=5                      # Keep 5 backups
)
log_handler.setLevel(logging.INFO)
formatter = logging.Formatter(
    "%(asctime)s - %(levelname)s - %(message)s"
)
log_handler.setFormatter(formatter)
logger = logging.getLogger(__name__)
logger.addHandler(log_handler)
logger.setLevel(logging.INFO)

# Metrics setup
Instrumentator().instrument(app).expose(app)

for route in app.routes:
    print(f"Route: {route.path}, Method(s): {route.methods}")

# Request model for dynamic JSON validation (empty model for flexible data)
class KafkaMessage(BaseModel):
    # This is an empty model, as FastAPI automatically validates the JSON data
    class Config:
        arbitrary_types_allowed = True

@app.get("/health")
def health_check():
    """Health check endpoint."""
    return {"status": "healthy"}

@app.post("/kafka/publish/{topic_name}")
async def produce_to_kafka(topic_name: str, request: dict, req: Request):
    client_ip = req.client.host
    logger.info(f"API hit from IP: {client_ip}, Endpoint: /kafka/publish/{topic_name}")

    try:
        # Validate input: Ensure request body is not empty
        if not request:  # Checking if the request body is empty
            logger.warning(f"Empty request body from IP: {client_ip}")
            raise HTTPException(status_code=400, detail="No JSON data found in request")

        # Calculate message size
        message_size = len(json.dumps(request).encode("utf-8"))
        if message_size > 10 * 1024 * 1024:  # 10 MB limit
            logger.warning(f"Message size {message_size} bytes exceeded from IP: {client_ip}")
            raise HTTPException(status_code=413, detail="Message size exceeded 10 MB")

        # Produce message to Kafka
        producer.send(topic_name, json.dumps(request))
        producer.flush()

        logger.info(f"Data successfully sent to Kafka topic '{topic_name}' from IP: {client_ip}")
        return {"status": "success", "message": f"Data sent to Kafka topic '{topic_name}'"}
    except Exception as e:
        logger.error(f"Server error from IP: {client_ip}: {e}")
        raise HTTPException(status_code=500, detail=str(e))
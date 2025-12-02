# Flight Booking Data Pipeline with Airflow & CI/CD

## 📦 Overview
This project implements a production-ready **Flight Booking Data Pipeline** using a modern data engineering stack. The pipeline ingests raw flight booking data, processes it using PySpark on **Dataproc Serverless**, orchestrates workflows using **Airflow**, stores the results in **BigQuery**, and automates deployments with **GitHub Actions**.

The architecture follows industry-standard best practices — modular, scalable, and fully CI/CD-enabled.

---

## 🚀 Tech Stack

- **GitHub** – Version control & repository hosting  
- **GitHub Actions** – CI/CD automation for Dev & Prod  
- **Google Cloud Storage (GCS)** – Raw and processed data storage  
- **PySpark** – Distributed data processing  
- **Dataproc Serverless** – Serverless PySpark job execution  
- **Apache Airflow** – Workflow orchestration  
- **BigQuery** – Data warehouse for analytics  

---

## 🛠️ Project Objectives

- Process flight booking data using **PySpark** to generate meaningful business insights  
- Orchestrate PySpark job execution via **Airflow DAGs**  
- Load transformed data into **BigQuery**  
- Automate deployment using **GitHub Actions** for both Dev and Prod environments  

---

## 📁 Project Structure


    ├── airflow_job/
    │ └── airflow_job.py
    ├── spark_job/
    │ └── spark_transformation_job.py
    ├── variables/
    │ ├── dev/variables.json
    │ └── prod/variables.json
    ├── .github/
    │ ├── ci-cd.yml
    ├── flight_booking.csv
    └── readme.md


---

## 🔄 Pipeline Flow

### 1. Raw Data Ingestion
Raw flight booking data is stored in **GCS** under the `raw/flight_data/` path.

### 2. PySpark Processing (Dataproc Serverless)
PySpark job performs:
- Data cleaning  
- Transformations  
- Route-level insights  
- Booking & revenue trend calculations  

### 3. Orchestrated by Airflow
Airflow DAG:
- Triggers Dataproc Serverless job  
- Manages retries, scheduling, logging  
- Notifies on success or failure  

### 4. Load into BigQuery
The transformed output is loaded into BigQuery tables such as:
- `flight_bookings_cleaned`
- `booking_summary`
- `route_demand_insights`
- `revenue_trends`

### 5. CI/CD with GitHub Actions
- **CI:** Runs linting, unit tests, PySpark job validation on pull requests  
- **CD:** Automatically deploys updated DAGs & PySpark code to Dev/Prod based on branch merges  

---

## ▶️ Running the Pipeline

### **1. Upload Raw Data**
Upload to:


### **2. Trigger DAG**
Airflow DAG name:


### **3. Monitor Jobs**
Track execution through:
- Airflow UI  
- Dataproc Job Logs  
- BigQuery table updates  

---

## 📈 Results
The pipeline outputs business-ready datasets used for analytics, dashboards, and reporting.  
These include insights on:
- Popular routes  
- Seasonal demand  
- Revenue performance  
- Booking patterns  

---

## 📊 Future Enhancements

- Add data quality checks (Great Expectations)  
- Implement schema validation  
- Add SLA monitoring  
- Add dashboard auto-refresh workflows  

---

## 📚 License
This project is for educational and industrial demonstration purposes.


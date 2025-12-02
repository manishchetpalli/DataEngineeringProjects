# Flink Ignite JDBC Dialect

A custom Apache Flink JDBC dialect implementation for Apache Ignite, enabling seamless integration between Flink and Ignite via the JDBC connector. This dialect allows Flink to read from and write to Apache Ignite tables using SQL and the Table API.

## Features
- Custom `JdbcDialect` for Apache Ignite
- Supports upsert (MERGE) statements for idempotent writes
- Compatible with Flink Table & SQL API
- Auto-discovery via Java SPI (no manual registration required)
- Supports a wide range of SQL types

## Requirements
- Java 8+
- Apache Maven
- Apache Flink 1.16.1
- Apache Ignite 2.15.0

## Build Instructions
1. Clone this repository:
   ```sh
   git clone <repo-url>
   cd flink-ignite-custom-diaelect
   ```
2. Build the JAR using Maven:
   ```sh
   mvn clean package
   ```
   The resulting JAR will be in `target/flink-ignite-dialect-1.0.0.jar`.

## Usage
1. **Add the JAR to Flink:**
   - Copy `flink-ignite-dialect-1.0.0.jar` to the `lib/` directory of your Flink distribution or add it to your job’s classpath.
2. **Ensure Ignite JDBC Driver is available:**
   - The dialect uses `org.apache.ignite.IgniteJdbcThinDriver`. Make sure the Ignite JDBC driver JAR is also present in the Flink `lib/` directory.
3. **Define a Flink Table with Ignite:**
   Example Flink SQL DDL:
   ```sql
   CREATE TABLE ignite_table (
     id INT PRIMARY KEY NOT ENFORCED,
     name STRING,
     value DOUBLE
   ) WITH (
     'connector' = 'jdbc',
     'url' = 'jdbc:ignite:thin://<ignite-host>:10800',
     'table-name' = 'my_table',
     'driver' = 'org.apache.ignite.IgniteJdbcThinDriver'
   );
   ```
   - Replace `<ignite-host>` and `my_table` with your Ignite node and table name.

4. **Use in Flink SQL or Table API:**
   - You can now read from and write to Ignite tables using Flink SQL or the Table API.

## Configuration Options
Common connector options (see [Flink JDBC connector docs](https://nightlies.apache.org/flink/flink-docs-release-1.16/docs/connectors/table/jdbc/)):
- `'connector' = 'jdbc'`
- `'url' = 'jdbc:ignite:thin://<ignite-host>:10800'`
- `'table-name' = '<ignite-table>'`
- `'driver' = 'org.apache.ignite.IgniteJdbcThinDriver'`
- `'username'` / `'password'` (if authentication is enabled)
- `'sink.buffer-flush.max-rows'`, `'sink.buffer-flush.interval'`, etc.

## How It Works
- The dialect is auto-registered via the Java Service Provider Interface (SPI) using the file:
  - `src/main/resources/META-INF/services/org.apache.flink.connector.jdbc.dialect.JdbcDialectFactory`
- Flink will automatically use this dialect for any JDBC URL starting with `jdbc:ignite:thin:`.

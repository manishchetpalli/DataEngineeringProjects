package de.reply.flink.ignite.dialect;

import org.apache.flink.connector.jdbc.dialect.JdbcDialect;
import org.apache.flink.connector.jdbc.dialect.JdbcDialectFactory;

public class IgniteDialectFactory implements JdbcDialectFactory {
    @Override
    public boolean acceptsURL(String url) {
        return url.startsWith("jdbc:ignite:thin:");
    }

    @Override
    public JdbcDialect create() {
        return new IgniteDialect();
    }
}


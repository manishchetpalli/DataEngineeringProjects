package de.reply.flink.ignite.dialect;

import org.apache.flink.connector.jdbc.converter.AbstractJdbcRowConverter;
import org.apache.flink.table.types.logical.RowType;

import java.io.Serial;

public class IgniteRowConverter extends AbstractJdbcRowConverter {

    @Serial
    private static final long serialVersionUID = 1L;

    public IgniteRowConverter(RowType rowType) {
        super(rowType);
    }

    @Override
    public String converterName() {
        return "Ignite";
    }
}

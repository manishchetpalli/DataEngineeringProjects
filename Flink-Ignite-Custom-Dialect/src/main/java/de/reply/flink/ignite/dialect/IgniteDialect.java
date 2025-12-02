package de.reply.flink.ignite.dialect;

import org.apache.flink.connector.jdbc.converter.JdbcRowConverter;
import org.apache.flink.connector.jdbc.dialect.AbstractDialect;
import org.apache.flink.table.types.logical.LogicalTypeRoot;
import org.apache.flink.table.types.logical.RowType;

import java.io.Serial;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class IgniteDialect extends AbstractDialect {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final int MAX_TIMESTAMP_PRECISION = 9;
    private static final int MIN_TIMESTAMP_PRECISION = 1;
    private static final int MAX_DECIMAL_PRECISION = 1000;
    private static final int MIN_DECIMAL_PRECISION = 1;

    @Override
    public String dialectName() {
        return "Ignite";
    }

    @Override
    public JdbcRowConverter getRowConverter(RowType rowType) {
        return new IgniteRowConverter(rowType);
    }

    @Override
    public String getLimitClause(long limit) {
        return "LIMIT " + limit;
    }

    @Override
    public String quoteIdentifier(String identifier) {
        return "`" + identifier + "`";
    }

    @Override
    public Optional<String> defaultDriverName() {
        return Optional.of("org.apache.ignite.IgniteJdbcThinDriver");
    }

    @Override
    public Optional<String> getUpsertStatement(String tableName, String[] fieldNames, String[] uniqueKeyFields) {
        String columns = Arrays.stream(fieldNames)
                .map(this::quoteIdentifier)
                .collect(Collectors.joining(", "));

        String placeholders = Arrays.stream(fieldNames)
                .map(f -> ":" + f)
                .collect(Collectors.joining(", "));

        return Optional.of("MERGE INTO " + quoteIdentifier(tableName) + " (" + columns + ") VALUES (" + placeholders + ")");
    }

    @Override
    public Optional<Range> decimalPrecisionRange() {
        return Optional.of(Range.of(MIN_DECIMAL_PRECISION, MAX_DECIMAL_PRECISION));
    }

    @Override
    public Optional<Range> timestampPrecisionRange() {
        return Optional.of(Range.of(MIN_TIMESTAMP_PRECISION, MAX_TIMESTAMP_PRECISION));
    }

    @Override
    public Set<LogicalTypeRoot> supportedTypes() {
        return EnumSet.of(
                LogicalTypeRoot.CHAR,
                LogicalTypeRoot.VARCHAR,
                LogicalTypeRoot.BOOLEAN,
                LogicalTypeRoot.VARBINARY,
                LogicalTypeRoot.DECIMAL,
                LogicalTypeRoot.TINYINT,
                LogicalTypeRoot.SMALLINT,
                LogicalTypeRoot.INTEGER,
                LogicalTypeRoot.BIGINT,
                LogicalTypeRoot.FLOAT,
                LogicalTypeRoot.DOUBLE,
                LogicalTypeRoot.DATE,
                LogicalTypeRoot.TIME_WITHOUT_TIME_ZONE,
                LogicalTypeRoot.TIMESTAMP_WITHOUT_TIME_ZONE,
                LogicalTypeRoot.TIMESTAMP_WITH_LOCAL_TIME_ZONE,
                LogicalTypeRoot.ARRAY
        );
    }
}


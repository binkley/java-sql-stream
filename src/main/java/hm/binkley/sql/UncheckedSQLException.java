package hm.binkley.sql;

import java.sql.SQLException;

@SuppressWarnings("serial")
public final class UncheckedSQLException
        extends RuntimeException {
    public UncheckedSQLException(final SQLException cause) {
        super(cause);
    }

    @Override
    public synchronized SQLException getCause() {
        return SQLException.class.cast(super.getCause());
    }
}

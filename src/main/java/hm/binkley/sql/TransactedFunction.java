package hm.binkley.sql;

import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;

@RequiredArgsConstructor(staticName = "applyTransacted")
public final class TransactedFunction<T, R>
        implements SQLFunction<T, R> {
    private final Connection connection;
    private final SQLFunction<T, R> wrapped;

    @Override
    public R apply(final T in)
            throws SQLException {
        connection.setAutoCommit(false);
        try {
            final R out = wrapped.apply(in);
            connection.commit();
            return out;
        } catch (final SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }
}

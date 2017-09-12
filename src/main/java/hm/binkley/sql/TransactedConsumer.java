package hm.binkley.sql;

import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;

@RequiredArgsConstructor(staticName = "acceptTransacted")
public final class TransactedConsumer<T>
        implements SQLConsumer<T> {
    private final Connection connection;
    private final SQLConsumer<T> wrapped;

    @Override
    public void accept(final T t)
            throws SQLException {
        connection.setAutoCommit(false);
        try {
            wrapped.accept(t);
            connection.commit();
        } catch (final SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }
}

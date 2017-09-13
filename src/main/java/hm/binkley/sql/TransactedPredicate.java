package hm.binkley.sql;

import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;

@RequiredArgsConstructor(staticName = "testTransacted")
public final class TransactedPredicate<T>
        implements SQLPredicate<T> {
    private final Connection connection;
    private final SQLPredicate<T> wrapped;

    @Override
    public boolean test(final T t)
            throws SQLException {
        connection.setAutoCommit(false);
        try {
            final boolean answer = wrapped.test(t);
            connection.commit();
            return answer;
        } catch (final SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }
}

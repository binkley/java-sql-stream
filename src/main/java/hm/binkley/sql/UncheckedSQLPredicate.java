package hm.binkley.sql;

import lombok.RequiredArgsConstructor;

import java.sql.SQLException;
import java.util.function.Predicate;

@RequiredArgsConstructor(staticName = "testUnchecked")
public final class UncheckedSQLPredicate<T>
        implements Predicate<T> {
    private final SQLPredicate<T> wrapped;

    @Override
    public boolean test(final T t) {
        try {
            return wrapped.test(t);
        } catch (final SQLException e) {
            throw new UncheckedSQLException(e);
        }
    }
}

package hm.binkley.sql;

import lombok.RequiredArgsConstructor;

import java.sql.SQLException;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

@RequiredArgsConstructor(staticName = "testReported")
public final class ReportedSQLPredicate<T>
        implements Predicate<T> {
    private final SQLPredicate<T> wrapped;
    private final boolean failedValue;
    private final BiConsumer<T, SQLException> reported;

    @Override
    public boolean test(final T t) {
        try {
            return wrapped.test(t);
        } catch (final SQLException e) {
            reported.accept(t, e);
            return failedValue;
        }
    }
}

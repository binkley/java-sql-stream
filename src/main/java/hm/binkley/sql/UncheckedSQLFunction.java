package hm.binkley.sql;

import lombok.RequiredArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

@RequiredArgsConstructor(staticName = "applyUnchecked")
public final class UncheckedSQLFunction<T, R>
        implements Function<T, R> {
    private final SQLFunction<T, R> wrapped;

    @Override
    public R apply(final T t) {
        try {
            return wrapped.apply(t);
        } catch (final SQLException e) {
            throw new UncheckedSQLException(e);
        }
    }

    public static UncheckedSQLFunction<ResultSet, String> getString(
            final String label) {
        return applyUnchecked(results -> results.getString(label));
    }
}

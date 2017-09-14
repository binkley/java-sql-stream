package hm.binkley.sql;

import lombok.RequiredArgsConstructor;

import java.sql.SQLException;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

@RequiredArgsConstructor(staticName = "applyReported")
public final class ReportedSQLFunction<T, R>
        implements Function<T, Optional<R>> {
    private final SQLFunction<T, R> wrapped;
    private final BiConsumer<T, SQLException> reported;

    @Override
    public Optional<R> apply(final T t) {
        try {
            return Optional.of(wrapped.apply(t));
        } catch (final SQLException e) {
            reported.accept(t, e);
            return Optional.empty();
        }
    }
}

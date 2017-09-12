package hm.binkley.sql;

import lombok.RequiredArgsConstructor;

import java.sql.SQLException;
import java.util.function.Consumer;

@RequiredArgsConstructor(staticName = "acceptUnchecked")
public final class UncheckedSQLConsumer<T>
        implements Consumer<T> {
    private final SQLConsumer<T> wrapped;

    @Override
    public void accept(final T t) {
        try {
            wrapped.accept(t);
        } catch (final SQLException e) {
            throw new UncheckedSQLException(e);
        }
    }
}

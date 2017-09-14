package hm.binkley.sql;

import lombok.RequiredArgsConstructor;

import java.sql.SQLException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@RequiredArgsConstructor(staticName = "acceptReported")
public final class ReportedSQLConsumer<T>
        implements Consumer<T> {
    private final SQLConsumer<T> wrapped;
    private final BiConsumer<T, SQLException> reported;

    @Override
    public void accept(final T t) {
        try {
            wrapped.accept(t);
        } catch (final SQLException e) {
            reported.accept(t, e);
        }
    }
}

package hm.binkley.sql;

import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@RequiredArgsConstructor(staticName = "with")
public final class WithConnection {
    private final Connection connection;

    public <T> Predicate<T> testTransacted(final SQLPredicate<T> wrapped) {
        return UncheckedSQLPredicate.testUnchecked(
                TransactedPredicate.<T>testTransacted(connection, wrapped));
    }

    public <T, R> Function<T, R> applyTransacted(
            final SQLFunction<T, R> wrapped) {
        return UncheckedSQLFunction.applyUnchecked(
                TransactedFunction.<T, R>applyTransacted(connection,
                        wrapped));
    }

    public <T> Consumer<T> acceptTransacted(final SQLConsumer<T> wrapped) {
        return UncheckedSQLConsumer.acceptUnchecked(
                TransactedConsumer.<T>acceptTransacted(connection, wrapped));
    }
}

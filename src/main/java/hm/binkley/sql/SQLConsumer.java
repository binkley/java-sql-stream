package hm.binkley.sql;

import java.sql.SQLException;

@FunctionalInterface
public interface SQLConsumer<T> {
    void accept(T t)
            throws SQLException;

    default SQLConsumer<T> andThen(final SQLConsumer<? super T> after) {
        return (T t) -> {
            accept(t);
            after.accept(t);
        };
    }
}

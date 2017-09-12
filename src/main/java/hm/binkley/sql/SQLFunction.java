package hm.binkley.sql;

import java.sql.SQLException;

@FunctionalInterface
public interface SQLFunction<T, R> {
    R apply(final T in)
            throws SQLException;

    default <V> SQLFunction<V, R> compose(
            final SQLFunction<? super V, ? extends T> before) {
        return (V v) -> apply(before.apply(v));
    }

    default <V> SQLFunction<T, V> andThen(
            final SQLFunction<? super R, ? extends V> after) {
        return (T t) -> after.apply(apply(t));
    }
}

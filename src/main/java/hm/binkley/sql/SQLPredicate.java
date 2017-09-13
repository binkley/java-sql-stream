package hm.binkley.sql;

import java.sql.SQLException;

@FunctionalInterface
public interface SQLPredicate<T> {
    boolean test(final T t)
            throws SQLException;

    default SQLPredicate<T> and(final SQLPredicate<? super T> other) {
        return t -> test(t) && other.test(t);
    }

    default SQLPredicate<T> negate() {
        return t -> !test(t);
    }

    default SQLPredicate<T> or(final SQLPredicate<? super T> other) {
        return t -> test(t) || other.test(t);
    }
}

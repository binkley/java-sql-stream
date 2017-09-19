package hm.binkley.sql;

import lombok.RequiredArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Spliterator.IMMUTABLE;
import static java.util.Spliterator.NONNULL;
import static java.util.Spliterator.ORDERED;
import static java.util.Spliterators.spliteratorUnknownSize;

@RequiredArgsConstructor(staticName = "over")
public final class ResultSetIterator
        implements Iterator<ResultSet> {
    private final ResultSet results;
    private boolean more;

    public static Stream<ResultSet> stream(final ResultSet results) {
        return StreamSupport.
                stream(spliteratorUnknownSize(over(results),
                        IMMUTABLE | NONNULL | ORDERED), false);
    }

    public static Iterable<ResultSet> iterable(final ResultSet results) {
        return () -> over(results);
    }

    @Override
    public boolean hasNext() {
        try {
            more = results.next();
            return more;
        } catch (final SQLException e) {
            throw new UncheckedSQLException(e);
        }
    }

    @Override
    public ResultSet next() {
        try {
            if (more) {
                return results;
            }
            throw new NoSuchElementException();
        } finally {
            more = false;
        }
    }
}

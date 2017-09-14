package hm.binkley.sql;

import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static hm.binkley.sql.ReportedSQLPredicate.testReported;
import static hm.binkley.sql.WithConnection.with;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public final class StreamTest {
    private final Connection connection = mock(Connection.class);

    @Test
    public void shouldSucceedWithPredicate()
            throws SQLException {
        final Integer value = Stream.of(0).
                filter(with(connection).testTransacted(in -> true)).
                findFirst().
                get();

        assertThat(value, is(equalTo(0)));
        verify(connection).commit();
    }

    @SuppressWarnings("WhitespaceAround")
    @Test
    public void shouldFailWithPredicate()
            throws SQLException {
        final SQLException cause = new SQLException();
        try {
            Stream.of(0).
                    filter(with(connection).testTransacted(in -> {
                        throw cause;
                    })).
                    forEach(in -> {});
        } catch (final UncheckedSQLException e) {
            assertThat(e.getCause(), is(sameInstance(cause)));
            verify(connection).rollback();
        }
    }

    @Test
    public void shouldSucceedWithFunction()
            throws SQLException {
        final Integer value = Stream.of("string").
                map(with(connection).applyTransacted(in -> 0)).
                findFirst().
                get();

        assertThat(value, is(equalTo(0)));
        verify(connection).commit();
    }

    @SuppressWarnings("WhitespaceAround")
    @Test
    public void shouldFailWithFunction()
            throws SQLException {
        final SQLException cause = new SQLException();
        try {
            Stream.of("string").
                    map(with(connection).applyTransacted(in -> {
                        throw cause;
                    })).
                    forEach(in -> {});
        } catch (final UncheckedSQLException e) {
            assertThat(e.getCause(), is(sameInstance(cause)));
            verify(connection).rollback();
        }
    }

    @SuppressWarnings("WhitespaceAround")
    @Test
    public void shouldSucceedWithConsumer()
            throws SQLException {
        Stream.of(0).
                forEach(with(connection).acceptTransacted(in -> {}));

        verify(connection).commit();
    }

    @SuppressWarnings("WhitespaceAround")
    @Test
    public void shouldFailWithConsumer()
            throws SQLException {
        final SQLException cause = new SQLException();
        try {
            Stream.of(0).
                    forEach(with(connection).acceptTransacted(in -> {
                        throw cause;
                    }));
        } catch (final UncheckedSQLException e) {
            assertThat(e.getCause(), is(sameInstance(cause)));
            verify(connection).rollback();
        }
    }

    @Test
    public void shouldReport() {
        final Map<String, SQLException> reported = new HashMap<>();
        final long count = Stream.of("a", "b", "c").
                filter(testReported(StreamTest::throwOnB, false,
                        reported::put)).
                count();

        assertThat(count, is(equalTo(2L)));
        assertThat(reported, is(aMapWithSize(1)));
    }

    private static boolean throwOnB(final String in)
            throws SQLException {
        switch (in) {
        case "b":
            throw new SQLException();
        default:
            return true;
        }
    }
}

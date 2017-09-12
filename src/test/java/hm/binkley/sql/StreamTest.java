package hm.binkley.sql;

import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.stream.Stream;

import static hm.binkley.sql.TransactedConsumer.acceptTransacted;
import static hm.binkley.sql.TransactedFunction.applyTransacted;
import static hm.binkley.sql.UncheckedSQLConsumer.acceptUnchecked;
import static hm.binkley.sql.UncheckedSQLFunction.applyUnchecked;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public final class StreamTest {
    private final Connection connection = mock(Connection.class);

    @Test
    public void shouldSucceedWithFunction()
            throws SQLException {
        final Integer value = Stream.of("string").
                map(applyUnchecked(applyTransacted(connection, in -> 0))).
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
                    map(applyUnchecked(applyTransacted(connection, in -> {
                        throw cause;
                    }))).
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
                forEach(acceptUnchecked(
                        acceptTransacted(connection, in -> {})));

        verify(connection).commit();
    }

    @SuppressWarnings("WhitespaceAround")
    @Test
    public void shouldFailWithConsumer()
            throws SQLException {
        final SQLException cause = new SQLException();
        try {
            Stream.of(0).
                    forEach(acceptUnchecked(
                            acceptTransacted(connection, in -> {
                                throw cause;
                            })));
        } catch (final UncheckedSQLException e) {
            assertThat(e.getCause(), is(sameInstance(cause)));
            verify(connection).rollback();
        }
    }
}

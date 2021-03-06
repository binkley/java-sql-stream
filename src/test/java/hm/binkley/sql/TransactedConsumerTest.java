package hm.binkley.sql;

import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static hm.binkley.sql.TransactedConsumer.acceptTransacted;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public final class TransactedConsumerTest {
    private final Connection connection = mock(Connection.class);

    @SuppressWarnings("WhitespaceAround")
    @Test
    public void shouldSucceed()
            throws SQLException {
        acceptTransacted(connection, in -> {}).accept(0);

        verify(connection).commit();
    }

    @Test
    public void shouldFail()
            throws SQLException {
        final SQLException cause = new SQLException();
        try {
            acceptTransacted(connection, in -> {
                throw cause;
            }).accept(0);
            fail();
        } catch (final SQLException e) {
            assertThat(e, is(equalTo(cause)));
            verify(connection).rollback();
        }
    }
}

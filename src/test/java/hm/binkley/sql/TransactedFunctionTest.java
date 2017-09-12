package hm.binkley.sql;

import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static hm.binkley.sql.TransactedFunction.applyTransacted;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public final class TransactedFunctionTest {
    private final Connection connection = mock(Connection.class);

    @Test
    public void shouldSucceed()
            throws SQLException {
        final Integer value = applyTransacted(connection, in -> 0)
                .apply("string");

        assertThat(value, is(equalTo(0)));
        verify(connection).commit();
    }

    @Test
    public void shouldFail()
            throws SQLException {
        final SQLException cause = new SQLException();
        try {
            applyTransacted(connection, in -> {
                throw cause;
            }).apply("string");
            fail();
        } catch (final SQLException e) {
            assertThat(e, is(equalTo(cause)));
            verify(connection).rollback();
        }
    }
}

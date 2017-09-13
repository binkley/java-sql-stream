package hm.binkley.sql;

import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static hm.binkley.sql.TransactedPredicate.testTransacted;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public final class TransactedPredicateTest {
    private final Connection connection = mock(Connection.class);

    @Test
    public void shouldSucceed()
            throws SQLException {
        final boolean answer = testTransacted(connection, in -> true).test(0);

        assertTrue(answer);
        verify(connection).commit();
    }

    @Test
    public void shouldFail()
            throws SQLException {
        final SQLException cause = new SQLException();
        try {
            testTransacted(connection, in -> {
                throw cause;
            }).test(0);
            fail();
        } catch (final SQLException e) {
            assertThat(e, is(equalTo(cause)));
            verify(connection).rollback();
        }
    }
}

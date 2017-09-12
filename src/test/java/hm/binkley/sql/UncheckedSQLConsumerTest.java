package hm.binkley.sql;

import org.junit.Test;

import java.sql.SQLException;

import static hm.binkley.sql.UncheckedSQLConsumer.acceptUnchecked;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public final class UncheckedSQLConsumerTest {
    @SuppressWarnings("WhitespaceAround")
    @Test
    public void shouldSucceed() {
        acceptUnchecked(in -> {}).accept("string");
    }

    @Test
    public void shouldFail() {
        final SQLException cause = new SQLException();
        try {
            acceptUnchecked(in -> {
                throw cause;
            }).accept("string");
            fail();
        } catch (final UncheckedSQLException e) {
            assertThat(e.getCause(), is(sameInstance(cause)));
        }
    }
}

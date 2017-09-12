package hm.binkley.sql;

import org.junit.Test;

import java.sql.SQLException;

import static hm.binkley.sql.UncheckedSQLFunction.applyUnchecked;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public final class UncheckedSQLFunctionTest {
    @Test
    public void shouldSucceed() {
        final Integer value = applyUnchecked(in -> 0).apply("string");

        assertThat(value, is(equalTo(0)));
    }

    @Test
    public void shouldFail() {
        final SQLException cause = new SQLException();
        try {
            applyUnchecked(in -> {
                throw cause;
            }).apply("string");
            fail();
        } catch (final UncheckedSQLException e) {
            assertThat(e.getCause(), is(sameInstance(cause)));
        }
    }
}

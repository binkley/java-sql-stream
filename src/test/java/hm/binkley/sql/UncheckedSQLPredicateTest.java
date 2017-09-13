package hm.binkley.sql;

import org.junit.Test;

import java.sql.SQLException;

import static hm.binkley.sql.UncheckedSQLPredicate.testUnchecked;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public final class UncheckedSQLPredicateTest {
    @Test
    public void shouldSucceed() {
        testUnchecked(in -> true).test(0);
    }

    @Test
    public void shouldFail() {
        final SQLException cause = new SQLException();
        try {
            testUnchecked(in -> {
                throw cause;
            }).test(0);
            fail();
        } catch (final UncheckedSQLException e) {
            assertThat(e.getCause(), is(sameInstance(cause)));
        }
    }
}

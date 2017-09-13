package hm.binkley.sql;

import org.junit.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static hm.binkley.sql.ReportedSQLPredicate.testReported;
import static java.util.Collections.singletonMap;
import static org.hamcrest.Matchers.anEmptyMap;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public final class ReportedSQLPredicateTest {
    @Test
    public void shouldSucceed() {
        final Map<Integer, SQLException> errors = new HashMap<>();

        final boolean value = testReported(in -> true, false, errors::put).
                test(0);

        assertTrue(value);
        assertThat(errors, is(anEmptyMap()));
    }

    @Test
    public void shouldFail() {
        final SQLException cause = new SQLException();
        final Map<Integer, SQLException> errors = new HashMap<>();

        final boolean value = testReported(in -> {
            throw cause;
        }, false, errors::put).
                test(0);

        assertFalse(value);
        assertThat(errors, is(equalTo(singletonMap(0, cause))));
    }
}

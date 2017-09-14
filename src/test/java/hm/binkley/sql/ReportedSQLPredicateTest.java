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
        final Map<Integer, SQLException> reported = new HashMap<>();

        final boolean value = testReported(in -> true, false, reported::put).
                test(0);

        assertTrue(value);
        assertThat(reported, is(anEmptyMap()));
    }

    @Test
    public void shouldFail() {
        final SQLException cause = new SQLException();
        final Map<Integer, SQLException> reported = new HashMap<>();

        final boolean value = testReported(in -> {
            throw cause;
        }, false, reported::put).
                test(0);

        assertFalse(value);
        assertThat(reported, is(equalTo(singletonMap(0, cause))));
    }
}

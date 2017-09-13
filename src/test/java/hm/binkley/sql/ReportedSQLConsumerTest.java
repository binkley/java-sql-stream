package hm.binkley.sql;

import org.junit.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static hm.binkley.sql.ReportedSQLConsumer.acceptReported;
import static java.util.Collections.singletonMap;
import static org.hamcrest.Matchers.anEmptyMap;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class ReportedSQLConsumerTest {
    @SuppressWarnings("WhitespaceAround")
    @Test
    public void shouldSucceed() {
        final Map<Integer, SQLException> errors = new HashMap<>();

        acceptReported(in -> {}, errors::put).accept(0);

        assertThat(errors, is(anEmptyMap()));
    }

    @Test
    public void shouldFail() {
        final SQLException cause = new SQLException();
        final Map<Integer, SQLException> errors = new HashMap<>();
        acceptReported(in -> {
            throw cause;
        }, errors::put).accept(0);

        assertThat(errors, is(equalTo(singletonMap(0, cause))));
    }
}

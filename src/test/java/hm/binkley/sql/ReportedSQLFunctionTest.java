package hm.binkley.sql;

import org.junit.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static hm.binkley.sql.ReportedSQLFunction.applyReported;
import static java.util.Collections.singletonMap;
import static org.hamcrest.Matchers.anEmptyMap;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class ReportedSQLFunctionTest {
    @Test
    public void shouldSucceed() {
        final Map<String, SQLException> errors = new HashMap<>();

        final Optional<Integer> value = applyReported((String in) -> 0,
                errors::put).
                apply("String");

        assertThat(value, is(equalTo(Optional.of(0))));
        assertThat(errors, is(anEmptyMap()));
    }

    @Test
    public void shouldFail() {
        final SQLException cause = new SQLException();
        final Map<String, SQLException> errors = new HashMap<>();

        final Optional<Integer> value
                = ReportedSQLFunction.<String, Integer>applyReported(in -> {
            throw cause;
        }, errors::put).
                apply("string");

        assertThat(value, is(equalTo(Optional.empty())));
        assertThat(errors, is(equalTo(singletonMap("string", cause))));
    }
}

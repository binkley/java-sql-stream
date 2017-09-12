package hm.binkley.sql;

import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.PI;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class SQLFunctionTest {
    @Test
    public void shouldCompose()
            throws SQLException {
        final List<Integer> ran = new ArrayList<>();
        final SQLFunction<String, Integer> map = in -> {
            ran.add(1);
            return 0;
        };

        final Integer value = map.compose(in -> {
            ran.add(2);
            return "string";
        }).apply(PI);

        assertThat(value, is(equalTo(0)));
        assertThat(ran, is(equalTo(asList(2, 1))));
    }

    @Test
    public void shouldAndThen()
            throws SQLException {
        final List<Integer> ran = new ArrayList<>();
        final SQLFunction<Double, String> map = in -> {
            ran.add(1);
            return "string";
        };

        final Integer value = map.andThen(in -> {
            ran.add(2);
            return 0;
        }).apply(PI);

        assertThat(value, is(equalTo(0)));
        assertThat(ran, is(equalTo(asList(1, 2))));
    }
}

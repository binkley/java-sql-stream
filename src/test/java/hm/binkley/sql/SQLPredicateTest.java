package hm.binkley.sql;

import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public final class SQLPredicateTest {
    @Test
    public void shouldAndLeft()
            throws SQLException {
        final List<Integer> ran = new ArrayList<>();
        final SQLPredicate<Integer> a = in -> {
            ran.add(1);
            return false;
        };

        final boolean answer = a.and(in -> {
            ran.add(2);
            return true;
        }).test(0);

        assertFalse(answer);
        assertThat(ran, is(equalTo(singletonList(1))));
    }

    @Test
    public void shouldAndRight()
            throws SQLException {
        final List<Integer> ran = new ArrayList<>();
        final SQLPredicate<Integer> a = in -> {
            ran.add(1);
            return true;
        };

        final boolean answer = a.and(in -> {
            ran.add(2);
            return true;
        }).test(0);

        assertTrue(answer);
        assertThat(ran, is(equalTo(asList(1, 2))));
    }

    @Test
    public void shouldNegateTrue()
            throws SQLException {
        final List<Integer> ran = new ArrayList<>();
        final SQLPredicate<Integer> a = in -> {
            ran.add(1);
            return true;
        };

        assertFalse(a.negate().test(0));
        assertThat(ran, is(equalTo(singletonList(1))));
    }

    @Test
    public void shouldNegateFalse()
            throws SQLException {
        final List<Integer> ran = new ArrayList<>();
        final SQLPredicate<Integer> a = in -> {
            ran.add(1);
            return false;
        };

        assertTrue(a.negate().test(0));
        assertThat(ran, is(equalTo(singletonList(1))));
    }

    @Test
    public void shouldOrLeft()
            throws SQLException {
        final List<Integer> ran = new ArrayList<>();
        final SQLPredicate<Integer> a = in -> {
            ran.add(1);
            return true;
        };

        final boolean answer = a.or(in -> {
            ran.add(2);
            return false;
        }).test(0);

        assertTrue(answer);
        assertThat(ran, is(equalTo(singletonList(1))));
    }

    @Test
    public void shouldOrRight()
            throws SQLException {
        final List<Integer> ran = new ArrayList<>();
        final SQLPredicate<Integer> a = in -> {
            ran.add(1);
            return false;
        };

        final boolean answer = a.or(in -> {
            ran.add(2);
            return false;
        }).test(0);

        assertFalse(answer);
        assertThat(ran, is(equalTo(asList(1, 2))));
    }
}

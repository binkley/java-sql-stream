package hm.binkley.sql;

import org.junit.After;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static hm.binkley.sql.ResultSetIterator.iterable;
import static hm.binkley.sql.ResultSetIterator.over;
import static hm.binkley.sql.ResultSetIterator.stream;
import static hm.binkley.sql.UncheckedSQLFunction.getString;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public final class ResultSetIteratorTest {
    private final ResultSet results = mock(ResultSet.class);
    private final ResultSetIterator iter = over(results);

    @After
    public void tearDown() {
        reset(results);
    }

    @Test
    public void shouldIterate()
            throws SQLException {
        when(results.next()).thenReturn(true, false);
        when(results.getString("value")).
                thenReturn("a").
                thenThrow(AssertionError.class);

        final List<String> values = new ArrayList<>();
        for (final ResultSet row : iterable(results)) {
            values.add(row.getString("value"));
        }

        assertThat(values, is(equalTo(singletonList("a"))));
    }

    @Test
    public void shouldStream()
            throws SQLException {
        when(results.next()).thenReturn(true, false);
        when(results.getString("value")).
                thenReturn("a").
                thenThrow(AssertionError.class);

        final List<String> values = stream(results).
                map(getString("value")).
                collect(toList());

        assertThat(values, is(equalTo(singletonList("a"))));
    }

    @Test
    public void shouldSucceed()
            throws SQLException {
        when(results.next()).thenReturn(true, false);
        when(results.getString("value")).
                thenReturn("a").
                thenThrow(AssertionError.class);

        final List<String> values = stream(results).
                map(getString("value")).
                collect(toList());

        assertThat(values, is(equalTo(singletonList("a"))));
    }

    @Test
    public void shouldThrowOnHasNext()
            throws SQLException {
        final SQLException cause = new SQLException();
        when(results.next()).thenThrow(cause);

        try {
            iter.hasNext();
            fail();
        } catch (final UncheckedSQLException e) {
            assertThat(e.getCause(), is(sameInstance(cause)));
        }
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldComplainIfNextWithoutHasNext() {
        iter.next();
    }
}

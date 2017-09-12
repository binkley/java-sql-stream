package hm.binkley.sql;

import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class SQLConsumerTest {
    @Test
    public void shouldAndThen()
            throws SQLException {
        final List<Integer> ran = new ArrayList<>();
        final SQLConsumer<Integer> sink = in -> ran.add(1);

        sink.andThen(in -> ran.add(2)).accept(0);

        assertThat(ran, is(equalTo(asList(1, 2))));
    }
}

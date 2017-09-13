# Java helpers for JDBC and Streams

*tl;dr* &mdash; _Jump to [SteamTest](src/test/java/hm/binkley/sql/StreamTest.java)
to see a full code example_

Java 8 Streams are a great addition to the JDK, however they have some 
restrictions which make them awkward to use at times.  In particular, the 
streams methods do not support checked exceptions: only methods throwing 
unchecked exceptions can be used in a stream.

Over time, the thinking around checked exceptions has shifted.  Originally 
a distinguishing feature of Java, presently they are frowned on by most 
developers and API writers, the JDK authors included.  Even the official 
documentation [calls them
controversial](https://docs.oracle.com/javase/tutorial/essential/exceptions/runtime.html).

One of the oldest APIs in the JDK (starting with 1.1), JDBC is
full-throated in use of checked exceptions.  The `SQLException` class 
is the root of a 20-class hierarchy of exceptions.

## Mixing JDBC with Streams

To work around the unchecked exception restriction, start by wrapping 
`SQLException` (a checked exception) with an unchecked exception, 
[`UncheckedSQLException`](src/main/java/hm/binkley/sql/UncheckedSQLException.java).
The model is `UncheckedIOException` in the JDK, added in JDK 1.8.  So you 
might write code such as:

```java
public long countThem(final SomethingStreamable<SomeThing> things) {
    return things.stream().
            map(this::readFromJdbc)
            count();
}

private SomeOtherThing readFromJdbc(final SomeThing oneThing) {
    try {
        return oneThing.asSomeOtherThing(); // throws SQLException
    } catch (final SQLException e) {
        throw new UncheckedSQLException(e);
    }
}
```

Over time, as more of these low-value wrapper methods are needed, it raises 
the overall whitenoise level of the code, and hides the real intent.

### Solution

Rather, take a different approach: use a general wrapping function directly
in the stream, and avoid writing hand-rolled wrappers:

```java
public long countThem(final SomethingStreamable<SomeThing> things) {
    return things.stream().
            map(applyUnchecked(SomeThing::asSomeOtherThing))
            count();
}
```

## Transactions

Another awkward JDBC API is the one for transactions.  A typical pattern 
is along the lines (there are variations):

```java
connection.setAutoCommit(false);
try {
    doWork(); // throws SQLException
    connection.commit();
} catch (final SQLException e) {
    connection.rollback();
    throw e;
} finally {
    connection.setAutoCommit(true);
}
```

So using a transaction within a stream method is verbose, generally needing 
a wrapper method for readability.

### Solution

Rather, again use a wrapper function rather than hand-roll:

```java
someStream.
        forEach(acceptUnchecked(acceptTransacted(this::doWork)));
```

## Result sets

Unfortunately as JDBC predates both Java 5 for-each loops and Java 8 
streams, looping through a result set has a unique API which is 
incompatible with either.  `StreamSupport.stream()` can wrap an iterator to
produce a stream, but first one needs the iterator.

### Solution

```java
final long poorMansRowCount = ResultSetIterator.stream(results).count();
```

## This library

* Unchecked predicates - use [`UncheckedSQLPredicate.testUnchecked(wrapped)`](src/main/java/hm/binkley/sql/UncheckedSQLPredicate.java)
* Unchecked functions - use [`UncheckedSQLFunction.applyUnchecked(wrapped)`](src/main/java/hm/binkley/sql/UncheckedSQLFunction.java)
* Unchecked consumers - use [`UncheckedSQLConsumer.acceptUnchecked(wrapped)`](src/main/java/hm/binkley/sql/UncheckedSQLConsumer.java)
* Transacted predicates - use [`TransactedSQLPredicate.testTransacted(connection, wrapped)`](src/main/java/hm/binkley/sql/TransactedSQLPredicate.java)
* Transacted functions - use [`TransactedSQLFunction.applyTransacted(connection, wrapped)`](src/main/java/hm/binkley/sql/TransactedSQLFunction.java)
* Transacted consumers - use [`TransactedSQLConsumer.acceptTransacted(connection, wrapped)`](src/main/java/hm/binkley/sql/TransactedSQLConsumer.java)

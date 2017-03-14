package org.icij.kaxxa.sql.concurrent;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.locks.Lock;

public class SQLBlockingQueue<E> extends AbstractBlockingQueue<E> {

	protected final DataSource ds;
	private final SQLBlockingQueueAdapter<E> adapter;

	public SQLBlockingQueue(final DataSource ds, final Lock lock, final SQLBlockingQueueAdapter<E> adapter) {
		super(lock);
		this.ds = ds;
		this.adapter = adapter;
	}

	private <R> R withConnection(final CheckedFunction<Connection, R> function) {
		try (final Connection c = ds.getConnection()) {
			return function.apply(c);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int size() {
		return withConnection(adapter::size);
	}

	@Override
	public E peek() {
		return withConnection(adapter::peek);
	}

	@Override
	public boolean contains(final Object o) {
		Objects.requireNonNull(o);
		return withConnection(c -> adapter.contains(c, o));
	}

	@Override
	public boolean remove(final Object o) {
		Objects.requireNonNull(o);
		return withConnection(c -> adapter.remove(c, o) > 0);
	}

	@Override
	public void clear() {
		withConnection(adapter::clear);
	}

	@Override
	public E poll() {
		return withConnection(adapter::poll);
	}

	@Override
	public boolean add(final E element) {
		Objects.requireNonNull(element);
		return withConnection(c -> adapter.add(c, element)) > 0;
	}
}

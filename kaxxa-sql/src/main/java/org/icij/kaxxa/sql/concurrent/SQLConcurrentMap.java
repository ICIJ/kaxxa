package org.icij.kaxxa.sql.concurrent;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class SQLConcurrentMap<K, V> extends AbstractConcurrentMap<K, V> {

	protected final DataSource ds;
	private final SQLConcurrentMapAdapter<K, V> adapter;

	public SQLConcurrentMap(final DataSource ds, final SQLConcurrentMapAdapter<K, V> adapter) {
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
	public Set<K> keySet() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<Map.Entry<K,V>> entrySet() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<V> values() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		return withConnection(adapter::size);
	}

	@Override
	public boolean isEmpty() {
		return withConnection(adapter::isEmpty);
	}

	@Override
	public boolean containsKey(final Object key) {
		return withConnection(c -> adapter.containsKey(c, key));
	}

	@Override
	public V get(final Object key) {
		return withConnection(c -> adapter.get(c, key));
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		withConnection(c -> adapter.putAll(c, m));
	}

	@Override
	public boolean containsValue(final Object o) {
		return withConnection(c -> adapter.containsValue(c, o));
	}

	@Override
	public V put(final K key, final V value) {
		return withConnection(c -> adapter.put(c, key, value));
	}

	@Override
	public V putIfAbsent(final K key, final V value) {
		return withConnection(c -> adapter.putIfAbsent(c, key, value));
	}

	public boolean fastPut(final K key, final V value) {
		return withConnection(c -> adapter.fastPut(c, key, value));
	}

	@Override
	public boolean remove(final Object key, final Object value) {
		return withConnection(c -> adapter.remove(c, key, value) > 0);
	}

	@Override
	public V remove(final Object key) {
		return withConnection(c -> adapter.remove(c, key));
	}

	@Override
	public V replace(final K key, final V value) {
		return withConnection(c -> adapter.replace(c, key, value));
	}

	@Override
	public boolean replace(final K key, final V oldValue, final V newValue) {
		return withConnection(c -> adapter.replace(c, key, oldValue, newValue) > 0);
	}

	@Override
	public void clear() {
		withConnection(adapter::clear);
	}
}

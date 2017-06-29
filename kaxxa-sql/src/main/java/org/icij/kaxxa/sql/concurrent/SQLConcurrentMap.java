package org.icij.kaxxa.sql.concurrent;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

public abstract class SQLConcurrentMap<K, V> implements ConcurrentMap<K, V> {

	protected final FunctionalDataSource dataSource;

	SQLConcurrentMap(final DataSource dataSource) {
		this.dataSource = FunctionalDataSource.cast(dataSource);
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

	public abstract boolean fastPut(final K key, final V value);
}

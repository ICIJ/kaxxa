package org.icij.kaxxa.sql.concurrent.locks;

import org.icij.kaxxa.sql.FunctionalDataSource;

import javax.sql.DataSource;
import java.util.concurrent.locks.Lock;

public abstract class SQLLock implements Lock {

	protected final FunctionalDataSource dataSource;

	public SQLLock(final DataSource dataSource) {
		this.dataSource = new FunctionalDataSource(dataSource);
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		if (Thread.interrupted()) {
			throw new InterruptedException();
		}

		lock();
	}
}

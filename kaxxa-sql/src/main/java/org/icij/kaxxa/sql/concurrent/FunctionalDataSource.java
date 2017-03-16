package org.icij.kaxxa.sql.concurrent;

import org.icij.kaxxa.sql.concurrent.function.CheckedConsumer;
import org.icij.kaxxa.sql.concurrent.function.CheckedFunction;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FunctionalDataSource extends DataSourceDecorator {

	public static FunctionalDataSource cast(final DataSource dataSource) {
		if (dataSource instanceof FunctionalDataSource) {
			return (FunctionalDataSource) dataSource;
		} else {
			return new FunctionalDataSource(dataSource);
		}
	}

	public FunctionalDataSource(final DataSource dataSource) {
		super(dataSource);
	}

	public void withConnection(final CheckedConsumer<Connection> consumer) {
		try (final Connection c = getConnection()) {
			consumer.accept(c);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public <R> R withConnection(final CheckedFunction<Connection, R> function) {
		try (final Connection c = getConnection()) {
			return function.apply(c);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void withStatement(final String statement, final CheckedConsumer<PreparedStatement> consumer) {
		withConnection(c -> {
			try (final PreparedStatement q = c.prepareStatement(statement)){
				consumer.accept(q);
			}
		});
	}

	public <R> R withStatement(final String statement, final CheckedFunction<PreparedStatement, R> function) {
		return withConnection(c -> {
			try (final PreparedStatement q = c.prepareStatement(statement)) {
				return function.apply(q);
			}
		});
	}
}

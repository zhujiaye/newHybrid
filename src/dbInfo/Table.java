package dbInfo;

import java.sql.Connection;
import java.sql.ResultSet;

import org.voltdb.VoltTable;
import org.voltdb.client.Client;

public abstract class Table {
	public abstract void update(Connection conn);

	public abstract void update(Client voltDBConn);

	public abstract void update(int idInVoltdb);

	public abstract ResultSet doSelectInMySQL();

	public abstract int doUpdateInMySQL();

	public abstract int doUpdateInVoltDB();

	public abstract VoltTable[] doSelectInVoltDB();
}

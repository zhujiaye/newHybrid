package utillity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import config.HConfig;

public class MySQLExporterForTenant implements Runnable {
	private String table_name;
	private int tenantid;
	private Connection conn;
	private String savedFileName;
	private MysqlConnectionPool pool;

	public MySQLExporterForTenant(String table_name, int tenantid) {
		this.table_name = table_name;
		this.tenantid = tenantid;
		this.savedFileName = HConfig.CSVPATH + "/" + table_name + tenantid
				+ ".csv";
		this.pool = new MysqlConnectionPool();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		long t1, t2;
		Statement stmt = null;
		t1 = System.currentTimeMillis();
		conn = pool.getConnection();
		try {
			stmt = conn.createStatement();
			stmt.execute("select *," + tenantid + ",0,0 from " + table_name
					+ (tenantid - 1) + " into outfile " + "'" + savedFileName
					+ "'" + "fields terminated by ',';");
			// if (stmt.execute("select *," + tenantid + ",0,0 from " +
			// table_name
			// + (tenantid - 1))){
			// ResultSet res=stmt.getResultSet();
			// res.close();
			// }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		t2 = System.currentTimeMillis();
		// System.out
		// .format("MySQL Export for table %s and tenant %d finished in %f minutes%n",
		// table_name, tenantid, ((double) (t2 - t1))
		// / (60.0 * 1000.0));
		pool.putConnection(conn);
		pool.clear();
	}
}

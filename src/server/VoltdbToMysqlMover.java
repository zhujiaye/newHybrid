package server;

import java.io.InterruptedIOException;
import java.sql.Connection;

import org.apache.log4j.Logger;
import org.voltdb.client.Client;

import config.Constants;
import retriver.CustomerRetriver;
import retriver.DistrictRetriver;
import retriver.HistoryRetriver;
import retriver.ItemRetriver;
import retriver.NewOrdersRetriver;
import retriver.OrderLineRetriver;
import retriver.OrdersRetriver;
import retriver.StockRetriver;
import retriver.WarehouseRetriver;
import utillity.MysqlConnectionPool;
import utillity.VoltdbConnectionPool;

//TODO make this class better
public class VoltdbToMysqlMover {

	final static private Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);
	public int tenantId, volumnId;
	private volatile boolean mIsCanceled = false;

	/**
	 * 
	 * @param tenantId
	 *            this variable is 0-index based
	 * @param volumnId
	 *            this variable is 0-index based
	 */
	public VoltdbToMysqlMover(int tenantId, int volumnId) {
		this.tenantId = tenantId;
		this.volumnId = volumnId;
	}

	/**
	 * move data from voltdb to msyql
	 * 
	 * @throws InterruptedException
	 * @throws InterruptedIOException
	 */
	public void move() throws InterruptedException, InterruptedIOException {
		MysqlConnectionPool mysql_pool = MysqlConnectionPool.getPool();
		VoltdbConnectionPool voltdb_pool = VoltdbConnectionPool.getPool();
		Connection conn;
		Client voltdbConn;

		if (mIsCanceled)
			return;
		conn = mysql_pool.getConnection();
		voltdbConn = voltdb_pool.getConnection();
		if (conn == null || voltdbConn == null) {
			LOG.error("No mysql or voltdb connections!");
			return;
		}
		CustomerRetriver cr = new CustomerRetriver(tenantId, volumnId, conn,
				voltdbConn);
		cr.move();
		mysql_pool.putConnection(conn);
		voltdb_pool.putConnection(voltdbConn);

		if (mIsCanceled)
			return;
		conn = mysql_pool.getConnection();
		voltdbConn = voltdb_pool.getConnection();
		if (conn == null || voltdbConn == null) {
			LOG.error("No mysql or voltdb connections!");
			return;
		}
		DistrictRetriver dr = new DistrictRetriver(tenantId, volumnId, conn,
				voltdbConn);
		dr.move();
		mysql_pool.putConnection(conn);
		voltdb_pool.putConnection(voltdbConn);

		if (mIsCanceled)
			return;
		conn = mysql_pool.getConnection();
		voltdbConn = voltdb_pool.getConnection();
		if (conn == null || voltdbConn == null) {
			LOG.error("No mysql or voltdb connections!");
			return;
		}
		HistoryRetriver hr = new HistoryRetriver(tenantId, volumnId, conn,
				voltdbConn);
		hr.move();
		mysql_pool.putConnection(conn);
		voltdb_pool.putConnection(voltdbConn);

		if (mIsCanceled)
			return;
		conn = mysql_pool.getConnection();
		voltdbConn = voltdb_pool.getConnection();
		if (conn == null || voltdbConn == null) {
			LOG.error("No mysql or voltdb connections!");
			return;
		}
		NewOrdersRetriver nor = new NewOrdersRetriver(tenantId, volumnId, conn,
				voltdbConn);
		nor.move();
		mysql_pool.putConnection(conn);
		voltdb_pool.putConnection(voltdbConn);

		if (mIsCanceled)
			return;
		conn = mysql_pool.getConnection();
		voltdbConn = voltdb_pool.getConnection();
		if (conn == null || voltdbConn == null) {
			LOG.error("No mysql or voltdb connections!");
			return;
		}
		OrderLineRetriver olr = new OrderLineRetriver(tenantId, volumnId, conn,
				voltdbConn);
		olr.move();
		mysql_pool.putConnection(conn);
		voltdb_pool.putConnection(voltdbConn);

		if (mIsCanceled)
			return;
		conn = mysql_pool.getConnection();
		voltdbConn = voltdb_pool.getConnection();
		if (conn == null || voltdbConn == null) {
			LOG.error("No mysql or voltdb connections!");
			return;
		}
		OrdersRetriver or = new OrdersRetriver(tenantId, volumnId, conn,
				voltdbConn);
		or.move();
		mysql_pool.putConnection(conn);
		voltdb_pool.putConnection(voltdbConn);

		if (mIsCanceled)
			return;
		conn = mysql_pool.getConnection();
		voltdbConn = voltdb_pool.getConnection();
		if (conn == null || voltdbConn == null) {
			LOG.error("No mysql or voltdb connections!");
			return;
		}
		StockRetriver sr = new StockRetriver(tenantId, volumnId, conn,
				voltdbConn);
		sr.move();
		mysql_pool.putConnection(conn);
		voltdb_pool.putConnection(voltdbConn);

		if (mIsCanceled)
			return;
		conn = mysql_pool.getConnection();
		voltdbConn = voltdb_pool.getConnection();
		if (conn == null || voltdbConn == null) {
			LOG.error("No mysql or voltdb connections!");
			return;
		}
		WarehouseRetriver wr = new WarehouseRetriver(tenantId, volumnId, conn,
				voltdbConn);
		wr.move();
		mysql_pool.putConnection(conn);
		voltdb_pool.putConnection(voltdbConn);

		if (mIsCanceled)
			return;
		conn = mysql_pool.getConnection();
		voltdbConn = voltdb_pool.getConnection();
		if (conn == null || voltdbConn == null) {
			LOG.error("No mysql or voltdb connections!");
			return;
		}
		ItemRetriver ir = new ItemRetriver(tenantId, volumnId, conn, voltdbConn);
		ir.move();
		mysql_pool.putConnection(conn);
		voltdb_pool.putConnection(voltdbConn);
	}

	public void cancel() {
		mIsCanceled = true;
	}
}

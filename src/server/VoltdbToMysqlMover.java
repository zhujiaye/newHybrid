package server;

import java.sql.Connection;

import newhybrid.HException;

import org.voltdb.client.Client;

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

public class VoltdbToMysqlMover {

	public int tenantId, volumnId;

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
	 * @throws HException
	 */
	public void move() throws HException {
		MysqlConnectionPool mysql_pool = new MysqlConnectionPool();
		VoltdbConnectionPool voltdb_pool = new VoltdbConnectionPool();
		Connection conn = mysql_pool.getConnection();
		Client voltdbConn = voltdb_pool.getConnection();
		
		conn = mysql_pool.getConnection();
		voltdbConn = voltdb_pool.getConnection();
		CustomerRetriver cr = new CustomerRetriver(tenantId, volumnId, conn,
				voltdbConn);
		
		conn = mysql_pool.getConnection();
		voltdbConn = voltdb_pool.getConnection();
		DistrictRetriver dr = new DistrictRetriver(tenantId, volumnId, conn,
				voltdbConn);
		
		conn = mysql_pool.getConnection();
		voltdbConn = voltdb_pool.getConnection();
		HistoryRetriver hr = new HistoryRetriver(tenantId, volumnId, conn,
				voltdbConn);
		
		conn = mysql_pool.getConnection();
		voltdbConn = voltdb_pool.getConnection();
		NewOrdersRetriver nor = new NewOrdersRetriver(tenantId, volumnId, conn,
				voltdbConn);
		
		conn = mysql_pool.getConnection();
		voltdbConn = voltdb_pool.getConnection();
		OrderLineRetriver olr = new OrderLineRetriver(tenantId, volumnId, conn,
				voltdbConn);
		
		conn = mysql_pool.getConnection();
		voltdbConn = voltdb_pool.getConnection();
		OrdersRetriver or = new OrdersRetriver(tenantId, volumnId, conn,
				voltdbConn);
		
		conn = mysql_pool.getConnection();
		voltdbConn = voltdb_pool.getConnection();
		StockRetriver sr = new StockRetriver(tenantId, volumnId, conn,
				voltdbConn);
		
		conn = mysql_pool.getConnection();
		voltdbConn = voltdb_pool.getConnection();
		WarehouseRetriver wr = new WarehouseRetriver(tenantId, volumnId, conn,
				voltdbConn);
		
		conn = mysql_pool.getConnection();
		voltdbConn = voltdb_pool.getConnection();
		ItemRetriver ir = new ItemRetriver(tenantId, volumnId, conn, voltdbConn);
		
		cr.move();
		dr.move();
		hr.move();
		nor.move();
		olr.move();
		or.move();
		sr.move();
		wr.move();
		ir.move();
	}

}

package retriver;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.voltdb.VoltTable;
import org.voltdb.VoltTableRow;
import org.voltdb.VoltType;
import org.voltdb.client.Client;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.ProcCallException;

public class NewOrdersRetriver  {
	public String url, username, password;
	public String voltdbServer;
	public int tenantId;
	public int volumnId;

	public Connection conn = null;
	public Statement stmt = null;
	Client voltdbConn = null;
	PreparedStatement[] statements;

	public NewOrdersRetriver(int tenantId, int volumnId, Connection conn,
			Client voltdbConn) {
		this.tenantId = tenantId;
		this.volumnId = volumnId;
		this.conn = conn;
		this.voltdbConn = voltdbConn;
	}

	public void move() throws InterruptedException, InterruptedIOException {
		try {
			stmt = conn.createStatement();
			conn.setAutoCommit(false);
			statements = new PreparedStatement[2];
			statements[0] = conn
					.prepareStatement("UPDATE new_orders"
							+ tenantId
							+ " SET no_o_id = ?,no_d_id = ?,no_w_id = ? WHERE no_w_id = ? AND no_d_id = ? AND no_o_id = ?");
			statements[1] = conn.prepareStatement("INSERT INTO new_orders"
					+ tenantId + " VALUES (?,?,?)");
		} catch (Exception e1) {
			System.out
					.println("error in creating or preparing mysql statement for retriving data...");
		}
		if (voltdbConn == null) {
			System.out
					.println("error connecting to voltdb while retriving data...");
		}
		// ******************************************************************************//
		ClientResponse response = null;
		VoltTable result = null;
		try {
			response = voltdbConn.callProcedure("SelectNewOrders_" + volumnId,
					tenantId, 0, 1);
			if (response.getStatus() == ClientResponse.SUCCESS
					&& response.getResults()[0].getRowCount() != 0) {
				result = response.getResults()[0];
				for (int i = 0; i < result.getRowCount(); i++) {
					VoltTableRow row = result.fetchRow(i);
					statements[0].setInt(1,
							(int) row.get("no_o_id", VoltType.INTEGER));
					statements[0].setInt(2,
							(int) row.get("no_d_id", VoltType.INTEGER));
					statements[0].setInt(3,
							(int) row.get("no_w_id", VoltType.INTEGER));
					statements[0].setInt(4,
							(int) row.get("no_w_id", VoltType.INTEGER));
					statements[0].setInt(5,
							(int) row.get("no_d_id", VoltType.INTEGER));
					statements[0].setInt(6,
							(int) row.get("no_o_id", VoltType.INTEGER));
					statements[0].addBatch();
				}
				if (result.getRowCount() > 0) {
					statements[0].executeBatch();
					conn.commit();
				}
			}

			response = voltdbConn.callProcedure("SelectNewOrders_" + volumnId,
					tenantId, 1, 0);
			if (response.getStatus() == ClientResponse.SUCCESS
					&& response.getResults()[0].getRowCount() != 0) {
				result = response.getResults()[0];
				for (int i = 0; i < result.getRowCount(); i++) {
					VoltTableRow row = result.fetchRow(i);
					statements[1].setInt(1,
							(int) row.get("no_o_id", VoltType.INTEGER));
					statements[1].setInt(2,
							(int) row.get("no_d_id", VoltType.INTEGER));
					statements[1].setInt(3,
							(int) row.get("no_w_id", VoltType.INTEGER));
					// statements[1].addBatch();
					try {
						statements[1].execute();
						conn.commit();
					} catch (Exception e) {
					}
				}
				// if(result.getRowCount() > 0) {
				// statements[1].executeBatch();
				// }
			}

			response = voltdbConn.callProcedure("SelectNewOrders_" + volumnId,
					tenantId, 1, 1);
			if (response.getStatus() == ClientResponse.SUCCESS
					&& response.getResults()[0].getRowCount() != 0) {
				result = response.getResults()[0];
				for (int i = 0; i < result.getRowCount(); i++) {
					VoltTableRow row = result.fetchRow(i);
					statements[1].setInt(1,
							(int) row.get("no_o_id", VoltType.INTEGER));
					statements[1].setInt(2,
							(int) row.get("no_d_id", VoltType.INTEGER));
					statements[1].setInt(3,
							(int) row.get("no_w_id", VoltType.INTEGER));
					// statements[1].addBatch();
					try {
						statements[1].execute();
						conn.commit();
					} catch (Exception e) {
					}
				}
				// if(result.getRowCount() > 0) {
				// statements[1].executeBatch();
				// }
			}
			voltdbConn.callProcedure("@AdHoc", "DELETE FROM new_orders"
					+ volumnId + " WHERE tenant_id = " + tenantId);
		} catch (IOException | ProcCallException | SQLException e) {
			if (e instanceof InterruptedIOException)
				throw (InterruptedIOException) e;
			e.printStackTrace();
		}
		// System.out.println("\n new_orders: "+tenantId+" truncated...");
		// ******************************************************************************//
		try {
			conn.close();
			voltdbConn.close();
		} catch (SQLException  e) {
			e.printStackTrace();
		}

	}

}

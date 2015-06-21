package retriver;

import java.io.IOException;
import java.math.BigDecimal;
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

public class HistoryRetriver{
	public String url, username, password;
	public String voltdbServer;
	public int tenantId;
	public int volumnId;

	public Connection conn = null;
	public Statement stmt = null;
	Client voltdbConn = null;
	PreparedStatement[] statements;

	public HistoryRetriver(int tenantId, int volumnId, Connection conn,
			Client voltdbConn) {
		this.tenantId = tenantId;
		this.volumnId = volumnId;
		this.conn = conn;
		this.voltdbConn = voltdbConn;
	}

	public void move() {
		try {
			stmt = conn.createStatement();
			conn.setAutoCommit(false);
			statements = new PreparedStatement[2];
			statements[0] = conn
					.prepareStatement("UPDATE history"
							+ tenantId
							+ " SET h_c_id = ?, h_c_d_id = ?, h_c_w_id = ?,h_d_id = ?,h_w_id = ?,h_date = ?,h_amount = ?,h_data = ? WHERE h_c_id = ? AND h_c_d_id = ? AND h_c_w_id = ?");
			statements[1] = conn.prepareStatement("INSERT INTO history"
					+ tenantId + " VALUES (?,?,?,?,?,?,?,?)");
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
			response = voltdbConn.callProcedure("SelectHistory_" + volumnId,
					tenantId, 0, 1);
			if (response.getStatus() == ClientResponse.SUCCESS
					&& response.getResults()[0].getRowCount() != 0) {
				result = response.getResults()[0];
				for (int i = 0; i < result.getRowCount(); i++) {
					VoltTableRow row = result.fetchRow(i);
					statements[0].setInt(1,
							(int) row.get("h_c_id", VoltType.INTEGER));
					statements[0].setInt(2,
							(int) row.get("h_c_d_id", VoltType.INTEGER));
					statements[0].setInt(3,
							(int) row.get("h_c_w_id", VoltType.INTEGER));
					statements[0].setInt(4,
							(int) row.get("h_d_id", VoltType.INTEGER));
					statements[0].setInt(5,
							(int) row.get("h_w_id", VoltType.INTEGER));
					statements[0].setTimestamp(6,
							row.getTimestampAsSqlTimestamp("h_date"));
					statements[0].setBigDecimal(
							7,
							row.getDecimalAsBigDecimal("h_amount").setScale(6,
									BigDecimal.ROUND_HALF_DOWN));
					statements[0].setString(8, row.getString("h_data"));
					statements[0].setInt(9,
							(int) row.get("h_c_id", VoltType.INTEGER));
					statements[0].setInt(10,
							(int) row.get("h_c_d_id", VoltType.INTEGER));
					statements[0].setInt(11,
							(int) row.get("h_c_w_id", VoltType.INTEGER));
					statements[0].addBatch();
				}
				if (result.getRowCount() > 0) {
					statements[0].executeBatch();
					conn.commit();
				}
			}

			response = voltdbConn.callProcedure("SelectHistory_" + volumnId,
					tenantId, 1, 0);
			if (response.getStatus() == ClientResponse.SUCCESS
					&& response.getResults()[0].getRowCount() != 0) {
				result = response.getResults()[0];
				for (int i = 0; i < result.getRowCount(); i++) {
					VoltTableRow row = result.fetchRow(i);
					statements[1].setInt(1,
							(int) row.get("h_c_id", VoltType.INTEGER));
					statements[1].setInt(2,
							(int) row.get("h_c_d_id", VoltType.INTEGER));
					statements[1].setInt(3,
							(int) row.get("h_c_w_id", VoltType.INTEGER));
					statements[1].setInt(4,
							(int) row.get("h_d_id", VoltType.INTEGER));
					statements[1].setInt(5,
							(int) row.get("h_w_id", VoltType.INTEGER));
					statements[1].setTimestamp(6,
							row.getTimestampAsSqlTimestamp("h_date"));
					statements[1].setBigDecimal(
							7,
							row.getDecimalAsBigDecimal("h_amount").setScale(6,
									BigDecimal.ROUND_HALF_DOWN));
					statements[1].setString(8, row.getString("h_data"));
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

			response = voltdbConn.callProcedure("SelectHistory_" + volumnId,
					tenantId, 1, 1);
			if (response.getStatus() == ClientResponse.SUCCESS
					&& response.getResults()[0].getRowCount() != 0) {
				result = response.getResults()[0];
				for (int i = 0; i < result.getRowCount(); i++) {
					VoltTableRow row = result.fetchRow(i);
					statements[1].setInt(1,
							(int) row.get("h_c_id", VoltType.INTEGER));
					statements[1].setInt(2,
							(int) row.get("h_c_d_id", VoltType.INTEGER));
					statements[1].setInt(3,
							(int) row.get("h_c_w_id", VoltType.INTEGER));
					statements[1].setInt(4,
							(int) row.get("h_d_id", VoltType.INTEGER));
					statements[1].setInt(5,
							(int) row.get("h_w_id", VoltType.INTEGER));
					statements[1].setTimestamp(6,
							row.getTimestampAsSqlTimestamp("h_date"));
					statements[1].setBigDecimal(
							7,
							row.getDecimalAsBigDecimal("h_amount").setScale(6,
									BigDecimal.ROUND_HALF_DOWN));
					statements[1].setString(8, row.getString("h_data"));
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
			voltdbConn.callProcedure("@AdHoc", "DELETE FROM history" + volumnId
					+ " WHERE tenant_id = " + tenantId);
		} catch (IOException | ProcCallException | SQLException e) {
			e.printStackTrace();
		}
		// System.out.println("\n history: "+tenantId+" truncated...");
		// ******************************************************************************//
		try {
			conn.close();
			voltdbConn.close();
		} catch (SQLException | InterruptedException e) {
			e.printStackTrace();
		}

	}

}

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

public class DistrictRetriver {
	public String url, username, password;
	public String voltdbServer;
	public int tenantId;
	public int volumnId;

	public Connection conn = null;
	public Statement stmt = null;
	Client voltdbConn = null;
	PreparedStatement[] statements;

	public DistrictRetriver(int tenantId, int volumnId, Connection conn,
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
					.prepareStatement("UPDATE district"
							+ tenantId
							+ " SET d_id = ?, d_w_id = ?, d_name = ?, d_street_1 = ?, d_street_2 = ?, d_city = ?, d_state = ?, d_zip = ?, d_tax = ?, d_ytd = ?, d_next_o_id = ? WHERE d_w_id = ? AND d_id = ?");
			statements[1] = conn.prepareStatement("INSERT INTO district"
					+ tenantId + " VALUES (?,?,?,?,?,?,?,?,?,?,?)");
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
			response = voltdbConn.callProcedure("SelectDistrict_" + volumnId,
					tenantId, 0, 1);
			if (response.getStatus() == ClientResponse.SUCCESS
					&& response.getResults()[0].getRowCount() != 0) {
				result = response.getResults()[0];
				for (int i = 0; i < result.getRowCount(); i++) {
					VoltTableRow row = result.fetchRow(i);
					statements[0].setInt(1,
							(int) row.get("d_id", VoltType.INTEGER));
					statements[0].setInt(2,
							(int) row.get("d_w_id", VoltType.INTEGER));
					statements[0].setString(3, row.getString("d_name"));
					statements[0].setString(4, row.getString("d_street_1"));
					statements[0].setString(5, row.getString("d_street_2"));
					statements[0].setString(6, row.getString("d_city"));
					statements[0].setString(7, row.getString("d_state"));
					statements[0].setString(8, row.getString("d_zip"));
					statements[0].setBigDecimal(
							9,
							row.getDecimalAsBigDecimal("d_tax").setScale(4,
									BigDecimal.ROUND_HALF_DOWN));
					statements[0].setBigDecimal(
							10,
							row.getDecimalAsBigDecimal("d_ytd").setScale(12,
									BigDecimal.ROUND_HALF_DOWN));
					statements[0].setInt(11,
							(int) row.get("d_next_o_id", VoltType.INTEGER));
					statements[0].setInt(12,
							(int) row.get("d_w_id", VoltType.INTEGER));
					statements[0].setInt(13,
							(int) row.get("d_id", VoltType.INTEGER));
					statements[0].addBatch();
				}
				if (result.getRowCount() > 0) {
					statements[0].executeBatch();
					conn.commit();
				}
			}

			response = voltdbConn.callProcedure("SelectDistrict_" + volumnId,
					tenantId, 1, 0);
			if (response.getStatus() == ClientResponse.SUCCESS
					&& response.getResults()[0].getRowCount() != 0) {
				result = response.getResults()[0];
				for (int i = 0; i < result.getRowCount(); i++) {
					VoltTableRow row = result.fetchRow(i);
					statements[1].setInt(1,
							(int) row.get("d_id", VoltType.INTEGER));
					statements[1].setInt(2,
							(int) row.get("d_w_id", VoltType.INTEGER));
					statements[1].setString(3, row.getString("d_name"));
					statements[1].setString(4, row.getString("d_street_1"));
					statements[1].setString(5, row.getString("d_street_2"));
					statements[1].setString(6, row.getString("d_city"));
					statements[1].setString(7, row.getString("d_state"));
					statements[1].setString(8, row.getString("d_zip"));
					statements[1].setBigDecimal(
							9,
							row.getDecimalAsBigDecimal("d_tax").setScale(4,
									BigDecimal.ROUND_HALF_DOWN));
					statements[1].setBigDecimal(
							10,
							row.getDecimalAsBigDecimal("d_ytd").setScale(12,
									BigDecimal.ROUND_HALF_DOWN));
					statements[1].setInt(11,
							(int) row.get("d_next_o_id", VoltType.INTEGER));
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

			response = voltdbConn.callProcedure("SelectDistrict_" + volumnId,
					tenantId, 1, 1);
			if (response.getStatus() == ClientResponse.SUCCESS
					&& response.getResults()[0].getRowCount() != 0) {
				result = response.getResults()[0];
				for (int i = 0; i < result.getRowCount(); i++) {
					VoltTableRow row = result.fetchRow(i);
					statements[1].setInt(1,
							(int) row.get("d_id", VoltType.INTEGER));
					statements[1].setInt(2,
							(int) row.get("d_w_id", VoltType.INTEGER));
					statements[1].setString(3, row.getString("d_name"));
					statements[1].setString(4, row.getString("d_street_1"));
					statements[1].setString(5, row.getString("d_street_2"));
					statements[1].setString(6, row.getString("d_city"));
					statements[1].setString(7, row.getString("d_state"));
					statements[1].setString(8, row.getString("d_zip"));
					statements[1].setBigDecimal(
							9,
							row.getDecimalAsBigDecimal("d_tax").setScale(4,
									BigDecimal.ROUND_HALF_DOWN));
					statements[1].setBigDecimal(
							10,
							row.getDecimalAsBigDecimal("d_ytd").setScale(12,
									BigDecimal.ROUND_HALF_DOWN));
					statements[1].setInt(11,
							(int) row.get("d_next_o_id", VoltType.INTEGER));
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
			voltdbConn.callProcedure("@AdHoc", "DELETE FROM district"
					+ volumnId + " WHERE tenant_id = " + tenantId);
		} catch (IOException | ProcCallException | SQLException e) {
			e.printStackTrace();
		}
		// System.out.println("\n district: "+tenantId+" truncated...");
		// ******************************************************************************//
		try {
			conn.close();
			voltdbConn.close();
		} catch (SQLException | InterruptedException e) {
			e.printStackTrace();
		}

	}

}

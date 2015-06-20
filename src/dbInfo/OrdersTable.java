package dbInfo;

import java.util.Random;
import newhybrid.HException;
import client.HTenantClient;
import utillity.ValueGenerator;
import config.Constants;

public class OrdersTable extends Table {
	public OrdersTable(HTenantClient htc) throws HException {
		super(htc);
		mName = "orders";
		mColumnNames = new String[] { "o_id", "o_d_id", " o_w_id", "o_c_id",
				"o_entry_d", "o_carrier_id", "o_ol_cnt", "o_all_local" };

		mPrimaryKeyColumn = new int[] { 2, 1, 0 };
		mColumnNamesInMysql = mColumnNames.clone();
		mPrimaryKeyColumnInMysql = mPrimaryKeyColumn.clone();
		int n = mColumnNames.length;
		mColumnNamesInVoltdb = new String[n + 3];
		for (int i = 0; i < n; i++)
			mColumnNamesInVoltdb[i] = mColumnNames[i];
		mColumnNamesInVoltdb[n] = "tenant_id";
		mColumnNamesInVoltdb[n + 1] = "is_insert";
		mColumnNamesInVoltdb[n + 2] = "is_update";
		mPrimaryKeyColumnInVoltdb = new int[] { 2, 1, 0, n };
	}

	@Override
	public void generateAllColumns() throws HException {
		if (mColumnValues == null)
			mColumnValues = new String[mColumnNames.length];
		Random ran = new Random(System.currentTimeMillis());
		int dataSizeKind = HTC.getDataSizeKind();
		mColumnValues[0] = String.valueOf(ValueGenerator.RandomNumber(1,
				Constants.ORD_PER_DIST[dataSizeKind]));
		mColumnValues[1] = String.valueOf(ValueGenerator.RandomNumber(1,
				Constants.DIST_PER_WARE[dataSizeKind]));
		mColumnValues[2] = String.valueOf(ValueGenerator.RandomNumber(
				Constants.min_ware, Constants.max_ware));
		mColumnValues[3] = String.valueOf(ValueGenerator.RandomNumber(1,
				Constants.ORD_PER_DIST[dataSizeKind]));
		mColumnValues[4] = String.valueOf(ValueGenerator.getTimeStamp());
		mColumnValues[5] = String.valueOf(ValueGenerator.RandomNumber(1, 10));
		mColumnValues[6] = String.valueOf(ValueGenerator.RandomNumber(5, 15));
		mColumnValues[7] = String.valueOf(ran.nextInt(100) == 1 ? 0 : 1);

	}

}

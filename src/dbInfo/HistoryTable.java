package dbInfo;

import client.HTenantClient;
import utillity.ValueGenerator;
import config.Constants;

public class HistoryTable extends Table {
	public HistoryTable(HTenantClient htc) {
		super(htc);
		mName = "history";
		mColumnNames = new String[] { "h_c_id", "h_c_d_id", "h_c_w_id",
				"h_d_id", "h_w_id", "h_date", "h_amount", "h_data" };

		mPrimaryKeyColumn = new int[] { 0, 1, 2 };
		mColumnNamesInMysql = mColumnNames.clone();
		mPrimaryKeyColumnInMysql = mPrimaryKeyColumn.clone();
		int n = mColumnNames.length;
		mColumnNamesInVoltdb = new String[n + 3];
		for (int i = 0; i < n; i++)
			mColumnNamesInVoltdb[i] = mColumnNames[i];
		mColumnNamesInVoltdb[n] = "tenant_id";
		mColumnNamesInVoltdb[n + 1] = "is_insert";
		mColumnNamesInVoltdb[n + 2] = "is_update";
		mPrimaryKeyColumnInVoltdb = new int[] { 0, 1, 2, n };
	}

	@Override
	public void generateAllColumns() {
		if (mColumnValues == null)
			mColumnValues = new String[mColumnNames.length];
		int dataSizeKind = HTC.getDataSizeKind();
		mColumnValues[0] = String.valueOf(ValueGenerator.RandomNumber(1,
				Constants.CUST_PER_DIST[dataSizeKind]));
		mColumnValues[1] = String.valueOf(ValueGenerator.RandomNumber(1,
				Constants.DIST_PER_WARE[dataSizeKind]));
		mColumnValues[2] = String.valueOf(ValueGenerator.RandomNumber(
				Constants.min_ware, Constants.max_ware));
		mColumnValues[3] = mColumnValues[1];
		mColumnValues[4] = mColumnValues[2];
		mColumnValues[5] = ValueGenerator.getTimeStamp();
		mColumnValues[6] = String.valueOf(10.0);
		mColumnValues[7] = ValueGenerator.MakeAlphaString(12, 24);
	}

}

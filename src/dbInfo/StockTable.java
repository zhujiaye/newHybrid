package dbInfo;

import newhybrid.HException;
import client.HTenantClient;
import utillity.ValueGenerator;
import config.Constants;

public class StockTable extends Table {
	public StockTable(HTenantClient htc) throws HException {
		super(htc);
		mName = "stock";
		mColumnNames = new String[] { "s_i_id", "s_w_id", " s_quantity",
				"s_dist_01", "s_dist_02", "s_dist_03", "s_dist_04",
				" s_dist_05", "s_dist_06", " s_dist_07", "s_dist_08",
				"s_dist_09", "s_dist_10", "s_ytd", "s_order_cnt",
				"s_remote_cnt", "s_data" };
		mPrimaryKeyColumn = new int[] { 1, 0 };
		mColumnNamesInMysql = mColumnNames.clone();
		mPrimaryKeyColumnInMysql = mPrimaryKeyColumn.clone();
		int n = mColumnNames.length;
		mColumnNamesInVoltdb = new String[n + 3];
		for (int i = 0; i < n; i++)
			mColumnNamesInVoltdb[i] = mColumnNames[i];
		mColumnNamesInVoltdb[n] = "tenant_id";
		mColumnNamesInVoltdb[n + 1] = "is_insert";
		mColumnNamesInVoltdb[n + 2] = "is_update";
		mPrimaryKeyColumnInVoltdb = new int[] { 1, 0, n };
	}

	@Override
	public void generateAllColumns() throws HException {
		if (mColumnValues == null)
			mColumnValues = new String[mColumnNames.length];
		int dataSizeKind = HTC.getDataSizeKind();
		mColumnValues[0] = String.valueOf(ValueGenerator.RandomNumber(1,
				Constants.MAXITEMS[dataSizeKind]));
		mColumnValues[1] = String.valueOf(ValueGenerator.RandomNumber(
				Constants.min_ware, Constants.max_ware));
		mColumnValues[2] = String.valueOf(ValueGenerator.RandomNumber(10, 100));
		mColumnValues[3] = ValueGenerator.MakeAlphaString(24, 24);
		mColumnValues[4] = ValueGenerator.MakeAlphaString(24, 24);
		mColumnValues[5] = ValueGenerator.MakeAlphaString(24, 24);
		mColumnValues[6] = ValueGenerator.MakeAlphaString(24, 24);
		mColumnValues[7] = ValueGenerator.MakeAlphaString(24, 24);
		mColumnValues[8] = ValueGenerator.MakeAlphaString(24, 24);
		mColumnValues[9] = ValueGenerator.MakeAlphaString(24, 24);
		mColumnValues[10] = ValueGenerator.MakeAlphaString(24, 24);
		mColumnValues[11] = ValueGenerator.MakeAlphaString(24, 24);
		mColumnValues[12] = ValueGenerator.MakeAlphaString(24, 24);
		mColumnValues[13] = String.valueOf(0);
		mColumnValues[14] = String.valueOf(0);
		mColumnValues[15] = String.valueOf(0);
		mColumnValues[16] = ValueGenerator.MakeAlphaString(26, 50);

	}

}

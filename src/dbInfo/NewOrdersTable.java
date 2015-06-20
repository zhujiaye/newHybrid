package dbInfo;

import newhybrid.HException;
import client.HTenantClient;
import utillity.ValueGenerator;
import config.Constants;

public class NewOrdersTable extends Table {

	public NewOrdersTable(HTenantClient htc) throws HException {
		super(htc);
		mName = "new_orders";
		mColumnNames = new String[] { "no_o_id", "no_d_id", "no_w_id" };
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
		int dataSizeKind = HTC.getDataSizeKind();
		mColumnValues[0] = String.valueOf(ValueGenerator.RandomNumber(1,
				Constants.ORD_PER_DIST[dataSizeKind]));
		mColumnValues[1] = String.valueOf(ValueGenerator.RandomNumber(1,
				Constants.DIST_PER_WARE[dataSizeKind]));
		mColumnValues[2] = String.valueOf(ValueGenerator.RandomNumber(
				Constants.min_ware, Constants.max_ware));

	}
}

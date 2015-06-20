package dbInfo;

import newhybrid.HException;
import client.HTenantClient;
import utillity.ValueGenerator;
import config.Constants;

public class WarehouseTable extends Table {
	public WarehouseTable(HTenantClient htc) throws HException {
		super(htc);
		mName = "warehouse";
		mColumnNames = new String[] { "w_id", "w_name", "w_street_1",
				"w_street_2", "w_city", "w_state", "w_zip", "w_tax", "w_ytd" };

		mPrimaryKeyColumn = new int[] { 0 };
		mColumnNamesInMysql = mColumnNames.clone();
		mPrimaryKeyColumnInMysql = mPrimaryKeyColumn.clone();
		int n = mColumnNames.length;
		mColumnNamesInVoltdb = new String[n + 3];
		for (int i = 0; i < n; i++)
			mColumnNamesInVoltdb[i] = mColumnNames[i];
		mColumnNamesInVoltdb[n] = "tenant_id";
		mColumnNamesInVoltdb[n + 1] = "is_insert";
		mColumnNamesInVoltdb[n + 2] = "is_update";
		mPrimaryKeyColumnInVoltdb = new int[] { 0, n };
	}

	@Override
	public void generateAllColumns() {
		if (mColumnValues == null)
			mColumnValues = new String[mColumnNames.length];
		mColumnValues[0] = String.valueOf(ValueGenerator.RandomNumber(
				Constants.min_ware, Constants.max_ware));
		mColumnValues[1] = ValueGenerator.MakeAlphaString(6, 10);
		mColumnValues[2] = ValueGenerator.MakeAlphaString(10, 20);
		mColumnValues[3] = ValueGenerator.MakeAlphaString(10, 20);
		mColumnValues[4] = ValueGenerator.MakeAlphaString(10, 20);
		mColumnValues[5] = ValueGenerator.MakeAlphaString(2, 2);
		mColumnValues[6] = ValueGenerator.MakeNumberString(9, 9);
		mColumnValues[7] = String.valueOf((float) (ValueGenerator.RandomNumber(
				10, 20) / 100.0));
		mColumnValues[8] = String.valueOf(3000000.00);

	}
}

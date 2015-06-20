package dbInfo;

import java.util.Random;
import newhybrid.HException;
import client.HTenantClient;
import utillity.ValueGenerator;
import config.Constants;

public class DistrictTable extends Table {
	public DistrictTable(HTenantClient htc) throws HException {
		super(htc);
		mName = "district";
		mColumnNames = new String[] { "d_id", "d_w_id", "d_name", "d_street_1",
				"d_street_2", "d_city", "d_state", "d_zip", "d_tax", "d_ytd",
				"d_next_o_id" };
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
		Random ran = new Random(System.currentTimeMillis());
		int dataSizeKind = HTC.getDataSizeKind();
		mColumnValues[0] = String.valueOf(ValueGenerator.RandomNumber(1,
				Constants.DIST_PER_WARE[dataSizeKind]));
		mColumnValues[1] = String.valueOf(ValueGenerator.RandomNumber(
				Constants.min_ware, Constants.max_ware));
		mColumnValues[2] = ValueGenerator.MakeAlphaString(6, 10);
		mColumnValues[3] = ValueGenerator.MakeAlphaString(10, 20);
		mColumnValues[4] = ValueGenerator.MakeAlphaString(10, 20);
		mColumnValues[5] = ValueGenerator.MakeAlphaString(10, 20);
		mColumnValues[6] = ValueGenerator.MakeAlphaString(2, 2);
		mColumnValues[7] = ValueGenerator.MakeNumberString(9, 9);
		mColumnValues[8] = String.valueOf((float) (ran.nextInt(20) / 100.0));
		mColumnValues[9] = String.valueOf(30000.0);
		mColumnValues[10] = String.valueOf(3001);
	}
}

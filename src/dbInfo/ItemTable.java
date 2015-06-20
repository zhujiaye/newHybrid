package dbInfo;

import java.util.Random;
import newhybrid.HException;
import client.HTenantClient;
import utillity.ValueGenerator;
import config.Constants;

public class ItemTable extends Table {

	public ItemTable(HTenantClient htc) throws HException {
		super(htc);
		mName = "item";
		mColumnNames = new String[] { "i_id", "i_im_id", "i_name", " i_price",
				"i_data" };
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
	public void generateAllColumns() throws HException {
		if (mColumnValues == null)
			mColumnValues = new String[mColumnNames.length];
		Random ran = new Random(System.currentTimeMillis());
		int dataSizeKind = HTC.getDataSizeKind();
		mColumnValues[0] = String.valueOf(ValueGenerator.RandomNumber(1,
				Constants.MAXITEMS[dataSizeKind]));
		mColumnValues[1] = String.valueOf(ran.nextInt(9999) + 1);
		mColumnValues[2] = ValueGenerator.MakeAlphaString(14, 24);
		mColumnValues[3] = String.valueOf((ran.nextInt(9900) + 100) / 100.0);
		mColumnValues[4] = ValueGenerator.MakeAlphaString(26, 50);
	}

}

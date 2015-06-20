package dbInfo;

import newhybrid.HException;
import client.HTenantClient;
import utillity.ValueGenerator;
import config.Constants;

public class OrderLineTable extends Table {
	public OrderLineTable(HTenantClient htc) throws HException {
		super(htc);
		mName = "order_line";
		mColumnNames = new String[] { "ol_o_id", "ol_d_id", "ol_w_id",
				"ol_number", "ol_i_id", "ol_supply_w_id", "ol_delivery_d",
				"ol_quantity", "ol_amount", "ol_dist_info" };
		mPrimaryKeyColumn = new int[] { 2, 1, 0, 3 };
		mColumnNamesInMysql = mColumnNames.clone();
		mPrimaryKeyColumnInMysql = mPrimaryKeyColumn.clone();
		int n = mColumnNames.length;
		mColumnNamesInVoltdb = new String[n + 3];
		for (int i = 0; i < n; i++)
			mColumnNamesInVoltdb[i] = mColumnNames[i];
		mColumnNamesInVoltdb[n] = "tenant_id";
		mColumnNamesInVoltdb[n + 1] = "is_insert";
		mColumnNamesInVoltdb[n + 2] = "is_update";
		mPrimaryKeyColumnInVoltdb = new int[] { 2, 1, 0, 3, n };
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
		mColumnValues[3] = String.valueOf(ValueGenerator.RandomNumber(1, 15));
		mColumnValues[4] = String.valueOf(ValueGenerator.RandomNumber(1,
				Constants.MAXITEMS[dataSizeKind]));
		mColumnValues[5] = String.valueOf(ValueGenerator.RandomNumber(
				Constants.min_ware, Constants.max_ware));
		mColumnValues[6] = ValueGenerator.getTimeStamp();
		mColumnValues[7] = String.valueOf(5);
		mColumnValues[8] = String.valueOf(0.0);
		mColumnValues[9] = ValueGenerator.MakeAlphaString(24, 24);
	}

}

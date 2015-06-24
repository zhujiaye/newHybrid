package utillity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import config.Constants;
import newhybrid.TenantWorkload;

public class WorkloadLoader {
	final private String mPath;

	private int mSplits;
	private TenantWorkload[] mTenantWorkloads;

	public WorkloadLoader(String path) {
		mPath = path;
		mTenantWorkloads = new TenantWorkload[Constants.NUMBER_OF_TENANTS];
	}

	/**
	 * 
	 * @return false if the workload file name is invalid
	 */
	public boolean load() {
		File file = new File(mPath);
		if (!file.exists() || !file.canRead())
			return false;
		Scanner in = null;
		try {
			in = new Scanner(file);
		} catch (FileNotFoundException e) {
			return false;
		}
		int intervals, splits;
		intervals = in.nextInt();
		mSplits = splits = intervals * 5;
		in.nextDouble();
		in.nextDouble();
		int[] SLO, DS, WH;
		SLO = new int[Constants.NUMBER_OF_TENANTS];
		DS = new int[Constants.NUMBER_OF_TENANTS];
		WH = new int[Constants.NUMBER_OF_TENANTS];
		for (int i = 0; i < Constants.NUMBER_OF_TENANTS; i++) {
			SLO[i] = in.nextInt();
		}
		for (int i = 0; i < Constants.NUMBER_OF_TENANTS; i++) {
			DS[i] = in.nextInt();
		}
		for (int i = 0; i < Constants.NUMBER_OF_TENANTS; i++) {
			WH[i] = 20;
		}
		for (int i = 0; i < Constants.NUMBER_OF_TENANTS; i++) {
			mTenantWorkloads[i] = new TenantWorkload(i + 1, SLO[i], DS[i],
					WH[i], splits);
		}
		for (int i = 0; i < intervals; i++) {
			int numberOfActive;
			numberOfActive = in.nextInt();
			for (int j = 0; j < numberOfActive; j++) {
				int id = in.nextInt();
				for (int k = 0; k < 5; k++)
					mTenantWorkloads[id - 1].setActiveAtSplit(i * 5 + k, true);
			}
		}
		for (int i = 0; i < splits; i++) {
			int split = in.nextInt();
			for (int j = 0; j < Constants.NUMBER_OF_TENANTS; j++) {
				int workload = in.nextInt();
				mTenantWorkloads[j].setWorkloadAtSplit(split - 1, workload);
			}
			in.nextInt();
		}
		in.close();
		return true;
	}

	public int getNumberOfSplits() {
		return mSplits;
	}

	public TenantWorkload getWorkloadForTenant(int id) {
		return mTenantWorkloads[id - 1];
	}

	/**
	 * This is just for test
	 */
	public void print() {
		int[] totWorkload = new int[mSplits];
		for (int i = 0; i < mSplits; i++) {
			totWorkload[i] = 0;
			for (int j = 0; j < Constants.NUMBER_OF_TENANTS; j++)
				totWorkload[i] += mTenantWorkloads[j]
						.getActualWorkloadAtSplit(i);
		}
		for (int i = 0; i < mSplits; i++) {
			System.out.format("%30d", totWorkload[i]);
		}
		System.out.println();
		for (int j = 0; j < Constants.NUMBER_OF_TENANTS; j++) {
			TenantWorkload tenantWorkload = mTenantWorkloads[j];
			for (int i = 0; i < mSplits; i++) {
				if (tenantWorkload.isActiveAtSplit(i)) {
					System.out.format("%30s", String.format(
							"%d(ID:%d,SLO:%d,DataSize:%d)",
							tenantWorkload.getActualWorkloadAtSplit(i),
							tenantWorkload.getID(), tenantWorkload.getSLO(),
							tenantWorkload.getDataSize()));
					// System.out.format("%7s", String.format(
					// "%d",
					// tenantWorkload.getActualWorkloadAtSplit(i)));
				} else {
					System.out.format("%30s", "");
				}
			}
			System.out.println();
		}
	}
}

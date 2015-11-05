package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.log4j.Logger;

import config.Constants;

public class WorkloadLoader {
	final static private Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);
	final private String mPath;

	private int mSplits;
	private TenantWorkload[] mTenantWorkloads;
	private HashMap<Integer, Integer> mPosition;
	private int mNumberOfTenants;

	public WorkloadLoader(String path) {
		mPath = path;
	}

	/**
	 * 
	 * @return false if the workload file not exist or in wrong format
	 */
	public boolean load() {
		File file = new File(mPath);
		LOG.info("Reading workload file " + file.getAbsolutePath());
		if (!file.exists() || !file.canRead())
			return false;
		Scanner in = null;
		try {
			in = new Scanner(file);
		} catch (FileNotFoundException e) {
			return false;
		}
		mNumberOfTenants = in.nextInt();
		int intervals, splits;
		int[] idList = new int[mNumberOfTenants];
		mTenantWorkloads = new TenantWorkload[mNumberOfTenants];
		mPosition = new HashMap<>();
		intervals = in.nextInt();
		mSplits = splits = intervals * 5;
		in.nextDouble();
		in.nextDouble();
		for (int i = 0; i < mNumberOfTenants; i++) {
			idList[i] = in.nextInt();
			mPosition.put(idList[i], i);
		}
		int[] SLO, DS, WH;
		SLO = new int[mNumberOfTenants];
		DS = new int[mNumberOfTenants];
		WH = new int[mNumberOfTenants];
		for (int i = 0; i < mNumberOfTenants; i++) {
			SLO[i] = in.nextInt();
		}
		for (int i = 0; i < mNumberOfTenants; i++) {
			DS[i] = in.nextInt();
		}
		for (int i = 0; i < mNumberOfTenants; i++) {
			WH[i] = 50;
		}
		for (int i = 0; i < mNumberOfTenants; i++) {
			mTenantWorkloads[i] = new TenantWorkload(idList[i], SLO[i], DS[i],
					WH[i], splits);
		}
		for (int i = 0; i < intervals; i++) {
			int numberOfActive;
			numberOfActive = in.nextInt();
			for (int j = 0; j < numberOfActive; j++) {
				int id = in.nextInt();
				for (int k = 0; k < 5; k++)
					mTenantWorkloads[mPosition.get(id)].setActiveAtSplit(i * 5
							+ k, true);
			}
		}
		for (int i = 0; i < splits; i++) {
			int split = in.nextInt();
			if (split != i + 1) {
				LOG.error("Wrong format!");
				in.close();
				return false;
			}
			for (int j = 0; j < mNumberOfTenants; j++) {
				int actualWorkload;
				int estimatedWorkload;
				actualWorkload = in.nextInt();
				estimatedWorkload = actualWorkload;
				mTenantWorkloads[j].setWorkloadAtSplit(split - 1,
						actualWorkload, estimatedWorkload);
			}
			// total actual workloads
			in.nextInt();
			// total estimated workloads
			// in.nextInt();
		}
		in.close();
		return true;
	}

	public int getNumberOfSplits() {
		return mSplits;
	}

	/**
	 * 
	 * @param id
	 * @return null if the tenant doesn't have a workload information
	 */
	public TenantWorkload getWorkloadForTenant(int id) {
		if (!mPosition.containsKey(id))
			return null;
		return mTenantWorkloads[mPosition.get(id)];
	}

	public void generateMigratingFlow() {
		
	}

}

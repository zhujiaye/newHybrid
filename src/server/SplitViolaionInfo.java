package server;

import java.util.ArrayList;
import java.util.List;

public class SplitViolaionInfo {

	private int mSplits;
	private List<Integer> mSplitTenantViolation;
	private List<Integer> mSplitQueryViolation;

	public SplitViolaionInfo(int splits) {
		mSplits = splits;
		mSplitTenantViolation = new ArrayList<>();
		mSplitQueryViolation = new ArrayList<>();
		for (int i = 0; i < splits; i++) {
			mSplitTenantViolation.add(0);
			mSplitQueryViolation.add(0);
		}
	}

	public void addSplitViolation(int splitID, int numberOfTenants,
			int numberOfQueries) {
		if (splitID >= mSplits)
			return;
		int old_numberOfTenants = mSplitTenantViolation.get(splitID);
		int old_numberOfQueries = mSplitQueryViolation.get(splitID);
		old_numberOfTenants += numberOfTenants;
		old_numberOfQueries += numberOfQueries;
		mSplitTenantViolation.set(splitID, old_numberOfTenants);
		mSplitQueryViolation.set(splitID, old_numberOfQueries);
	}

	public boolean isViolated() {
		for (int i = 0; i < mSplits; i++) {
			int numberOfTenants = mSplitTenantViolation.get(i);
			int numberOfQueries = mSplitQueryViolation.get(i);
			if (numberOfTenants > 7 && numberOfQueries > 100)
				return true;
		}
		return false;
	}
}

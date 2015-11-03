package test;

public class SplitResult {
	private final int TOT;
	private final int SENT;
	private final int SUCCESS;

	public SplitResult(int tot, int sent, int success) {
		TOT = tot;
		SENT = sent;
		SUCCESS = success;
	}

	public int getTot() {
		return TOT;
	}

	public int getSent() {
		return SENT;
	}

	public int getSuccess() {
		return SUCCESS;
	}
}

package newhybrid;

public class Pair<First, Second> {
	private final First FIRST;
	private final Second SECOND;

	public Pair(First first, Second second) {
		FIRST = first;
		SECOND = second;
	}

	public First first() {
		return FIRST;
	}

	public Second second() {
		return SECOND;
	}
}

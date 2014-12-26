package utillity;

import config.HConfig;

public class HTimer implements Runnable {
	private Object obj;
	private int gap;
	private boolean isClientMonitor = false;

	// public HTimer(Object obj) {
	// this.obj = obj;
	// }

	public HTimer(Object obj, int gap) {
		this.obj = obj;
		this.gap = gap;
	}

	public HTimer(Object obj, int gap, boolean boo) {
		this.obj = obj;
		this.gap = gap;
		this.isClientMonitor = true;
	}

	@Override
	public void run() {
		long startTime = System.currentTimeMillis();
		long lastTime = startTime;
		while (true) {
			if (isClientMonitor) {
				((client.Monitor) obj).printQueryingTenants();
			}
			long tmp = System.currentTimeMillis() - startTime;
			if (tmp > (HConfig.INTERVAL_TIME_IN_MINUTES
					* HConfig.NUMBER_OF_INTERVAL + 1) * 60 * 1000) {
				break;
			}
			long thisTime = System.currentTimeMillis();
			tmp = thisTime - lastTime;
			synchronized (obj) {
				obj.notifyAll();
			}
			lastTime = thisTime;
			// if (tmp > gap * 1000) {
			// synchronized (obj) {
			// obj.notifyAll();
			// }
			// lastTime = thisTime;
			// }
			try {
				Thread.sleep(gap * 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

package newhybrid;

import org.apache.log4j.Logger;

import config.Constants;

public class HeartbeatThread extends Thread {
	final static private Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);
	private final String THREAD_NAME;
	private final HeartbeatExecutor EXECUTOR;
	private final long FIXED_EXECUTION_INTERVAL_TIME;

	private volatile boolean mIsShutdown;

	/**
	 * @param threadName
	 * @param Executor
	 * @param fixedExecutionIntervalTime
	 *            Sleep time between different heartbeat.
	 */
	public HeartbeatThread(String threadName, HeartbeatExecutor Executor,
			long fixedExecutionIntervalTime) {
		THREAD_NAME = threadName;
		EXECUTOR = Executor;
		FIXED_EXECUTION_INTERVAL_TIME = fixedExecutionIntervalTime;
		mIsShutdown = false;
		// setDaemon(true);
	}

	@Override
	public void run() {
		try {
			while (!mIsShutdown) {
				long lastTime = System.nanoTime();
				EXECUTOR.heartbeat();
				long executionTime = System.nanoTime() - lastTime;
				if (executionTime > FIXED_EXECUTION_INTERVAL_TIME) {
					LOG.debug(THREAD_NAME + " last execution took "
							+ executionTime + " ns. Longer than "
							+ " the FIXED_EXECUTION_INTERVAL_TIME "
							+ FIXED_EXECUTION_INTERVAL_TIME);
				} else {
					Thread.sleep((FIXED_EXECUTION_INTERVAL_TIME - executionTime) / 1000000);
				}
			}
		} catch (InterruptedException e) {
			if (!mIsShutdown) {
				LOG.error("Heartbeat Thread was interrupted ungracefully, shutting down..."
						+ e.getMessage());
			}
		} catch (Exception e) {
			LOG.error("Uncaught exception in heartbeat executor, Heartbeat Thread shutting down:"
					+ e.getMessage());
		}
	}

	public void shutdown() {
		mIsShutdown = true;
		this.interrupt();
	}
}

package client;

import config.HConfig;

public class Main {
	public static void main(String[] args) {
		// Thread[] threads = new Thread[HConfig.TOTTENANTS];
		// HTenant[] htenants = new HTenant[HConfig.TOTTENANTS];
		// for (int i = 0; i < HConfig.TOTTENANTS; i++) {
		// htenants[i]=new HTenant(i+1);
		// threads[i] = new Thread(htenants[i]);
		// }
		// for (int i = 0; i < HConfig.TOTTENANTS; i++)
		// threads[i].start();
		HConfig.load();
		HConfig.print();
		Monitor monitor = null;
		if (args.length >= 2) {
			monitor = new Monitor(Integer.valueOf(args[0]),
					Integer.valueOf(args[1]));
		} else
			monitor = new Monitor(1, HConfig.TOTTENANTS);
		new Thread(monitor).start();
	}
}

package test;

import config.ClientConf;
import config.ServerConf;
import config.WorkerConf;

public class Test {

	public static void main(String[] args) {
		if (args[0].equals("server"))
			ServerConf.getConf().print();
		else if (args[0].equals("worker"))
			WorkerConf.getConf().print();
		else
			ClientConf.getConf().print();
	}
}

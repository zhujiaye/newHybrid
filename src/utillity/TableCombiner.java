package utillity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import config.HConfig;

public class TableCombiner implements Runnable {
	private String table_name;
	private ArrayList<Integer> tenantids;

	public TableCombiner(String table_name, ArrayList<Integer> tenantids) {
		this.table_name = table_name;
		this.tenantids = tenantids;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		long t1, t2;
		t1 = System.currentTimeMillis();
		File writefile = null;
		File readfile = null;
		FileOutputStream writer = null;
		FileInputStream reader = null;
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		writefile = new File(HConfig.CSVPATH + "/" + table_name + ".csv");

		try {
			byte[] buffer = new byte[1024];
			// ArrayList<Byte> buffer=new ArrayList<>();
			writer = new FileOutputStream(writefile);
			out = new BufferedOutputStream(writer);
			for (int i = 0; i < tenantids.size(); i++) {
				int id = tenantids.get(i);
				reader = new FileInputStream(new File(HConfig.CSVPATH + "/"
						+ table_name + id + ".csv"));
				in = new BufferedInputStream(reader);
				int c;
				while ((c = in.read(buffer)) != -1) {
					out.write(buffer, 0, c);
				}
				// while ((c = in.read()) != -1) {
				// buffer.add((byte)c);
				// // writer.write(buffer, 0, c);
				// //out.write(buffer, 0, c);
				// }
				// writer.flush();
				// reader.close();
			}
			// Object[] tmp = buffer.toArray();
			// for (int i = 0; i < tmp.length; i++)
			// out.write((Byte) tmp[i]);
			// writer.flush();
			// writer.close();
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		t2 = System.currentTimeMillis();
		System.out.format("Finish combine for table %s in %f minutes%n",
				table_name, ((double) (t2 - t1)) / (60.0 * 1000.0));
	}
}

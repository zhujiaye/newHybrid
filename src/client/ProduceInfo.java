package client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import config.HConfig;

public class ProduceInfo {
	public static void main(String[] args) {
		int cnt20, cnt60, cnt200, wcnt40, wcnt60;
		cnt20 = cnt60 = cnt200 = 0;
		wcnt40 = wcnt60 = 0;
		PrintWriter writer = null;

		try {
			writer = new PrintWriter(new File(HConfig.INFO_FILE));
			for (int i = 0; i < HConfig.TOTTENANTS; i++) {
				HTenant tmp = new HTenant(i + 1);
				int slo, wh, ds;
				slo = tmp.getSLOinfo();
				wh = tmp.getWriteHeavyinfo();
				ds = tmp.getDataSizeinfo();
				if (slo == 20)
					cnt20++;
				if (slo == 60)
					cnt60++;
				if (slo == 200)
					cnt200++;
				if (wh == 40)
					wcnt40++;
				if (wh == 60)
					wcnt60++;
				writer.format("%d %d %d %d%n", i + 1, slo, ds, wh);
				writer.flush();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (writer != null)
				writer.close();
		}
		System.out.format("20(%d) 60(%d) 200(%d)%n", cnt20, cnt60, cnt200);
		System.out.format("40(%d) 60(%d)%n", wcnt40, wcnt60);
	}
}

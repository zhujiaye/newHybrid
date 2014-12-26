package retrivers;

public class RetriveThread extends Thread {
	private long rowNumber = 0;
	public long getRowNumber(){
		return rowNumber;
	}
	
	public void run(){
		Retriver next;
		while((next = Retriver.next()) != null){
			Voltdb2Mysql m = new Voltdb2Mysql(next.tenantId, next.volumnId);
			m.start();
			try {
				m.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			rowNumber += m.getRowNumber();
		}
	}
}

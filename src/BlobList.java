import java.util.ArrayList;


public class BlobList extends ArrayList<Blob> {
	public Blob searchBlob(int x, int y) {
		Blob ret = null;
		Blob tmpBlob;
		for (int i=0; i<this.size(); i++) {
			tmpBlob = this.get(i);
			if (tmpBlob.containsKey(makeKey(x, y))) {
				ret = tmpBlob;
				break;
			}
		}
		
		return ret;
	}

	public void deleteBlob(int x, int y) {
		Blob tmpBlob;
		for (int i=0; i<this.size(); i++) {
			tmpBlob = this.get(i);
			if (tmpBlob.containsKey(makeKey(x, y))) {
				this.remove(i);
				break;
			}
		}
	}
	
	
	private String makeKey(int x, int y) {
		return String.valueOf(x) + ","+String.valueOf(y);
	}	
}

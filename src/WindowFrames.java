import java.util.ArrayList;

public class WindowFrames extends ArrayList<int[]> {
	public int getWidth(int idx) {
		return this.get(idx).length;
	} 
	
	public int getHeight() {
		return this.size();
	}
	
	public int getData(int x, int y) {
		return this.get(y)[x];
	}
	
	
	/*
	 * Tools for debugging below here 	
	 */
	public void printWindow() {
		for (int y=0; y<this.getHeight(); y++) {
			System.out.print("[ ");
			for (int x=0; x<this.getWidth(y); x++) {
				System.out.print(this.getData(x, y) + " ");
			}
			System.out.println("]");
		}
	}
	
	public void printFrame(int[] frame) {
		for (int i=0; i<frame.length; i++) {
			System.out.print(frame[i] + " ");
		}
	}

	
}

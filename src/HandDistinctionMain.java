
public class HandDistinctionMain {
	
	public static void main(String args[]) {
		new HandDistinctionMain();
		
		String fileName = "./csv/right3_x.csv";
		CSVHandler csvHandler = new CSVHandler(fileName);
		WindowFrames depthWindowFrames = csvHandler.csvToWindowFrames();
		
		// split into first frame and sparse frames
		int[] firstDepthFrame = depthWindowFrames.get(0);
		depthWindowFrames.remove(0);
		
		HandDistinction distinction = new HandDistinction(depthWindowFrames, firstDepthFrame);
		int result = distinction.distinct();
		
		if (result > 0) {
			System.out.println("Right");
		} else {
			System.out.println("Left");
		}
	}
}


public class HandDistinctionMain {
	
	public static void main(String args[]) {
		new HandDistinctionMain();
		
		// left right detection
		
		String fileNameX = "./csv/down11_x.csv";
		CSVHandler csvHandlerX = new CSVHandler(fileNameX);
		WindowFrames depthWindowFramesX = csvHandlerX.csvToWindowFrames();
		
		// split into first frame and sparse frames
		int[] firstDepthFrameX = depthWindowFramesX.get(0);
		depthWindowFramesX.remove(0);
		
		HandDistinction distinctionX = new HandDistinction(depthWindowFramesX, firstDepthFrameX);
		int resultX = distinctionX.distinct();
		
		if (resultX == 1) {
			System.out.println("X:1");
		} else if (resultX == -1){
			System.out.println("X:-1");
		} else {
			System.out.println("X:0");
		}
		
		String fileNameY = "./csv/down11_y.csv";
		CSVHandler csvHandlerY = new CSVHandler(fileNameY);
		WindowFrames depthWindowFramesY = csvHandlerY.csvToWindowFrames();
		
		// split into first frame and sparse frames
		int[] firstDepthFrameY = depthWindowFramesY.get(0);
		depthWindowFramesY.remove(0);
		
		HandDistinction distinctionY = new HandDistinction(depthWindowFramesY, firstDepthFrameY);
		int resultY = distinctionY.distinct();
		
		if (resultY == 1) {
			System.out.println("Y:1");
		} else if (resultY == -1){
			System.out.println("Y:-1");
		} else {
			System.out.println("Y:0");
		}
		
		if (resultX == 0 && resultY == -1) {
			System.out.println("UP");
		} else if (resultX == 0 && resultY == 1) {
			System.out.println("DOWN");
		} else if (resultX == -1 && resultY == 0) {
			System.out.println("LEFT");
		} else if (resultX == 1 && resultY == 0) {
			System.out.println("RIGHT");
		} else {
			System.out.println("NO HAND DETECTION");
		}
	}
}

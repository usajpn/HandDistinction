
public class HandDistinctionMain {
	
	public static void main(String args[]) {
		new HandDistinctionMain();
		
		int countRight = 0;
		int countLeft = 0;
		int countUp = 0;
		int countDown = 0;
		double d;
		
		for (int z=1; z<=30; z++) {
			// up up detection
			System.out.println("***frame" + z + "***");
			String fileNameX = "./csv2/up" + String.valueOf(z) +"_x.csv";
			CSVHandler csvHandlerX = new CSVHandler(fileNameX);
			WindowFrames depthWindowFramesX = csvHandlerX.csvToWindowFrames();
			
			// split into first frame and sparse frames
			int[] firstDepthFrameX = depthWindowFramesX.get(0);
			depthWindowFramesX.remove(0);
			
			HandDistinction distinctionX = new HandDistinction(depthWindowFramesX, firstDepthFrameX);
			int resultX = distinctionX.distinct();
			
	//		if (resultX == 1) {
	//			System.out.println("X:1");
	//		} else if (resultX == -1){
	//			System.out.println("X:-1");
	//		} else {
	//			System.out.println("X:0");
	//		}
			
			String fileNameY = "./csv2/up" + String.valueOf(z) + "_y.csv";
			CSVHandler csvHandlerY = new CSVHandler(fileNameY);
			WindowFrames depthWindowFramesY = csvHandlerY.csvToWindowFrames();
			
			// split into first frame and sparse frames
			int[] firstDepthFrameY = depthWindowFramesY.get(0);
			depthWindowFramesY.remove(0);
			
			HandDistinction distinctionY = new HandDistinction(depthWindowFramesY, firstDepthFrameY);
			int resultY = distinctionY.distinct();
			
	//		if (resultY == 1) {
	//			System.out.println("Y:1");
	//		} else if (resultY == -1){
	//			System.out.println("Y:-1");
	//		} else {
	//			System.out.println("Y:0");
	//		}
			
			if (resultX == 0 && resultY == -1) {
				System.out.println("UP");
				countUp++;
			} else if (resultX == 0 && resultY == 1) {
				System.out.println("DOWN");
				countDown++;
			} else if (resultX == -1 && resultY == 0) {
				System.out.println("LEFT");
				countLeft++;
			} else if (resultX == 1 && resultY == 0) {
				System.out.println("RIGHT");
				countRight++;
			} else {
				if (resultX == 1 && distinctionY.getTwoThirds()) {
					System.out.println("RIGHT");
					countRight++;
				} else if (resultX == -1 && distinctionY.getTwoThirds()) {
					System.out.println("LEFT");
					countLeft++;
				}
			}
		}	
		System.out.println(countUp);
	}
}

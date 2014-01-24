import java.util.ArrayList;
import java.util.Collections;


public class HandDistinction {
	private WindowFrames depthWindowFrames;
	private int[] firstDepthFrame;
	private static final int threshold = 1000;
	private BlobList groupList;
	private ArrayList<ArrayList<Vector>> groupVectorList = new ArrayList<ArrayList<Vector>>();
	
	public HandDistinction(WindowFrames windowFrames, int[] firstFrame) {
		this.depthWindowFrames = windowFrames;
		this.firstDepthFrame = firstFrame;
	}
	
	public void thresholdize() {
		int depthData;
		for (int y=0; y<this.depthWindowFrames.getHeight(); y++) {
			for (int x=0; x<this.depthWindowFrames.getWidth(y); x++) {
				depthData = this.depthWindowFrames.getData(x, y);
				
				if (threshold < depthData) {
					int[] intAry = this.depthWindowFrames.get(y);
					intAry[x] = 0;
					this.depthWindowFrames.set(y, intAry);	
				}
			}
		}
	}
	
	public void convertRealDepthData() {
		for (int y=0; y<this.depthWindowFrames.getHeight(); y++) {
			int[] replaceFrame = new int[this.depthWindowFrames.get(y).length];
			for (int x=0; x<this.depthWindowFrames.getWidth(y); x++) {
				replaceFrame[x] = this.firstDepthFrame[x] + this.depthWindowFrames.getData(x, y); 
			}
			this.depthWindowFrames.set(y, replaceFrame);
		}
	}
	
	public BlobList groupify() {
		groupList = new BlobList();
		Blob group = new Blob();
		Blob tmpGroup = new Blob();
		
		// first row
		int[] firstFrame = this.depthWindowFrames.get(0);
		boolean hitBefore = false;
		boolean addToListFlag = false;
		int data;
		
		for (int x=0; x<firstFrame.length; x++) {
			data = firstFrame[x];
			if (data != 0) {
				// append to group
				if (hitBefore) {
					group.append(x, 0, data);

				} 
				// create new group and append to group
				else {
					groupList.add(group);
					group = new Blob();
					group.append(x, 0, data);
				}
				hitBefore = true;
			} else {
				hitBefore = false;
			}
			if (x == (firstFrame.length - 1) && group.size() != 0) {
				groupList.add(group);
			}	
		}
		
		// after first row
		int[] frame;
		hitBefore = false;
		for (int y=1; y<this.depthWindowFrames.getHeight(); y++) {
			frame = this.depthWindowFrames.get(y);
			for (int x=0; x<this.depthWindowFrames.getWidth(y); x++) {
				data = frame[x];
				if (data != 0) {
					if (hitBefore) {
						group.append(x, y, data);
						
						//check top pixel
						tmpGroup = groupList.searchBlob(x, y-1);
						if (tmpGroup != null) {
							group.putAll(tmpGroup);
							groupList.deleteBlob(x, y-1);
						}
					} else {
						groupList.add(group);
						group = new Blob();
						group.append(x, y, data);
						
						//check top pixel
						tmpGroup = groupList.searchBlob(x, y-1);
						if (tmpGroup != null) {
							group.putAll(tmpGroup);
							groupList.deleteBlob(x, y-1);
						}	
					}
		
					hitBefore = true;
				} else {
					hitBefore = false;
				}
				
			
			}
			hitBefore = false;
		}
		if (group.size() != 0) {
			groupList.add(group);
		}			
		
		return groupList;
	}
	
	public void beautifyGroup() {
		/* 1: x 2: y 3: data */
		Blob group = new Blob();
		
		for (int i=0; i<this.groupList.size(); i++) {
			group = this.groupList.get(i);
			if (group.size() == 0) {
				continue;
			}
			
			//vector list for one group
			String[] keyStr;
			Vector vector;
			ArrayList<Vector> vectorList = new ArrayList<Vector>();
			for (String key : group.keySet()) {
				vector = new Vector();
				vector.setData(group.get(key));
				keyStr = key.split(",");
				vector.setX(Integer.parseInt(keyStr[0])); // x axis
				vector.setY(Integer.parseInt(keyStr[1])); // y axis
				vectorList.add(vector);
			}
			
			// sort vector list by y (time)
			Collections.sort(vectorList, new TemporalComparator());
			int minTime = vectorList.get(0).getY();
			int maxTime = vectorList.get(vectorList.size()-1).getY();
			
			ArrayList<Vector> minTimeList = new ArrayList<Vector>();
			ArrayList<Vector> maxTimeList = new ArrayList<Vector>();
			
			Vector tmpVector;
			for (int a=0; a<vectorList.size(); a++) {
				tmpVector = vectorList.get(a);
				if (tmpVector.getY() == minTime) {
					minTimeList.add(tmpVector);
				}
				if (tmpVector.getY() == maxTime) {
					maxTimeList.add(tmpVector);
				}
			}
			
			Collections.sort(minTimeList, new XComparator());
			Collections.sort(maxTimeList, new XComparator());
			
			ArrayList<Vector> groupFourVectors = new ArrayList<Vector>();
			// add like in Z
			groupFourVectors.add(minTimeList.get(0));
			groupFourVectors.add(minTimeList.get(minTimeList.size()-1));
			
			groupFourVectors.add(maxTimeList.get(0));
			groupFourVectors.add(maxTimeList.get(maxTimeList.size()-1));			
			
			groupVectorList.add(groupFourVectors);
			
		}
			
	}
	
	public Vector arrangeVector(Vector v1, Vector v2) {
		int x1 = v1.getX();
		int y1 = v1.getY();
		int x2 = v2.getX();
		int y2 = v2.getY();
		v2.setX(x2 - x1);
		v2.setY(y2 - y1);
		
		return v2;
	}
	
	public int calculateLength(Vector v1, Vector v2) {
		int result = 0;
		int x1 = v1.getX();
		int x2 = v2.getX();
		int y1 = v1.getY();
		int y2 = v2.getY();
		
		result = (int) Math.floor(Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)));
		return result;
	}
	
	public int calculateAngle(Vector v1, Vector v2) {
		int deg;
		double s;
		double pie = 3.14;
		
		Vector v = arrangeVector(v1, v2);
		int x = v.getX();
		int y = v.getY();
		
		s=Math.acos(x/Math.sqrt(x*x+y*y)); /*角度θを求める*/
		
		s=(s/pie)*180.0; /* ラジアンを度に変換 */
		
		if (y<0) /* θ＞πの時 */
		      s=360-s;
		
		deg=(int)Math.floor(s);
		
		if ((s-deg)>=0.5) /*小数点を四捨五入*/
		      deg++;
		
	    return deg; /*角度θを返す*/
	}
	
	/* 1: right -1: left */
	public int distinct() {
		int result = 0;
		this.convertRealDepthData();
		this.thresholdize();
		
		// make groups
		this.groupify();
		
		// beautify group
		this.beautifyGroup();
		
		for (int i=0; i<this.groupVectorList.size(); i++) {
			System.out.println("==group" + i + "===");
			ArrayList<Vector> tmpVecList = this.groupVectorList.get(i);
			for (int j=0; j<tmpVecList.size(); j++) {
				Vector tmpVec = tmpVecList.get(j);
				System.out.println("x:" + tmpVec.getX() + ", y: " + tmpVec.getY() + ", d: " + tmpVec.getData());
			}
		}
		
		// calculate vectors for each group
		
		int ang1 = 0, ang2 = 0, len1 = 0, len2 = 0;
		int saveAng = 0, saveLen = 0;
		
		for (int i=0; i<this.groupVectorList.size(); i++) {
			System.out.println("==group" + i + "===");
			ArrayList<Vector> tmpVecList = this.groupVectorList.get(i);
			
			Vector leftTop = tmpVecList.get(0);
			Vector rightTop = tmpVecList.get(1);
			Vector leftBottom = tmpVecList.get(2);
			Vector rightBottom = tmpVecList.get(3);
			
			ang1 = this.calculateAngle(leftTop, rightBottom);
			ang2 = this.calculateAngle(rightTop, leftBottom);
			System.out.println("ang1:" + ang1);
			System.out.println("ang2:" + ang2);
			
			len1 = this.calculateLength(leftTop, rightBottom);
			len2 = this.calculateLength(rightTop, leftBottom);
			System.out.println("len1:" + len1);
			System.out.println("len2:" + len2);		
		}	
		
		if (ang1 < 90) {
			result = 1;
		} else {
			result = -1;
		}
		return result;
	}
}

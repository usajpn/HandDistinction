import java.util.HashMap;

/* e.g: "0,1", 800 */
public class Blob extends HashMap<String, Integer>{
	private boolean hitTop;
	
	public Blob() {
		this.hitTop = false;
	}
	
	public void append(int x, int y, int data) {
		String key = makeKey(x, y);
		this.put(key, data);
	}
	
	public void setHitTop() {
		this.hitTop = true;
	}
	
	public boolean hitTopState() {
		return this.hitTop;
	}
	
	private String makeKey(int x, int y) {
		return String.valueOf(x) + ","+String.valueOf(y);
	}

	
	
}

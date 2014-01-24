import java.util.Comparator;


public class XComparator implements Comparator<Vector> {
	@Override
	public int compare(Vector v1, Vector v2) {
		return v1.getX() < v2.getX() ? -1 : 1; 
	}
}

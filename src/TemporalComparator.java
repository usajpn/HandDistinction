import java.util.Comparator;


public class TemporalComparator implements Comparator<Vector> {
	@Override
	public int compare(Vector v1, Vector v2) {
		return v1.getY() < v2.getY() ? -1 : 1; 
	}
}

package org.khelekore.prtree.nd;

/** An implementation of MBRND that keeps a double array with the max
 *  and min values
 *
 * <p>Please note that you should not normally use this class when PRTree
 * wants a MBR since this will actually use a lot of extra memory.
 */
public class SimpleMBRND implements MBRND {
    private final double values[];

    private SimpleMBRND (int dimensions) {
	values = new double[dimensions * 2];	
    }

    public SimpleMBRND (double... values) {
	this.values = values.clone ();
    }

    public <T> SimpleMBRND (T t, MBRConverterND<T> converter) {
	int dims = converter.getDimensions ();
	values = new double[dims * 2];
	int p = 0;
	for (int i = 0; i < dims; i++) {
	    values[p++] = converter.getMin (i, t);
	    values[p++] = converter.getMax (i, t);
	}
    }

    public int getDimensions () {
	return values.length / 2;
    }

    public double getMin (int axis) {
	return values[axis * 2];
    }

    public double getMax (int axis) {
	return values[axis * 2 + 1];
    }

    public MBRND union (MBRND mbr) {
	int dims = getDimensions ();
	SimpleMBRND n = new SimpleMBRND (dims);
	int p = 0;
	for (int i = 0; i < dims; i++) {
	    n.values[p] = Math.min (values[p], mbr.getMin (i));
	    p++;
	    n.values[p] = Math.max (values[p], mbr.getMax (i));
	    p++;
	}
	return n;
    }

    public boolean intersects (MBRND other) {
	for (int i = 0; i < getDimensions (); i++) {
	    if (other.getMax (i) < getMin (i) || other.getMin (i) > getMax (i))
		return false;
	}
	return true;
    }

    public <T> boolean intersects (T t, MBRConverterND<T> converter) {
	for (int i = 0; i < getDimensions (); i++) {
	    if (converter.getMax (i, t) < getMin (i) ||
		converter.getMin (i, t) > getMax (i))
		return false;
	}
	return true;
    }

    @Override public String toString () {
	return getClass ().getSimpleName () +
	    "{values: " + java.util.Arrays.toString (values) + "}";
    }
}
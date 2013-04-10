package com.siberhus.hswing.table;

/**
 * This class is used to keep track of which rows have been scheduled for
 * loading, so that rows don't get scheduled twice concurrently. The idea is to
 * store Segments in a sorted data structure for fast searching.
 * 
 * The compareTo() method sorts first by base position, then by length.
 */
final class Segment implements Comparable<Segment> {
	
	private int base = 0;
	private int length = 1;
	
	public Segment(int base, int length) {
		this.base = base;
		this.length = length;
	}

	public boolean contains(int pos) {
		return ((base <= pos) && (pos < (base + length)));
	}

	public boolean equals(Object o) {
		return o instanceof Segment && (base == ((Segment) o).base)
				&& (length == ((Segment) o).length);
	}

	public int compareTo(Segment other) {
		// return negative/zero/positive as this object is
		// less-than/equal-to/greater-than other
		int d = base - other.base;

		if (d != 0) {
			return d;
		}

		return length - other.length;
	}
}
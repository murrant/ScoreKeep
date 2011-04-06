package org.homelinux.murray.scorekeep;

import java.util.ArrayList;
import java.util.Collections;

public final class GameTypes {
	static final ArrayList<Type> TYPES;
	public static final Type DEFAULT = newType(0, "Generic", R.layout.add_score_generic, true);

	static {
		//TODO include rules?
		//TODO favorites?
		TYPES = new ArrayList<Type>();
		TYPES.add(DEFAULT);
		
		TYPES.add(newType(1, "500", R.layout.add_score_generic, true));
		TYPES.add(newType(2, "Pfeffer", R.layout.add_score_generic, true));
		
		Collections.sort(TYPES);
	}
	private GameTypes() {}
	
	private static Type newType(int id, String name, int resource, boolean enabled) {
		return (new GameTypes()).new Type(id, name, resource, enabled);
	}
	
	public static Type getComparable(int i) {
		return (new GameTypes()).new Type(i, null, 0, true);
	}
	
	// inner class for game types
	public final class Type implements Comparable<Type> {
		public final String name;
		public final int id;
		public final boolean enabled;
		public final int resource;
		
		public Type(int id, String name, int resource, boolean enabled) {
			this.name = name;
			this.id = id;
			this.enabled = enabled;
			this.resource = resource;
		}
		
		public String toString() {
			return name;
		}

		public int compareTo(Type another) {
			return id - another.id;
		}
	}
}

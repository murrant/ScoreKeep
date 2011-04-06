package org.homelinux.murray.scorekeep.games;

import android.app.Dialog;
import android.view.View;

/**
 * Used to implement games.
 * An xml resource must be designed and set to the variable resource.
 * There are several id's you can assign to buttons to get generic functionality.
 * 
 * @author Tony Murray <murraytony@gmail.com>
 *
 */
public abstract class GameDefinition implements Comparable<GameDefinition>, View.OnClickListener {
	public final String name;
	public final int id;
	public final boolean enabled;
	public final int resource;
	
	public GameDefinition(int id, String name, int resource, boolean enabled) {
		this.name = name;
		this.id = id;
		this.enabled = enabled;
		this.resource = resource;
	}
	
	public String toString() {
		return name;
	}

	public int compareTo(GameDefinition another) {
		return id - another.id;
	}
	
	protected static long parseScore(String mathExp) throws NumberFormatException, ArithmeticException {
		Long score = Long.getLong(mathExp);
		if(mathExp != null && !mathExp.isEmpty() && score == null) {
			score = Math.round(new MathEval().evaluate(mathExp));
		}
		return score;
	}
	
	//TODO the dialog argument seems wrong
	public abstract Long calculateScore(Dialog dialog);
	public abstract String getContext(Dialog dialog);
}

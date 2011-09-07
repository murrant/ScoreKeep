package org.homelinux.murray.scorekeep.games;

import org.homelinux.murray.scorekeep.PlayerData;
import org.homelinux.murray.scorekeep.ScoreData;

import android.app.Dialog;
import android.util.Log;
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
	private static final String DEBUG_TAG = "ScoreKeep:GameDefinition";
	/**
	 * Must be defined and unique for each type.
	 */
	private final int id;
	public final String name;
	public final boolean enabled;
	private final int resource;
	
	public GameDefinition(int id, String name, int resource, boolean enabled) {
		this.name = name;
		this.enabled = enabled;
		this.resource = resource;
		this.id = id;
	}
	
	public String toString() {
		return name;
	}

	public int compareTo(GameDefinition another) {
		return id - another.getGameId();
	}
	
	protected static long parseScore(String mathExp) throws NumberFormatException, ArithmeticException {
		Long score = Long.getLong(mathExp);
		if(mathExp != null && !mathExp.isEmpty() && score == null) {
			score = Math.round(new MathEval().evaluate(mathExp));
		}
		return score;
	}
	
	public int getDialogResource() {
		Log.d(DEBUG_TAG, "Using super class implementation of getDialogResource");
		return resource;
	}
	
	public int getGameId() {
		return id;
	}
	
	//TODO the dialog argument seems wrong
	/**
	 * returns null if it could not validate the dialog, it should also display a toast with the problem
	 */
	public abstract ScoreData getScore(Dialog dialog, PlayerData player);
}

package org.homelinux.murray.scorekeep.games;

import java.util.ArrayList;
import java.util.Collections;

import org.homelinux.murray.scorekeep.games.definitions.*;

import android.app.Dialog;
import android.view.View;

public final class GameDefs {
	public static final ArrayList<GameDefinition> TYPES;
	public static final GameDefinition DEFAULT = new Generic();

	static {
		//TODO include rules?
		//TODO favorites?
		TYPES = new ArrayList<GameDefinition>();
		TYPES.add(DEFAULT);
		
		//add game types, by hand
		TYPES.add(new FiveHundred());
		TYPES.add(new Pfeffer());
		TYPES.add(new UpAndDownTheRiver());
		
		Collections.sort(TYPES);
	}
	private GameDefs() {}
	
	public static GameDefinition getGameDef(int i) {
		return TYPES.get(Collections.binarySearch(TYPES, new Key(i)));
	}
	
	// private class for use as a Key object
	private static class Key extends GameDefinition {
		public Key(int id) {super(id, null, 0, true);}
		public Long calculateScore(Dialog dlg) {return null;}
		public String getContext(Dialog dlg) {return null;}
		public void onClick(View arg0) {}
	}
}

/**
 *  Copyright 2011 Tony Murray <murraytony@gmail.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.splashmobileproductions.scorekeep.games;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.splashmobileproductions.scorekeep.games.definitions.*;

public final class GameDefs {
	public static final int DEFAULT = 0;
	public static final int FIVE_HUNDRED = 1;
	public static final int PFEFFER = 2;
	public static final int UP_AND_DOWN_THE_RIVER = 3;
	
	public static final HashMap<Integer, String> games = new HashMap<Integer, String>();
	public static final ArrayList<GameDefinition> TYPES;

	static {
		//TODO include rules?
		//TODO favorites?
		TYPES = new ArrayList<GameDefinition>();
		TYPES.add(new Generic());
		
		//add game types, by hand
		TYPES.add(new FiveHundred());
		TYPES.add(new Pfeffer());
		TYPES.add(new UpAndDownTheRiver());
		
		games.put(DEFAULT, "Generic");
		
		Collections.sort(TYPES);
	}
	private GameDefs() {}
	
	public static GameDefinition getGameDef(int i) {
		switch (i) {
		case DEFAULT:
			return new Generic();
		case FIVE_HUNDRED:
			return new FiveHundred();
		case PFEFFER:
			return new Pfeffer();
		case UP_AND_DOWN_THE_RIVER:
			return new UpAndDownTheRiver();
		default:
			return null;
		}
				
	}
	/*
	// private class for use as a Key object
	private static class Key extends GameDefinition {
		public Key(int id) {super(id, null, 0, true);}
		public ScoreData getScore(Dialog dlg, PlayerData player) {return null;}
		public void onClick(View arg0) {}
	}
	*/
}

package com.splashmobileproductions.scorekeep.games.definitions;

import com.splashmobileproductions.scorekeep.R;

import com.splashmobileproductions.scorekeep.PlayerData;
import com.splashmobileproductions.scorekeep.ScoreData;
import com.splashmobileproductions.scorekeep.games.GameDefinition;
import com.splashmobileproductions.scorekeep.games.GameDefs;

import android.app.Dialog;
import android.view.View;

public class Pfeffer extends GameDefinition {

	public Pfeffer() {
		super(GameDefs.PFEFFER, "Pfeffer", R.layout.add_score_generic, true);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ScoreData getScore(Dialog dialog, PlayerData player) {
		// TODO Auto-generated method stub
		return null;
	}
}

package org.homelinux.murray.scorekeep.games.definitions;

import org.homelinux.murray.scorekeep.PlayerData;
import org.homelinux.murray.scorekeep.R;
import org.homelinux.murray.scorekeep.ScoreData;
import org.homelinux.murray.scorekeep.games.GameDefinition;
import org.homelinux.murray.scorekeep.games.GameDefs;

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

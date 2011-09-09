package com.splashmobileproductions.scorekeep.games;

import com.splashmobileproductions.scorekeep.R;

import com.splashmobileproductions.scorekeep.PlayerData;
import com.splashmobileproductions.scorekeep.ScoreData;

import android.app.Dialog;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Generic extends GameDefinition {

	public Generic() {
		super(GameDefs.DEFAULT, "Generic", R.layout.add_score_generic, true);
	}

	@Override
	public ScoreData getScore(Dialog dlg, PlayerData player) {
		TextView scoreView = (TextView) dlg.findViewById(R.id.score_edit);
		Long score = null;
		try {
			score = parseScore(scoreView.getText().toString());
		} catch(Exception e) {
			Toast.makeText(dlg.getContext(), R.string.number_parse_failed, Toast.LENGTH_LONG).show();
			return null;
		}
		
		TextView contextView = (TextView) dlg.findViewById(R.id.context_edit);
		String context = contextView.getText().toString();		
		
		return new ScoreData(score, context);
	}

	public void onClick(View v) {
		// nothing
	}
}

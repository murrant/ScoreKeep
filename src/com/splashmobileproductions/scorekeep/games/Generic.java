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

import com.splashmobileproductions.scorekeep.R;

import com.splashmobileproductions.scorekeep.controller.PlayerData;
import com.splashmobileproductions.scorekeep.controller.ScoreData;

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
		TextView contextView = (TextView) dlg.findViewById(R.id.context_edit);		
		String scoreInput = scoreView.getText().toString();
		String context = contextView.getText().toString();				

		Long score = null;
		// if there no score, but a context.  Leave score null and return
		if(scoreInput.isEmpty() && !context.isEmpty()) {	
			return new ScoreData(score, context);
		}
		
		try {
			score = parseScore(scoreInput);
		} catch(Exception e) {
			Toast.makeText(dlg.getContext(), R.string.number_parse_failed, Toast.LENGTH_LONG).show();
			return null;
		}

		


		
		return new ScoreData(score, context);
	}

	public void onClick(View v) {
		// nothing
	}
}

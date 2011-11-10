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
package com.splashmobileproductions.scorekeep;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.TextView;


public class ScoreCard extends Activity {
	@SuppressWarnings("unused")
	private static final String DEBUG_TAG = "ScoreKeep:ScoreCard";
	private GameData game;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.score_card);
		Uri dataUri = getIntent().getData();
		// If there is no data associated with the Intent, bring up new game dialog
		if (dataUri == null) {
			startActivity(new Intent(this, NewGameFragment.class));
			finish(); // remove this activity
			return;
		}

		// load the game data
		game = new GameData(this, dataUri);
		
		GridView grid = (GridView) findViewById(R.id.score_card_grid);
		TextView desc = (TextView) findViewById(R.id.game_desc_title);
		desc.setText(game.description);

		grid.setAdapter(game);

		// check if wake lock should be enabled and enable it
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		if (settings.getBoolean(SettingsFragment.KEY_SCREEN_ON, false)) {
		    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.score_card_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {		
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.score_round:
			return false;
		case R.id.new_game:
			startActivity(new Intent(this, NewGameFragment.class));
			finish(); // remove this activity
			return true;
		case R.id.change_players:
			//TODO add/remove/reorder players
			return false;
		case R.id.reset_scores:
			game.resetScores();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
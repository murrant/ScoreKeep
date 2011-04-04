package org.homelinux.murray.scorekeep;

import org.homelinux.murray.scorekeep.provider.Game;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;


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
			startActivity(new Intent(this, NewGame.class));
			return;
		}

		// load the game data
		game = new GameData(this, dataUri);
		
		GridView grid = (GridView) findViewById(R.id.score_card_grid);
		TextView desc = (TextView) findViewById(R.id.game_desc_title);
		desc.setText(game.description);
		
		grid.setAdapter(game);
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
		case R.id.new_game:
			startActivity(new Intent(this, NewGame.class));
			this.finish();
			return true;
		case R.id.change_players:
			//TODO modify players
			return false;
		case R.id.reset_scores:
			game.resetScores();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
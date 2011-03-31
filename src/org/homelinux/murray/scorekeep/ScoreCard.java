package org.homelinux.murray.scorekeep;

import org.homelinux.murray.scorekeep.provider.Game;
import org.homelinux.murray.scorekeep.provider.Player;
import org.homelinux.murray.scorekeep.provider.Score;
import org.homelinux.murray.scorekeep.provider.ScoresProvider;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class ScoreCard extends Activity {
	private static final String DEBUG_TAG = "ScoreKeep:ScoreCard";
	private long gameId;
	private long[] playerIds;


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
		gameId = ContentUris.parseId(dataUri);
		Cursor gameCursor = getContentResolver().query(dataUri, null, null, null, null);
		gameCursor.moveToFirst();
		String playerIdsString = gameCursor.getString(gameCursor.getColumnIndex(Game.COLUMN_NAME_PLAYER_IDS));
		playerIds = ScoresProvider.deserializePlayers(playerIdsString);
		
		LinearLayout ll = (LinearLayout) findViewById(R.id.score_columns);
		//Cursor playerCursor = this.getContentResolver().query(Player.CONTENT_URI, null, Player._ID + " IN \"" + playerIdsString + "\"", null, null);
		
		for(long playerId : playerIds) {
			ListView playerListView = new ListView(this);
			playerListView.setScrollContainer(false);
			LinearLayout header = new LinearLayout(this);
			header.setOrientation(LinearLayout.VERTICAL);

			TextView name = new TextView(this);
			Cursor playerCursor = getContentResolver().query(ContentUris.withAppendedId(Player.CONTENT_ID_URI_BASE, playerId), null, null, null, null);
			playerCursor.moveToFirst();
			name.setText(playerCursor.getString(playerCursor.getColumnIndex(Player.COLUMN_NAME_NAME)));
			name.setBackgroundColor(playerCursor.getInt(playerCursor.getColumnIndex(Player.COLUMN_NAME_COLOR)));
			header.addView(name);
			
			Button button = new Button(this);
			button.setText(getString(R.string.add_score));
			final long id = playerId;
			button.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					addPlayerScore(id, "5");
					
				}
			});
			header.addView(button);

			playerListView.addHeaderView(header);
			CursorAdapter ca = getPlayerAdapter(playerId);
			playerListView.setAdapter(ca);
			
			ll.addView(playerListView);
		}
		

		Log.d(DEBUG_TAG, "Cursor count:"+gameCursor.getCount());
		//grid.setAdapter(new ScoresAdapter(cursor));
		TextView desc = (TextView) this.findViewById(R.id.game_desc_title);
		desc.setText("Game ID: "+gameId);

	}
	
	private CursorAdapter getPlayerAdapter(long playerId) {
		String selection = Score.COLUMN_NAME_GAME_ID + "=" + gameId + " AND " + Score.COLUMN_NAME_PLAYER_ID + "=" + playerId;
		Cursor cursor = this.managedQuery(Score.CONTENT_URI, null, selection, null, null);
		return new PlayerScoresAdapter(this, cursor);
	}
	
	private long addPlayerScore(long playerId, String mathString) {
		ContentValues values = new ContentValues();
		values.put(Score.COLUMN_NAME_GAME_ID, gameId);
		values.put(Score.COLUMN_NAME_PLAYER_ID, playerId);
		MathEval me = new MathEval();
		double result = me.evaluate(mathString);
		long score = Math.round(result);
		values.put(Score.COLUMN_NAME_SCORE, score);
		this.getContentResolver().insert(Score.CONTENT_URI, values);
		return score;
	}
}
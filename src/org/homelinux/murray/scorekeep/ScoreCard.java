package org.homelinux.murray.scorekeep;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.TextView;


public class ScoreCard extends Activity {
	private static final String DEBUG_TAG = "ScoreKeep:ScoreCard";
	GridView grid;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.score_card);
		Uri dataUri = getIntent().getData();
		// If there is no data associated with the Intent, bring up new game dialog
		if (dataUri== null) {
			startActivity(new Intent(this, NewGame.class));
			return;
		}
		
		long gameId = ContentUris.parseId(dataUri);
		
		grid = (GridView) findViewById(R.id.score_card_grid);
		
		Cursor cursor = getGame(dataUri);
		Log.d(DEBUG_TAG, "Cursor count:"+cursor.getCount());
		//grid.setAdapter(new ScoresAdapter(cursor));
		TextView desc = (TextView) this.findViewById(R.id.game_desc_title);
		desc.setText("Game ID: "+gameId);

	}

	private Cursor getGame(Uri gameUri) {
		//TODO: sooo incorrect
		Cursor cursor = managedQuery(gameUri, null, null, null, null);
		return cursor;
	}
}
package org.homelinux.murray.scorekeep;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;


public class ScoreCard extends Activity {
	private static final String DEBUG_TAG = "ScoreKeep:ScoreCard";
	GridView grid;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.score_card);
		Intent intent = getIntent();
		// If there is no data associated with the Intent, bring up new game dialog
		if (intent.getData() == null) {
			startActivity(new Intent(this, NewGame.class));
			return;
		}
		
		grid = (GridView) findViewById(R.id.score_card_grid);
		
		Cursor cursor = getGame(intent.getData());
		Log.d(DEBUG_TAG, "Cursor count:"+cursor.getCount());
		//grid.setAdapter(new ScoresAdapter(cursor));

	}
	/*
    @Override
    public void onDestroy() {
    	this.onDestroy();
    	dbh.closeDb();
    }
	 */

	private Cursor getGame(Uri gameUri) {
		//TODO: sooo incorrect
		Cursor cursor = managedQuery(gameUri, null, null, null, null);
		return cursor;
	}
}
package org.homelinux.murray.scorekeep;

import org.homelinux.murray.scorekeep.R;
import org.homelinux.murray.scorekeep.provider.Game;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.content.Intent;
import android.database.Cursor;
import android.widget.Toast;

public class GameHistory extends ListActivity {
	private static final String DEBUG_TAG = "ScoreKeep:GameHistory";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.games_list);

		/* Android 3.0...
		CursorLoader loader = new CursorLoader(this, Game.CONTENT_URI, null, null, null, null);
		Cursor cursor = loader.loadInBackground();
		*/
		Cursor cursor = managedQuery(Game.CONTENT_URI, null, null, null, null);
		Log.d(DEBUG_TAG, "Cursor retrieved:"+cursor);
		
		GamesListAdapter gla = new GamesListAdapter(this,cursor);
		setListAdapter(gla);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.games_list_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.new_game:
			startActivity(new Intent(this, NewGame.class));
			return true;
		case R.id.view_players:
			startActivity(new Intent(this, PlayerList.class));
			return true;
		case R.id.clear_history:
			int rows = getContentResolver().delete(Game.CONTENT_URI, null, null);
			if(rows>0) {
				Toast.makeText(getApplicationContext(), rows+" games deleted!", Toast.LENGTH_SHORT).show();
				return true;
			}
			return false;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
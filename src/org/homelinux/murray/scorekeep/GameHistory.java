package org.homelinux.murray.scorekeep;

import org.homelinux.murray.scorekeep.R;
import org.homelinux.murray.scorekeep.provider.Game;
import android.app.ListActivity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.View;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.widget.AdapterView;
import android.widget.Toast;


public class GameHistory extends ListActivity {
	@SuppressWarnings("unused")
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
		
		GamesListAdapter gla = new GamesListAdapter(this,cursor);
		setListAdapter(gla);
		
		// Open games on click
		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {  
			public void onItemClick(AdapterView<?> listView, View item, int position, long id) {
				Intent intent = new Intent(listView.getContext(), ScoreCard.class);
				Uri gameUri = ContentUris.withAppendedId(Game.CONTENT_ID_URI_BASE, id);
				intent.setData(gameUri);  //set data uri for the new game
				startActivity(intent);
			}
		});
		
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
		case R.id.glmenu_new_game:
			startActivity(new Intent(this, NewGame.class));
			return true;
		case R.id.glmenu_view_players:
			startActivity(new Intent(this, PlayerList.class));
			return true;
		case R.id.glmenu_clear_history:
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
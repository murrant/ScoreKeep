package org.homelinux.murray.scorekeep;

import org.homelinux.murray.scorekeep.R;
import org.homelinux.murray.scorekeep.provider.Game;
import org.homelinux.murray.scorekeep.provider.ScoresProvider;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.widget.Toast;

public class GameHistory extends ListActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.games_list);
        
        Cursor cursor = managedQuery(Game.CONTENT_URI, null, null, null, null);

        Log.d("ScoreKeep:GameHistory", "Game Uri is: "+Game.CONTENT_URI);
        if(cursor == null) {
            Log.d("ScoreKeep:GameHistory", "Cursor is null");
        } else {
        	Log.d("ScoreKeep:GameHistory", "cursor.getCount()=" + cursor.getCount());
            GamesListAdapter gla = new GamesListAdapter(this,cursor);
            setListAdapter(gla);
        }
    }
  /*  
    @Override
    public void onDestroy() {
    	this.onDestroy();
    	dbh.closeDb();
    }
    */
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
        	Intent intent = new Intent(GameHistory.this, NewGame.class);
			GameHistory.this.startActivity(intent);
    	    return true;
    	case R.id.view_players:
			Toast.makeText(getApplicationContext(), "No Players",
					Toast.LENGTH_SHORT).show();
        	return true;
    	case R.id.clear_history:
			Toast.makeText(getApplicationContext(), "History Destroyed!",
					Toast.LENGTH_SHORT).show();
        	return true;
    	default:
        	return super.onOptionsItemSelected(item);
    	}
	}
}
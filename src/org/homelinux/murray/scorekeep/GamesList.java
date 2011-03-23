package org.homelinux.murray.scorekeep;

import org.homelinux.murray.scorekeep.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.content.Intent;
import android.widget.Toast;

public class GamesList extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.games_list);
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
        	Intent intent = new Intent(GamesList.this, NewGame.class);
			GamesList.this.startActivity(intent);
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
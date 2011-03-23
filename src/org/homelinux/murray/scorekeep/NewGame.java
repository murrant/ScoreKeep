package org.homelinux.murray.scorekeep;

import java.util.ArrayList;

import org.homelinux.murray.scorekeep.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.content.Intent;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.widget.Adapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NewGame extends Activity {
	PlayerAdapter adapter;
	DbHelper dbh;
	private ListView list;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_game);
        
        dbh = new DbHelper(this);
        list = (ListView) findViewById(R.id.new_game_players_list);
        adapter=new PlayerAdapter(this, dbh.getPlayers());
        list.setAdapter(adapter);
        list.setItemsCanFocus(false);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }
    /*
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	dbh.closeDb();
    }
    */
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.new_game_menu, menu);
    	return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.start_game:
			Intent intent = new Intent(NewGame.this, ScoreCard.class);

			Adapter adapter = list.getAdapter();
			int count = adapter.getCount();
			ArrayList<Long> selectedItems = new ArrayList<Long>();
			for (int i = 0; i<count; i++) {
				CheckBox cb = (CheckBox) list.getChildAt(i);
				if(cb.isChecked()) {
					selectedItems.add(list.getItemIdAtPosition(i));
				}
			}
        	int selectedCount = selectedItems.size();
        	if(selectedCount < 1) {
        		Toast.makeText(this,R.string.no_players,Toast.LENGTH_SHORT).show();
        		return false;
        	}
        	long[] players = new long[selectedCount];
        	for (int i =0; i<selectedCount; i++) {
        		players[i] = selectedItems.get(i).longValue();
        	}
        	
        	final TextView gameDesc = (TextView) findViewById(R.id.game_desc);
        	long gameId = dbh.newGame(gameDesc.getText().toString(), players);
        	Bundle bundle = new Bundle();
        	bundle.putLong("GameID",gameId);  //pass game id of stored game
        	intent.putExtras(bundle);
        	
			NewGame.this.startActivity(intent);
    	    return true;
    	case R.id.add_player:
    		final AlertDialog.Builder alert = new AlertDialog.Builder(this);
    		alert.setTitle("New Player");
    		final EditText input = new EditText(this);
    		alert.setView(input);
    		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int whichButton) {
    				String value = input.getText().toString().trim();
    				addPlayer(value);
    				
    			}
    		});
    		alert.setNegativeButton("Cancel",
    				new DialogInterface.OnClickListener() {
    					public void onClick(DialogInterface dialog, int whichButton) {
    						dialog.cancel();
    					}
    				});
    		alert.show();
        	return true;
    	default:
        	return super.onOptionsItemSelected(item);
    	}
	}
    
	public void addPlayer(String name) {
		 dbh.newPlayer(name);
		 adapter.notifyDataSetChanged();
	}
}
package org.homelinux.murray.scorekeep;

import org.homelinux.murray.scorekeep.R;
import org.homelinux.murray.scorekeep.games.GameDefinition;
import org.homelinux.murray.scorekeep.games.GameDefs;
import org.homelinux.murray.scorekeep.provider.Game;
import org.homelinux.murray.scorekeep.provider.Player;
import org.homelinux.murray.scorekeep.provider.ScoresProvider;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.content.ContentValues;
import android.content.Intent;
import android.content.DialogInterface;
import android.database.Cursor;
import android.app.AlertDialog;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class NewGame extends Activity {
	private static final String DEBUG_TAG = "ScoreKeep:NewGame";
	private ListView list;
	private Spinner gameTypes;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_game);

		gameTypes = (Spinner) findViewById(R.id.game_type_list);
		ArrayAdapter<GameDefinition> aa = new ArrayAdapter<GameDefinition>(this, R.layout.item_game_type, android.R.id.text1, GameDefs.TYPES);
		gameTypes.setAdapter(aa);

		Cursor c = managedQuery(Player.CONTENT_URI, null, null, null, null);
		list = (ListView) findViewById(R.id.new_game_players_list);
		PlayerAdapter adapter = new PlayerAdapter(this, c);
		list.setAdapter(adapter);
		list.setItemsCanFocus(false);
		list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	}

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
			Intent intent = new Intent(this, ScoreCard.class);

			long[] players = list.getCheckedItemIds();
			if(players.length < 1) {
				Toast.makeText(this,R.string.no_players_selected,Toast.LENGTH_SHORT).show();
				return false;
			}

			GameDefinition gameType = GameDefs.TYPES.get(gameTypes.getSelectedItemPosition());
			Uri newGameUri = newGame(gameType, players);
			intent.setData(newGameUri);  //set data uri for the new game
			startActivity(intent);
			return true;
		case R.id.add_player:
			final AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle(R.string.new_player);
			final EditText input = new EditText(this);
			input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
			alert.setView(input);
			alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					String value = input.getText().toString().trim();
					addPlayer(value);

				}
			});
			alert.setNegativeButton(R.string.cancel,
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

	private Uri addPlayer(String name) {
		ContentValues content = new ContentValues();
		content.put(Player.COLUMN_NAME_NAME, name);
		Uri result = getContentResolver().insert(Player.CONTENT_URI, content);
		Log.d(DEBUG_TAG, "Add player result Uri: "+result.toString());
		return result;
	}

	private Uri newGame(GameDefinition game, long[] player_ids) {
		ContentValues content = new ContentValues();
		Log.d(DEBUG_TAG, "New Game: "+game.getClass().getName());
		content.put(Game.COLUMN_NAME_TYPE, game.getGameId());
		content.put(Game.COLUMN_NAME_DESCRIPTION, game.name);
		content.put(Game.COLUMN_NAME_PLAYER_IDS, ScoresProvider.serializePlayers(player_ids));
		Uri result = getContentResolver().insert(Game.CONTENT_URI, content);
		Log.d(DEBUG_TAG, "New Game result Uri: "+result.toString());
		return result;
	}
}
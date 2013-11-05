/**
 *  Copyright 2011-2013 Tony Murray <murraytony@gmail.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.splashmobileproductions.scorekeep;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.splashmobileproductions.scorekeep.games.GameDefinition;
import com.splashmobileproductions.scorekeep.games.GameDefs;
import com.splashmobileproductions.scorekeep.provider.Game;
import com.splashmobileproductions.scorekeep.provider.Player;
import com.splashmobileproductions.scorekeep.provider.ScoresProvider;

public class NewGameFragment extends Fragment {
	private static final String DEBUG_TAG = "ScoreKeep:NewGameFragment";
    private final static String[] FROM = new String[]{Player.COLUMN_NAME_NAME};
    private final static int[] TO = new int[]{android.R.id.text1};
	private ListView list;
	private Spinner gameTypes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);

        View view = inflater.inflate(R.layout.new_game, container, false);

        gameTypes = (Spinner) view.findViewById(R.id.game_type_list);
        ArrayAdapter<GameDefinition> aa = new ArrayAdapter<GameDefinition>(getActivity(), R.layout.item_game_type, android.R.id.text1, GameDefs.TYPES);
        gameTypes.setAdapter(aa);

        Cursor c = getActivity().managedQuery(Player.CONTENT_URI, null, null, null, null);
        list = (ListView) view.findViewById(R.id.new_game_players_list);

        list.setAdapter(new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_multiple_choice, c, FROM, TO));
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.new_game_menu, menu);
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.start_game:
			Intent intent = new Intent(getActivity(), ScoreCardActivity.class);

			long[] players = list.getCheckedItemIds();
			if(players.length < 1) {
				Toast.makeText(getActivity(),R.string.no_players_selected,Toast.LENGTH_SHORT).show();
				return false;
			}

			GameDefinition gameType = GameDefs.TYPES.get(gameTypes.getSelectedItemPosition());
			Uri newGameUri = newGame(gameType, players);
			intent.setData(newGameUri);  //set data uri for the new game
			startActivity(intent);
            getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
			return true;
		case R.id.add_player:
			final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
			alert.setTitle(R.string.new_player);
			final EditText input = new EditText(getActivity());
			input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
			alert.setView(input);
			alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					String value = input.getText().toString().trim();
					addPlayer(value);

				}
			});
			alert.setNegativeButton(android.R.string.cancel,
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
		// add the player to the Content Provider
		ContentValues content = new ContentValues();
		content.put(Player.COLUMN_NAME_NAME, name);
		Uri result = getActivity().getContentResolver().insert(Player.CONTENT_URI, content);
		
		//register a listener to select the user once the listview gets updated.
		final long targetId = ContentUris.parseId(result);
		list.getAdapter().registerDataSetObserver(new DataSetObserver() {
			public void onChanged() {
				// find the new user and select it
				int count = list.getCount()+1;
				// BUG: the count seems to be off by one.
				for(int i = 0; i<count; i++) {
					long itemId = list.getItemIdAtPosition(i);
					boolean isChecked = (itemId == targetId); // remove and only change check when needed
					if(isChecked) {
						list.setItemChecked(i, isChecked);
						list.getAdapter().unregisterDataSetObserver(this);
					}
				}
				
			}
		});

		Log.d(DEBUG_TAG, "Added player Uri: "+result.toString());
		return result;
	}

	private Uri newGame(GameDefinition game, long[] player_ids) {
		ContentValues content = new ContentValues();
		Log.d(DEBUG_TAG, "New Game: "+game.getClass().getName());
		content.put(Game.COLUMN_NAME_TYPE, game.getGameId());
		content.put(Game.COLUMN_NAME_DESCRIPTION, game.name);
		content.put(Game.COLUMN_NAME_PLAYER_IDS, ScoresProvider.serializePlayers(player_ids));
		Uri result = getActivity().getContentResolver().insert(Game.CONTENT_URI, content);
		Log.d(DEBUG_TAG, "New Game result Uri: "+result.toString());
		return result;
	}
	/* TODO: remove?
	private static List<Long> asList(final long[] l) {
	    return new AbstractList<Long>() {
	        public Long get(int i) {return l[i];}
	        // throws NPE if val == null
	        public Long set(int i, Long val) {
	            Long oldVal = l[i];
	            l[i] = val;
	            return oldVal;
	        }
	        public int size() { return l.length;}
	    };
	}
*/

}
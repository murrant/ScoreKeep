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

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.transition.Scene;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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

public class NewGameFragment extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String DEBUG_TAG = "ScoreKeep:NewGameFragment";
    private static final int PLAYER_LOADER = 0;
    private final static String[] FROM = new String[]{Player.COLUMN_NAME_NAME};
    private final static int[] TO = new int[]{android.R.id.text1};
    private SimpleCursorAdapter mPlayerAdapter;
    private ListView mPlayerList;
    private Spinner gameTypes;
    private Scene mListScene;

    @SuppressLint("Override")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @SuppressLint("Override")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sk_new_game_dialog, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

//        mListScene = Scene.getSceneForLayout(container, R.layout.sk_new_game_dialog, getActivity());
        mPlayerAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_multiple_choice, null, FROM, TO, 0);
        mPlayerList = (ListView) view.findViewById(R.id.new_game_players_list);
        mPlayerList.setAdapter(mPlayerAdapter);

//        gameTypes = (Spinner) view.findViewById(R.id.game_type_list);
//        ArrayAdapter<GameDefinition> aa = new ArrayAdapter<GameDefinition>(getActivity(), R.layout.item_game_type, android.R.id.text1, GameDefs.TYPES);
//        gameTypes.setAdapter(aa);

        getLoaderManager().initLoader(PLAYER_LOADER, null, this);

        return view;
    }

    public void startNewGame(View view) {
        Intent intent = new Intent(getActivity(), ScoreCardActivity.class);

        long[] players = mPlayerList.getCheckedItemIds();
        if (players.length < 1) {
            Toast.makeText(getActivity(), R.string.no_players_selected, Toast.LENGTH_SHORT).show();
            return;
        }

        GameDefinition gameType = GameDefs.TYPES.get(gameTypes.getSelectedItemPosition());
        Uri newGameUri = newGame(gameType, players);
        intent.setData(newGameUri);  //set data uri for the new game
        startActivity(intent);
    }

    public void addPlayer(View view) {
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
                }
        );
        final AlertDialog dialog = alert.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        dialog.show();
    }

    private Uri addPlayer(String name) {
        // add the player to the Content Provider
        ContentValues content = new ContentValues();
        content.put(Player.COLUMN_NAME_NAME, name);
        Uri result = getActivity().getContentResolver().insert(Player.CONTENT_URI, content);

        //register a listener to select the user once the listview gets updated.
        final long targetId = ContentUris.parseId(result);
        mPlayerList.getAdapter().registerDataSetObserver(new DataSetObserver() {
            public void onChanged() {
                // find the new user and select it
                int count = mPlayerList.getCount() + 1;
                // BUG: the count seems to be off by one.
                for (int i = 0; i < count; i++) {
                    long itemId = mPlayerList.getItemIdAtPosition(i);
                    boolean isChecked = (itemId == targetId); // remove and only change check when needed
                    if (isChecked) {
                        mPlayerList.setItemChecked(i, isChecked);
                        mPlayerList.getAdapter().unregisterDataSetObserver(this);
                    }
                }

            }
        });

//        TransitionManager.go(mListScene);
        Log.d(DEBUG_TAG, "Added player Uri: " + result.toString());
        return result;
    }

    private Uri newGame(GameDefinition game, long[] player_ids) {
        ContentValues content = new ContentValues();
        Log.d(DEBUG_TAG, "New Game: " + game.getClass().getName());
        content.put(Game.COLUMN_NAME_TYPE, game.getGameId());
        content.put(Game.COLUMN_NAME_DESCRIPTION, game.name);
        content.put(Game.COLUMN_NAME_PLAYER_IDS, ScoresProvider.serializePlayers(player_ids));
        Uri result = getActivity().getContentResolver().insert(Game.CONTENT_URI, content);
        Log.d(DEBUG_TAG, "New Game result Uri: " + result.toString());
        return result;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        switch (loaderID) {
            case PLAYER_LOADER:
                return new CursorLoader(getActivity(), Player.CONTENT_URI, null, null, null, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if (cursor.getCount() > 0) {
            //TransitionManager.go(mListScene);
        }
        mPlayerAdapter.changeCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mPlayerAdapter.changeCursor(null);
    }
}
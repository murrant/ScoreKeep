/**
 * Copyright 2011-2013 Tony Murray <murraytony@gmail.com>
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.splashmobileproductions.scorekeep;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.InputType;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.splashmobileproductions.scorekeep.games.GameDefinition;
import com.splashmobileproductions.scorekeep.games.GameDefs;
import com.splashmobileproductions.scorekeep.provider.Game;
import com.splashmobileproductions.scorekeep.provider.Player;
import com.splashmobileproductions.scorekeep.provider.ScoresProvider;

public class NewGameFragment extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String DEBUG_TAG = "ScoreKeep:NewGameFragme";
    private static final int PLAYER_LOADER = 0;
    private final static String[] FROM = new String[]{Player.COLUMN_NAME_NAME};
    private final static int[] TO = new int[]{android.R.id.text1};
    private SimpleCursorAdapter mPlayerAdapter;
    private ListView mPlayerList;
    private Scene mListScene;

    @SuppressLint("Override")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @SuppressLint("Override")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View defaultLayout = inflater.inflate(R.layout.sk_new_game_dialog_default, container, false);
        initButtons(defaultLayout);

        mPlayerAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_multiple_choice, null, FROM, TO, 0);

        return defaultLayout;
    }

    @SuppressLint("Override")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Window window = getDialog().getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);

        ViewGroup sceneRoot = (ViewGroup) getDialog().findViewById(R.id.new_game_layout).getParent();
        mListScene = Scene.getSceneForLayout(sceneRoot, R.layout.sk_new_game_dialog, getActivity());

        getLoaderManager().initLoader(PLAYER_LOADER, null, this);
    }

    private void initButtons(View view) {
        View cb = view.findViewById(R.id.new_game_close_button);
        cb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        View apb = view.findViewById(R.id.new_game_add_player_button);
        apb.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                showAddPlayerDialog();
            }
        });

        View sgb = view.findViewById(R.id.new_game_go_button);
        sgb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewGame();
            }
        });
    }

    public void showPlayerList() {
        if (mPlayerList == null) {
            String gameName = ((EditText) getDialog().findViewById(R.id.new_game_text)).getText().toString();
            TransitionManager.go(mListScene);
            ((EditText) getDialog().findViewById(R.id.new_game_text)).setText(gameName);
            mPlayerList = (ListView) getDialog().findViewById(R.id.new_game_players_list);
            mPlayerList.setAdapter(mPlayerAdapter);
            initButtons(getDialog().findViewById(R.id.new_game_layout));

        }
    }

    public void startNewGame() {
        Intent intent = new Intent(getActivity(), ScoreCardActivity.class);
        if (mPlayerList == null) {
            Toast.makeText(getActivity(), R.string.no_players_add_one, Toast.LENGTH_SHORT).show();
            return;
        }
        long[] players = mPlayerList.getCheckedItemIds();
        if (players.length < 1) {
            Toast.makeText(getActivity(), R.string.no_players_selected, Toast.LENGTH_SHORT).show();
            return;
        }

        GameDefinition gameType = GameDefs.TYPES.get(GameDefs.DEFAULT);
        String gameName = ((EditText) getDialog().findViewById(R.id.new_game_text)).getText().toString();
        if (gameName.isEmpty()) gameName = getString(R.string.new_game);
        gameType.setName(gameName);
        Uri newGameUri = newGame(gameType, players);
        intent.setData(newGameUri);  //set data uri for the new game
        dismiss();
        startActivity(intent);
    }

    public void showAddPlayerDialog() {
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

        showPlayerList();

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
                        mPlayerList.setItemChecked(i, true);
                        mPlayerList.getAdapter().unregisterDataSetObserver(this);
                    }
                }

            }
        });

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
            showPlayerList();
        }
        mPlayerAdapter.changeCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mPlayerAdapter.changeCursor(null);
    }
}
/**
 * Copyright 2011 Tony Murray <murraytony@gmail.com>
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
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.splashmobileproductions.scorekeep.data.GameAdapter;
import com.splashmobileproductions.scorekeep.provider.Game;
import com.splashmobileproductions.scorekeep.provider.Score;


public class GameHistoryFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    @SuppressWarnings("unused")
    private static final String DEBUG_TAG = "ScoreKeep:GameHistoryFragment";
    private static final int GAME_LOADER = 1;
    GameAdapter mGameListAdapter;

    @SuppressLint("Override")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mGameListAdapter = new GameAdapter(getActivity(), null);
        setListAdapter(mGameListAdapter);

        getLoaderManager().initLoader(GAME_LOADER, null, this);
    }

    @SuppressLint("Override")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sk_game_list, container, false);
    }

    @SuppressLint("Override")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Open games on click
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> listView, View item, int position, long id) {
                Intent intent = new Intent(listView.getContext(), ScoreCardActivity.class);
                Uri gameUri = ContentUris.withAppendedId(Game.CONTENT_ID_URI_BASE, id);
                intent.setData(gameUri);  //set data uri for the new game
                startActivity(intent);
            }
        });
    }

    @SuppressLint("Override")
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().onCreateOptionsMenu(menu);
        inflater.inflate(R.menu.games_list_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @SuppressLint("Override")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.glmenu_clear_history:
                ContentResolver cr = getActivity().getContentResolver();
                int rows = cr.delete(Game.CONTENT_URI, null, null);
                cr.delete(Score.CONTENT_URI, null, null);
                if (rows > 0) {
                    Toast.makeText(getActivity().getApplicationContext(), rows + " games deleted!", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        switch (loaderID) {
            case GAME_LOADER:
                return new CursorLoader(getActivity(), Game.CONTENT_URI, null, null, null, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mGameListAdapter.changeCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mGameListAdapter.changeCursor(null);
    }


}
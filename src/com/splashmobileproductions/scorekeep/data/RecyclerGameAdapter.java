package com.splashmobileproductions.scorekeep.data;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.util.LongSparseArray;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.splashmobileproductions.scorekeep.R;
import com.splashmobileproductions.scorekeep.ScoreCardActivity;
import com.splashmobileproductions.scorekeep.provider.Game;
import com.splashmobileproductions.scorekeep.provider.Player;
import com.splashmobileproductions.scorekeep.provider.ScoresProvider;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Copyright 2015 Tony Murray <murraytony@gmail.com>
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

public class RecyclerGameAdapter extends CursorRecyclerAdapter<RecyclerGameAdapter.ViewHolder> implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String DEBUG_TAG = "SK:RecyclerGameAdapter";
    private static final int PLAYER_LOADER = 54;
    private final PrettyTime mPrettyTime = new PrettyTime(Locale.getDefault());
    private final LongSparseArray<String> mPlayers = new LongSparseArray<>();
    FragmentActivity mFragmentActivity;

    public RecyclerGameAdapter(FragmentActivity context, Cursor c) {
        super(c);
        mFragmentActivity = context;
        mFragmentActivity.getSupportLoaderManager().initLoader(PLAYER_LOADER, null, this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sk_game_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
        holder.mDescription.setText(getDescription(cursor));
        holder.mModifiedDate.setText(getModifiedDate(cursor));
        holder.mPlayers.setText(getPlayerList(cursor));
        holder.mGameID = getGameID(cursor);
    }

    private long getGameID(Cursor cursor) {
        return cursor.getLong(cursor.getColumnIndex(Game._ID));
    }

    private String getDescription(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndex(Game.COLUMN_NAME_DESCRIPTION));
    }

    private String getPlayerList(Cursor cursor) {
        if (mPlayers.size() == 0) {
            return "";
        } else {
            String playerIdsString = cursor.getString(cursor.getColumnIndex(Game.COLUMN_NAME_PLAYER_IDS));
            long[] playerIds = ScoresProvider.deserializePlayers(playerIdsString);
            ArrayList<String> playerNames = new ArrayList<>();
            for (long playerId : playerIds) {
                playerNames.add(mPlayers.get(playerId));
            }
            return TextUtils.join(", ", playerNames);
        }
    }

    private String getModifiedDate(Cursor cursor) {
        Date playedDate = new Date(cursor.getLong(cursor.getColumnIndex(Game.COLUMN_NAME_MODIFICATION_DATE)));
        return mPrettyTime.format(playedDate);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case PLAYER_LOADER:
                return new CursorLoader(mFragmentActivity, Player.CONTENT_URI, null, null, null, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursor.moveToPosition(-1); //reset cursor position, could be a reused cursor
        while (cursor.moveToNext()) {
            mPlayers.put(cursor.getLong(cursor.getColumnIndex(Player._ID)), cursor.getString(cursor.getColumnIndex(Player.COLUMN_NAME_NAME)));
        }

        // notify for redraw if the player names came late
        notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mPlayers.clear();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mDescription, mModifiedDate, mPlayers;
        long mGameID;

        public ViewHolder(View v) {
            super(v);
            mDescription = (TextView) itemView.findViewById(R.id.gl_desc);
            mModifiedDate = (TextView) itemView.findViewById(R.id.gl_modified);
            mPlayers = (TextView) itemView.findViewById(R.id.gl_players);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            Log.d(DEBUG_TAG, "Item Clicked. Game ID: "+mGameID);
            Intent intent = new Intent(v.getContext(), ScoreCardActivity.class);
            Uri gameUri = ContentUris.withAppendedId(Game.CONTENT_ID_URI_BASE, mGameID);
            intent.setData(gameUri);  //set data uri for the new game
            v.getContext().startActivity(intent);
        }
    }
}

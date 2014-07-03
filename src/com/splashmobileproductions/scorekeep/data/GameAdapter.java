/**
 *  Copyright 2011 Tony Murray <murraytony@gmail.com>
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
package com.splashmobileproductions.scorekeep.data;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.splashmobileproductions.scorekeep.R;
import com.splashmobileproductions.scorekeep.provider.Game;
import com.splashmobileproductions.scorekeep.provider.Player;
import com.splashmobileproductions.scorekeep.provider.ScoresProvider;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class GameAdapter extends CursorAdapter implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int PLAYER_LOADER = 0;
    private final PrettyTime mPrettyTime = new PrettyTime(Locale.getDefault());
    private final LongSparseArray<String> mPlayers = new LongSparseArray<String>();
    private Context mContext;

    public GameAdapter(Context context, Cursor c) {
        super(context, c, 0);
        mContext = context;
        ((Activity) context).getLoaderManager().initLoader(PLAYER_LOADER, null, this);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.sk_game_list_item, parent, false);

        setDescription((TextView) itemView.findViewById(R.id.gl_desc), cursor);
        setModifiedDate((TextView) itemView.findViewById(R.id.gl_modified), cursor);
        setPlayerList((TextView) itemView.findViewById(R.id.gl_players), cursor);

        return itemView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        setDescription((TextView) view.findViewById(R.id.gl_desc), cursor);
        setModifiedDate((TextView) view.findViewById(R.id.gl_modified), cursor);
        setPlayerList((TextView) view.findViewById(R.id.gl_players), cursor);
    }

    private void setDescription(TextView item, Cursor cursor) {
        item.setText(cursor.getString(cursor.getColumnIndex(Game.COLUMN_NAME_DESCRIPTION)));
    }

    private void setPlayerList(TextView item, Cursor cursor) {
        if (mPlayers.size() == 0) {
            item.setVisibility(View.GONE);
        } else {
            String playerIdsString = cursor.getString(cursor.getColumnIndex(Game.COLUMN_NAME_PLAYER_IDS));
            long[] playerIds = ScoresProvider.deserializePlayers(playerIdsString);
            ArrayList<String> playerNames = new ArrayList<String>();
            for (long playerId : playerIds) {

                playerNames.add(mPlayers.get(playerId));
            }
            item.setText(TextUtils.join(", ", playerNames));
        }
    }

    private void setModifiedDate(TextView item, Cursor cursor) {
        Date playedDate = new Date(cursor.getLong(cursor.getColumnIndex(Game.COLUMN_NAME_MODIFICATION_DATE)));
        item.setText(mPrettyTime.format(playedDate));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        switch (loaderID) {
            case PLAYER_LOADER:
                return new CursorLoader(mContext, Player.CONTENT_URI, null, null, null, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        while (cursor.moveToNext()) {
            mPlayers.put(cursor.getLong(cursor.getColumnIndex(Player._ID)), cursor.getString(cursor.getColumnIndex(Player.COLUMN_NAME_NAME)));
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mPlayers.clear();
    }
}

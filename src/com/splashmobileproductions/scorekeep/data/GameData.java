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

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.splashmobileproductions.scorekeep.R;
import com.splashmobileproductions.scorekeep.games.GameDefinition;
import com.splashmobileproductions.scorekeep.games.GameDefs;
import com.splashmobileproductions.scorekeep.provider.Game;
import com.splashmobileproductions.scorekeep.provider.ScoresProvider;

import java.util.ArrayList;

public final class GameData extends BaseAdapter implements ListAdapter {
    private static final String DEBUG_TAG = "ScoreKeep:GameData";

    public final long id;
    public final GameDefinition gameDef;
    public final String description;
    public final long starting_score = 0;  //TODO support alternate starting scores
    private final LayoutInflater mInflater;
    private ArrayList<PlayerData> players = new ArrayList<PlayerData>();
    private ContentValues extra = new ContentValues();

    /**
     * Load game data from the database with all players.
     *
     * @param context The calling activitie's context
     * @param gameUri The unique Id of the desired game
     */
    public GameData(Context context, Uri gameUri) {
        id = ContentUris.parseId(gameUri);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Cursor gameCursor = context.getContentResolver().query(gameUri, null, null, null, null);
        gameCursor.moveToFirst();
        int gameType = gameCursor.getInt(gameCursor.getColumnIndex(Game.COLUMN_NAME_TYPE));
        Log.d(DEBUG_TAG, "Game Type ID:" + gameType);
        gameDef = GameDefs.getGameDef(gameType);
        description = gameCursor.getString(gameCursor.getColumnIndex(Game.COLUMN_NAME_DESCRIPTION));
        String playerIdsString = gameCursor.getString(gameCursor.getColumnIndex(Game.COLUMN_NAME_PLAYER_IDS));

        long[] playerIds = ScoresProvider.deserializePlayers(playerIdsString);
        for (long playerId : playerIds) {
            players.add(new PlayerData(context, playerId, this));
        }
    }

    public ArrayList<PlayerData> getPlayers() {
        return players;
    }

    public int getCount() {
        return players.size();
    }

    public Object getItem(int position) {
        return players.get(position);
    }

    public PlayerData getPlayerById(long playerId) {
        for (PlayerData pl : players) {
            if (pl.id == playerId) return pl;
        }
        return null;
    }

    public long getItemId(int position) {
        return players.get(position).id;
    }

    public int getItemViewType(int position) {
        return IGNORE_ITEM_VIEW_TYPE;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.badge, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.badge_name);
            holder.score = (TextView) convertView.findViewById(R.id.badge_score);
            holder.context = (TextView) convertView.findViewById(R.id.badge_context);
            holder.addButton = (ImageView) convertView.findViewById(R.id.badge_add);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PlayerData pl = players.get(position);
        holder.name.setText(pl.name);
        convertView.setBackgroundColor((int) pl.color);
        holder.score.setText(Long.toString(pl.getTotal()));
        if (pl.getCurrentContext() != null) {
            holder.context.setText(pl.getCurrentContext());
        }
        holder.addButton.setOnClickListener(pl);
        holder.name.setOnClickListener(pl);
        return convertView;
    }

    public int getViewTypeCount() {
        return 1;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }

    public boolean areAllItemsEnabled() {
        // TODO Auto-generated method stub
        return true;
    }

    public boolean isEnabled(int position) {
        // TODO Auto-generated method stub
        return true;
    }

    public void resetScores() {
        for (PlayerData player : players) {
            player.resetScore();
        }
        notifyDataSetChanged();
    }

    public void putExtra(String key, Integer value) {
        extra.put(key, value);
    }

    public void putExtra(String key, Boolean value) {
        extra.put(key, value);
    }

    public void putExtra(String key, String value) {
        extra.put(key, value);
    }

    public void putExtra(String key, Long value) {
        extra.put(key, value);
    }

    public Object getExtra(String key) {
        return extra.get(key);
    }

    public Integer getExtraAsInteger(String key) {
        return extra.getAsInteger(key);
    }

    ;

    public Boolean getExtraAsBoolean(String key) {
        return extra.getAsBoolean(key);
    }

    public String getExtraAsString(String key) {
        return extra.getAsString(key);
    }

    public Long getExtraAsLong(String key) {
        return extra.getAsLong(key);
    }

    public static class ViewHolder {
        TextView name;
        TextView score;
        TextView context;
        ImageView addButton;
    }
}

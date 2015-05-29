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

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.splashmobileproductions.scorekeep.AddScoreDialog;
import com.splashmobileproductions.scorekeep.R;
import com.splashmobileproductions.scorekeep.provider.Player;
import com.splashmobileproductions.scorekeep.provider.Score;

import java.util.ArrayList;

public final class PlayerData implements View.OnClickListener, DialogInterface.OnClickListener {
    private static final String DEBUG_TAG = "ScoreKeep:PlayerData";
    public final long id;
    public final String name;
    public final int color;
    public final GameData game;
    private final ArrayList<ScoreData> scores = new ArrayList<>();
    private final Context appContext;
    private long total;
    private String scoreContext = null;

    /**
     * Load player data from the DB with scores from a game
     *
     * @param context  The context to run a query
     * @param playerId Unique player identifier
     * @param gameId   Unique id for game to get scores from
     */
    public PlayerData(Context context, long playerId, GameData gameId) {
        id = playerId;
        this.game = gameId;
        appContext = context.getApplicationContext();

        Cursor pc = appContext.getContentResolver().query(ContentUris.withAppendedId(Player.CONTENT_ID_URI_BASE, playerId), null, null, null, null);
        pc.moveToFirst();
        name = pc.getString(pc.getColumnIndex(Player.COLUMN_NAME_NAME));
        color = pc.getInt(pc.getColumnIndex(Player.COLUMN_NAME_COLOR));

        String selection = Score.COLUMN_NAME_GAME_ID + "=" + game.id + " AND " + Score.COLUMN_NAME_PLAYER_ID + "=" + playerId;
        Cursor sc = appContext.getContentResolver().query(Score.CONTENT_URI, null, selection, null, null);
        int scoreColumn = sc.getColumnIndex(Score.COLUMN_NAME_SCORE);
        int idColumn = sc.getColumnIndex(Score._ID);
        int contextColumn = sc.getColumnIndex(Score.COLUMN_NAME_CONTEXT);
        int createdColumn = sc.getColumnIndex(Score.COLUMN_NAME_CREATE_DATE);
        total = game.starting_score;
        long id, created;
        Long score;
        String tmpContext = null;
        while (sc.moveToNext()) {
            id = sc.getLong(idColumn);
            if (sc.isNull(scoreColumn)) {
                score = null;
            } else {
                score = sc.getLong(scoreColumn);
                total += score;
            }

            if (!sc.isNull(contextColumn)) {
                tmpContext = sc.getString(contextColumn);
                scoreContext = tmpContext;
            }

            created = sc.getLong(createdColumn);
            scores.add(new ScoreData(id, score, tmpContext, created));
        }

        pc.close();
        sc.close();
    }

    public long addScore(long score) {
        return addScoreAndContext(new ScoreData(score, null));
    }

    public void addContext(String context) {
        addScoreAndContext(new ScoreData(null, context));
    }

    public ScoreData getLastScore() {
        return scores.get(scores.size() - 1);
    }

    public long addScoreAndContext(ScoreData sd) {
        // insert to the DB
        ContentValues values = new ContentValues();
        values.put(Score.COLUMN_NAME_GAME_ID, game.id);
        values.put(Score.COLUMN_NAME_PLAYER_ID, id);
        if (sd.score == null && sd.context == null) {
            Log.d(DEBUG_TAG, "Score and Context are null, WTH?");
            return total;
        }
        if (sd.score != null) {
            values.put(Score.COLUMN_NAME_SCORE, sd.score);
            total += sd.score;
        }
        if (sd.context != null && !sd.context.isEmpty()) {
            values.put(Score.COLUMN_NAME_CONTEXT, sd.context);
            scoreContext = sd.context;
        }
        Long now = System.currentTimeMillis();
        values.put(Score.COLUMN_NAME_CREATE_DATE, now);

        Uri uri = appContext.getContentResolver().insert(Score.CONTENT_URI, values);
        // add to history
        scores.add(new ScoreData(ContentUris.parseId(uri), sd.score, sd.context, now));
        // add to total

        appContext.getContentResolver().notifyChange(uri, null);
        game.notifyDataSetChanged();

        return total;
    }

    public long getTotal() {
        return total;
    }

    public String getCurrentContext() {
        return scoreContext;
    }

    public void onClick(View v) {
        int vid = v.getId();
        switch (vid) {
            case R.id.badge_add:
                (new AddScoreDialog(v.getContext(), this)).show();
                return;
            case R.id.badge_name:
                Context context = v.getContext();
                Log.d(DEBUG_TAG, "Score History button clicked.");
                final AlertDialog.Builder alert = new AlertDialog.Builder(context);

                HistoryAdapter ha = new HistoryAdapter(context, scores);

                // add a column header.
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                alert.setCustomTitle(inflater.inflate(R.layout.score_history_header, null));

                alert.setAdapter(ha, this);
                alert.show();

        }
    }

    public void onClick(DialogInterface dialog, int which) {
        // TODO Auto-generated method stub, from history list

    }

    public void resetScore() {
        ContentResolver cr = appContext.getContentResolver();
        String where = Score.COLUMN_NAME_GAME_ID + "=" + game.id + " AND " + Score.COLUMN_NAME_PLAYER_ID + "=" + id;
        total = game.starting_score;
        scoreContext = "";
        scores.clear();
        cr.delete(Score.CONTENT_URI, where, null);
        // must notify after complete
    }
}

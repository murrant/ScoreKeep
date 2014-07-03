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
package com.splashmobileproductions.scorekeep.controller;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.splashmobileproductions.scorekeep.R;
import com.splashmobileproductions.scorekeep.provider.Game;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;

public class GameAdapter extends CursorAdapter {
    final PrettyTime mPrettyTime = new PrettyTime();

    public GameAdapter(Context context, Cursor c) {
        super(context, c, 0);
        // TODO Auto-generated constructor stub
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
        item.setText("Player list unsupported.");
    }

    private void setModifiedDate(TextView item, Cursor cursor) {
        Date playedDate = new Date(cursor.getLong(cursor.getColumnIndex(Game.COLUMN_NAME_MODIFICATION_DATE)));
        item.setText(mPrettyTime.format(playedDate));
    }
}

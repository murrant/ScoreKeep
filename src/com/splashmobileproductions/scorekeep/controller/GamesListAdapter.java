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

import java.text.DateFormat;
import java.util.Date;

import com.splashmobileproductions.scorekeep.R;
import com.splashmobileproductions.scorekeep.provider.Game;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GamesListAdapter extends CursorAdapter {

	public GamesListAdapter(Context context, Cursor c) {
		super(context, c);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		final LayoutInflater inflater = LayoutInflater.from(context);
		LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.games_list_item, parent, false);
		
		setDescription((TextView) ll.getChildAt(0), cursor);
		setCreationDate((TextView) ll.getChildAt(1), cursor);
		setModifiedDate((TextView) ll.getChildAt(2), cursor);
		
		return ll;
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		LinearLayout ll = (LinearLayout) view;
		setDescription((TextView) ll.getChildAt(0), cursor);
		setCreationDate((TextView) ll.getChildAt(1), cursor);
		setModifiedDate((TextView) ll.getChildAt(2), cursor);
	}
	
	private void setDescription(TextView item, Cursor cursor) {
		item.setText(cursor.getString(cursor.getColumnIndex(Game.COLUMN_NAME_DESCRIPTION)));		
	}

	private void setCreationDate(TextView item, Cursor cursor) {
		Date startedDate = new Date(cursor.getLong(cursor.getColumnIndex(Game.COLUMN_NAME_CREATE_DATE)));
		String startedString = DateFormat.getDateInstance().format(startedDate);
		item.setText(startedString);		
	}

	private void setModifiedDate(TextView item, Cursor cursor) {
		Date playedDate = new Date(cursor.getLong(cursor.getColumnIndex(Game.COLUMN_NAME_MODIFICATION_DATE)));
		DateFormat dt = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT);
		item.setText(dt.format(playedDate));		
	}
}

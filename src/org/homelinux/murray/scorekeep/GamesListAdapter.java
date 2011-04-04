package org.homelinux.murray.scorekeep;

import java.text.DateFormat;
import java.util.Date;

import org.homelinux.murray.scorekeep.provider.Game;

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

package org.homelinux.murray.scorekeep;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.CursorAdapter;

public class PlayerAdapter extends CursorAdapter {
	public PlayerAdapter(Context context, Cursor c) {
		super(context, c);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		final LayoutInflater inflater = LayoutInflater.from(context);
		CheckBox v = (CheckBox) inflater.inflate(R.layout.player_item, parent, false);
		v.setText(cursor.getString(cursor.getColumnIndex(DbHelper.KEY_NAME)));
		//v.setBackgroundColor(cursor.getInt(cursor.getColumnIndex(DbHelper.KEY_COLOR)));
		return v;
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		CheckBox v = (CheckBox) view;
		v.setText(cursor.getString(cursor.getColumnIndex(DbHelper.KEY_NAME)));
		//v.setBackgroundColor(cursor.getInt(cursor.getColumnIndex(DbHelper.KEY_COLOR)));
	}
}

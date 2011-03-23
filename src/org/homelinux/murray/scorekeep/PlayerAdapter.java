package org.homelinux.murray.scorekeep;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class PlayerAdapter extends CursorAdapter {
	public PlayerAdapter(Context context, Cursor c) {
		super(context, c, CursorAdapter.FLAG_AUTO_REQUERY&CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		final LayoutInflater inflater = LayoutInflater.from(context);
		TextView v = (TextView) inflater.inflate(R.layout.player_item, parent, false);
		v.setText(cursor.getString(cursor.getColumnIndex(DbHelper.KEY_NAME)));
		v.setBackgroundColor(cursor.getInt(cursor.getColumnIndex(DbHelper.KEY_COLOR)));
		return v;
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView v = (TextView) view;
		v.setText(cursor.getString(cursor.getColumnIndex(DbHelper.KEY_NAME)));
		v.setBackgroundColor(cursor.getInt(cursor.getColumnIndex(DbHelper.KEY_COLOR)));
	}
}

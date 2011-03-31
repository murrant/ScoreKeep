package org.homelinux.murray.scorekeep;

import org.homelinux.murray.scorekeep.provider.Score;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class PlayerScoresAdapter extends CursorAdapter {
    public PlayerScoresAdapter(Context context, Cursor c) {
		super(context, c);
		// TODO Auto-generated constructor stub
	}

	//private final static String[] FROM = new String[]{Score.COLUMN_NAME_SCORE};
    //private final static int[] TO = new int[]{android.R.id.text1};

	//public PlayerScoresAdapter(Context context, Cursor c) {
	//	super(context, android.R.layout.simple_list_item_1, c, FROM, TO);
	//}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		long score = cursor.getLong(cursor.getColumnIndex(Score.COLUMN_NAME_SCORE));
		((TextView) view).setText(Long.toString(score));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		final LayoutInflater inflater = LayoutInflater.from(context);
		TextView v = (TextView) inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
		long score = cursor.getLong(cursor.getColumnIndex(Score.COLUMN_NAME_SCORE));
		v.setText(Long.toString(score));
		return v;
	}
}

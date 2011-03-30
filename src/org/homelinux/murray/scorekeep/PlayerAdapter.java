package org.homelinux.murray.scorekeep;

import org.homelinux.murray.scorekeep.provider.Player;

import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;

public class PlayerAdapter extends SimpleCursorAdapter {
    private final static String[] FROM = new String[]{Player.COLUMN_NAME_NAME};
    private final static int[] TO = new int[]{android.R.id.text1};

	public PlayerAdapter(Context context, Cursor c) {
		super(context, android.R.layout.simple_list_item_multiple_choice, c, FROM, TO);
	}
}

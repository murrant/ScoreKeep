package org.homelinux.murray.scorekeep;

import org.homelinux.murray.scorekeep.provider.Player;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class PlayerList extends ListActivity {
    private final static String[] FROM = new String[]{Player.COLUMN_NAME_NAME};
    private final static int[] TO = new int[]{android.R.id.text1};

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.players_list);
        Cursor cursor = managedQuery(Player.CONTENT_URI, null, null, null, null);
        ListView list = getListView();
        list.setAdapter(new SimpleCursorAdapter(this, android.R.layout.simple_list_item_multiple_choice, cursor, FROM, TO));
        list.setItemsCanFocus(false);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }
}

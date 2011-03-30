package org.homelinux.murray.scorekeep;

import org.homelinux.murray.scorekeep.provider.Player;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;

public class PlayerList extends ListActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.players_list);
        Cursor cursor = managedQuery(Player.CONTENT_URI, null, null, null, null);
        ListView list = getListView();
        list.setAdapter(new PlayerAdapter(this, cursor));
        list.setItemsCanFocus(false);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }
}

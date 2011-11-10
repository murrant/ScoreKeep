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
package com.splashmobileproductions.scorekeep;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.splashmobileproductions.scorekeep.provider.Player;

public class PlayerList extends ListFragment {
	private final static String[] FROM = new String[]{Player.COLUMN_NAME_NAME};
	private final static int[] TO = new int[]{android.R.id.text1};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		Cursor cursor = getActivity().managedQuery(Player.CONTENT_URI, null, null, null, null);
		setListAdapter(new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_multiple_choice, cursor, FROM, TO));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.players_list, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		ListView list = getListView();
		list.setItemsCanFocus(false);
		list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.player_list_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {		
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.plmenu_new:
			final Intent addPlayerIntent = new Intent(getActivity(), AddPlayerDialog.class);
			startActivity(addPlayerIntent);
			return true;
		case R.id.plmenu_clear:
			//put up big warning, then nuke all players and all games
			return false;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}

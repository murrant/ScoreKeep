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

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;

import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.splashmobileproductions.scorekeep.provider.Game;
import com.splashmobileproductions.scorekeep.provider.Player;
import com.splashmobileproductions.scorekeep.provider.Score;

public class PlayerList extends SherlockListFragment implements OnClickListener {
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
			//TODO: combine with code in NewGameFragment and move to a common location
			final AlertDialog.Builder alert = new AlertDialog.Builder(this.getActivity());
			alert.setTitle(R.string.new_player);
			final EditText input = new EditText(this.getActivity());
			input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
			alert.setView(input);
			alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					String name = input.getText().toString().trim();
					// add the player to the Content Provider
					ContentValues content = new ContentValues();
					content.put(Player.COLUMN_NAME_NAME, name);
					PlayerList.this.getActivity().getContentResolver().insert(Player.CONTENT_URI, content);

				}
			});
			alert.setNegativeButton(android.R.string.cancel,
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.cancel();
				}
			});
			alert.show();
			return true;
		case R.id.plmenu_clear:
			//put up big warning, then nuke all players and all games
			DialogFragment df = Utility.createYesNoDialog(this, R.string.dialog_delete_everything);
			df.show(getFragmentManager(), "dialog");
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if(DialogInterface.BUTTON_POSITIVE == which) {
			ContentResolver cr = getActivity().getContentResolver();
			int dgms = cr.delete(Game.CONTENT_URI, null, null);
			cr.delete(Score.CONTENT_URI, null, null);
			int dpls = cr.delete(Player.CONTENT_URI, null, null);
			if(dpls>0) {
				Toast.makeText(getActivity().getApplicationContext(), dpls+" players and "+dgms+" games deleted!", Toast.LENGTH_SHORT).show();
			}
		}
	}
}

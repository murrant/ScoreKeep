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

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.splashmobileproductions.scorekeep.provider.Game;


public class GameHistory extends ListFragment {
	@SuppressWarnings("unused")
	private static final String DEBUG_TAG = "ScoreKeep:GameHistory";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		/* Android 3.0...
		CursorLoader loader = new CursorLoader(this, Game.CONTENT_URI, null, null, null, null);
		Cursor cursor = loader.loadInBackground();
		 */

		Cursor cursor = getActivity().managedQuery(Game.CONTENT_URI, null, null, null, null);
		GamesListAdapter gla = new GamesListAdapter(getActivity(),cursor);
		setListAdapter(gla);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.games_list, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// Open games on click
		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {  
			public void onItemClick(AdapterView<?> listView, View item, int position, long id) {
				Intent intent = new Intent(listView.getContext(), ScoreCard.class);
				Uri gameUri = ContentUris.withAppendedId(Game.CONTENT_ID_URI_BASE, id);
				intent.setData(gameUri);  //set data uri for the new game
				startActivity(intent);
				
				//TODO don't like this...  would almost rather leave it on the stack.
				//clear the back stack
				((FragmentActivity) getActivity()).getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
			}
		});
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.games_list_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {		
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.glmenu_clear_history:
			int rows = getActivity().getContentResolver().delete(Game.CONTENT_URI, null, null);
			if(rows>0) {
				Toast.makeText(getActivity().getApplicationContext(), rows+" games deleted!", Toast.LENGTH_SHORT).show();
				return true;
			}
			return false;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
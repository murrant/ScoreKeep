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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import com.splashmobileproductions.scorekeep.provider.Player;

public class AddPlayerDialog extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		final AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(R.string.new_player);
		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
		alert.setView(input);
		alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString().trim();
				addPlayer(value);
				finish();
			}
		});
		alert.setNegativeButton(android.R.string.cancel,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.cancel();
				finish();
			}
		});
		alert.show();
	}

	private Uri addPlayer(String name) {
		// add the player to the Content Provider
		ContentValues content = new ContentValues();
		content.put(Player.COLUMN_NAME_NAME, name);
		return getContentResolver().insert(Player.CONTENT_URI, content);		
	}
}
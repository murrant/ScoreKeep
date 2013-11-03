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

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

// TODO use PreferenceFragment if possible
public class SettingsFragment extends Fragment {
	public static final String PREFS_NAME = "ScoreKeepSettings";

	private CheckBox chkKeepScreenOn = null;
	public static final String KEY_SCREEN_ON = "keepScreenOn";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.act_settings, container, false);

		chkKeepScreenOn = (CheckBox) view.findViewById(R.id.settings_keepscreenon);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();

		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
		chkKeepScreenOn.setChecked(settings.getBoolean(KEY_SCREEN_ON, false));
	}


	@Override
	public void onPause() {
		super.onPause();

		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
		SharedPreferences.Editor editor = settings.edit();

		if (chkKeepScreenOn != null) {
			editor.putBoolean(KEY_SCREEN_ON, chkKeepScreenOn.isChecked());
		}

		// Commit the edits!
		editor.commit();
	}
}

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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;

public class Settings extends Activity {
    public static final String PREFS_NAME = "ScoreKeepSettings";

    private CheckBox chkKeepScreenOn = null;
    public static final String KEY_SCREEN_ON = "keepScreenOn";

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.act_settings);
	chkKeepScreenOn = (CheckBox) findViewById(R.id.settings_keepscreenon);

	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	chkKeepScreenOn.setChecked(settings.getBoolean(KEY_SCREEN_ON, false));

    }

    @Override
    protected void onStop() {
	super.onStop();

	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	SharedPreferences.Editor editor = settings.edit();

	if (chkKeepScreenOn != null) {
	    editor.putBoolean(KEY_SCREEN_ON, chkKeepScreenOn.isChecked());
	}

	// Commit the edits!
	editor.commit();
    }
}

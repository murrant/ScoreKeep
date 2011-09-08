/**
 * 
 */
package org.homelinux.murray.scorekeep;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;

/**
 * @author murrant
 *
 */
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

/**
 * 
 */
package org.homelinux.murray.scorekeep;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/**
 * @author murrant
 *
 */
public class HomeActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.dashboard);

	// Attach event handlers
	findViewById(R.id.home_btn_new).setOnClickListener(new View.OnClickListener() {
	    public void onClick(View view) {
		startActivity(new Intent(HomeActivity.this, NewGame.class));
	    }
	});


	findViewById(R.id.home_btn_history).setOnClickListener(new View.OnClickListener() {
	    public void onClick(View view) {
		startActivity(new Intent(HomeActivity.this, GameHistory.class));
	    }
	});


	findViewById(R.id.home_btn_players).setOnClickListener(new View.OnClickListener() {
	    public void onClick(View view) {
		startActivity(new Intent(HomeActivity.this, PlayerList.class));
	    }
	});

	findViewById(R.id.home_btn_settings).setOnClickListener(new View.OnClickListener() {
	    public void onClick(View view) {
		startActivity(new Intent(HomeActivity.this, Settings.class));
	    }
	});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.home_menu, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {		
	// Handle item selection
	switch (item.getItemId()) {
	case R.id.home_menu_new:
	    startActivity(new Intent(this, NewGame.class));
	    return true;
	case R.id.home_menu_about:
	    //show about dialog
	    try {
		AboutDialogBuilder.create(this).show();
	    } catch (NameNotFoundException nnfe) {}
	default:
	    return super.onOptionsItemSelected(item);
	}
    }
    
    public static class AboutDialogBuilder {
	    public static AlertDialog create( Context context ) throws NameNotFoundException {
	        // Try to load the a package matching the name of our own package
	        PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
	        String versionInfo = pInfo.versionName;
	 
	        String aboutTitle = String.format("About %s", context.getString(R.string.app_name));
	        String versionString = String.format("Version: %s", versionInfo);
	        String aboutText = context.getString(R.string.about_dialog_text);
	 
	        // Set up the TextView
	        final TextView message = new TextView(context);
	        // We'll use a spannablestring to be able to make links clickable
	        final SpannableString s = new SpannableString(aboutText);
	 
	        // Set some padding
	        message.setPadding(5, 5, 5, 5);
	        // Set up the final string
	        message.setText(versionString + "\n\n" + s);
	        // Now linkify the text
	        Linkify.addLinks(message, Linkify.ALL);
	 
	        return new AlertDialog.Builder(context).setTitle(aboutTitle).setCancelable(true).setIcon(R.drawable.icon).setPositiveButton(
	             context.getString(android.R.string.ok), null).setView(message).create();
	    }
	}

}

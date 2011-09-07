/**
 * 
 */
package org.homelinux.murray.scorekeep;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
    }

}

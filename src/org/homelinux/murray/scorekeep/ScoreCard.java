package org.homelinux.murray.scorekeep;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;


public class ScoreCard extends Activity {
	//ArrayList<Player> players=new ArrayList<Player>();
	DbHelper dbh;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_card);
        
        Bundle bundle = getIntent().getExtras();
        //Next extract the values using the key as
        long gameId = bundle.getLong("GameID");
        
        dbh = new DbHelper(this);
        Cursor game = dbh.getGame(gameId);
        game.getCount();
    }
    /*
    @Override
    public void onDestroy() {
    	this.onDestroy();
    	dbh.closeDb();
    }
    */
}
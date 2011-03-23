package org.homelinux.murray.scorekeep;

import java.util.ArrayList;
import org.homelinux.murray.scorekeep.R;

import android.app.Activity;
import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;

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

    }
    /*
    @Override
    public void onDestroy() {
    	this.onDestroy();
    	dbh.closeDb();
    }
    */
}
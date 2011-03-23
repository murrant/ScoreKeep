package org.homelinux.murray.scorekeep;

import java.util.ArrayList;
import org.homelinux.murray.scorekeep.R;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;

public class ScoreCard extends ListActivity {
	ArrayList<Player> players=new ArrayList<Player>();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keep_score);
        
        Bundle bundle = getIntent().getExtras();
        //Next extract the values using the key as
        //int game_id = bundle.getInt("GameID");
        int player_count = bundle.getInt("player_count");
        for (int n = 0; n<player_count; n++) {
        	players.add(new Player(this, bundle.getString("player"+n)));
        }
        
        
        /*
        final TableLayout layout = (TableLayout) findViewById(R.id.score_keep_layout);
        TableRow titleRow = new TableRow(this);
        layout.addView(titleRow);
        TableRow totalRow = new TableRow(this);
        layout.addView(totalRow);
        TableRow scoreRow = new TableRow(this);
        layout.addView(scoreRow);
        TableRow entryRow = new TableRow(this);
        layout.addView(entryRow);
        
        for (Player player : players) {
        	TextView nameTitle = new TextView(this);
        	nameTitle.setText(player.name);
        	titleRow.addView(nameTitle);
        	
        	player.totalTextView.setText(Integer.toString(player.scoreTotal));
        	totalRow.addView(player.totalTextView);
        	
        	ListView listView = new ListView(this);
        	TextView testa = new TextView(this);
        	testa.setText("4");
        	listView.addView(testa);
        	TextView testb = new TextView(this);
        	testb.setText("32");
        	listView.addView(testb);
        	scoreRow.addView(listView);
        	/*
            scoreRow.addView(player.scoreList);
            player.addScore(4); //test data
            player.addScore(32);
            
            final TextView scoreEntry = new TextView(this);
            Button addButton = new Button(this);
            addButton.setText("Add Score");
            final Player playerRef = player;
            // Create a message handling object as an anonymous class.
            OnClickListener mMessageClickedHandler = new OnClickListener() {
                public void onClick(View v)
                {
                	playerRef.addFormula(scoreEntry.toString());
                }
            };
            addButton.setOnClickListener(mMessageClickedHandler);
            
            
        }
        */
    }
    
}
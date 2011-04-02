package org.homelinux.murray.scorekeep;

import java.util.Random;

import org.homelinux.murray.scorekeep.provider.Game;
import org.homelinux.murray.scorekeep.provider.Player;
import org.homelinux.murray.scorekeep.provider.Score;
import org.homelinux.murray.scorekeep.provider.ScoresProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class ScoreCard extends Activity {
	private static final String DEBUG_TAG = "ScoreKeep:ScoreCard";
	private GameData game;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.score_card);
		Uri dataUri = getIntent().getData();
		// If there is no data associated with the Intent, bring up new game dialog
		if (dataUri == null) {
			startActivity(new Intent(this, NewGame.class));
			return;
		}

		// load the game data
		game = new GameData(this, dataUri);
		
		GridView grid = (GridView) findViewById(R.id.score_card_grid);
		TextView desc = (TextView) findViewById(R.id.game_desc_title);
		desc.setText(game.description);
		
		grid.setAdapter(game);
	}
}
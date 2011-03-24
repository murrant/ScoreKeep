package org.homelinux.murray.scorekeep;

import java.util.Date;
import java.util.Random;

import org.homelinux.murray.scorekeep.provider.ScoresProvider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.util.Log;

public class DbHelper {
	private static final String DB_NAME = "games.db";
	private static final int DB_VERSION = 1;

	public static final String TABLE_GAMES = "games";
	public static final String TABLE_PLAYERS = "players";
	public static final String TABLE_SCORES = "scores";

	public static final String _ID = "_id";
	public static final String COLUMN_NAME_DESCRIPTION = "description";
	public static final String COLUMN_NAME_PLAYER_IDS = "player_ids";
	public static final String COLUMN_NAME_CREATION_DATE = "creation_date";
	public static final String COLUMN_NAME_MODIFIED_DATE = "modified_date";
	public static final String COLUMN_NAME_NAME = "name";
	public static final String COLUMN_NAME_COLOR = "color";
	public static final String COLUMN_NAME_GAME_ID = "game_id";
	public static final String COLUMN_NAME_PLAYER_ID = "player_id";
	public static final String COLUMN_NAME_SCORE = "score";
	
	private SQLiteDatabase db;
	private String gameString;

	public DbHelper(Context context) {
		OpenHelper openHelper = new OpenHelper(context);
		db = openHelper.getWritableDatabase();
		gameString = context.getString(R.string.game);
	}
	
	public void closeDb() {
		db.close();
	}

	// create a new player, returns id
	public long newPlayer(String name) {
		ContentValues playerValues = new ContentValues();
		playerValues.put(COLUMN_NAME_NAME, name);
		Random rand = new Random();
		playerValues.put(COLUMN_NAME_COLOR, Color.rgb(rand.nextInt(256),rand.nextInt(256),rand.nextInt(256)));
		return db.insert(TABLE_PLAYERS, null, playerValues);
	}
	
	public long newGame(String description, long[] players) {
		ContentValues gameValues = new ContentValues();
		if(description.isEmpty()) {
			gameValues.put(COLUMN_NAME_DESCRIPTION, gameString);
		} else {
			gameValues.put(COLUMN_NAME_DESCRIPTION, description);
		}
		gameValues.put(COLUMN_NAME_PLAYER_IDS, ScoresProvider.serializePlayers(players));
		long now = (new Date()).getTime();
		gameValues.put(COLUMN_NAME_CREATION_DATE, now);
		gameValues.put(COLUMN_NAME_MODIFIED_DATE, now);
		return db.insert(TABLE_GAMES, null, gameValues);
	}
	
	public long newScore(int gameId, int playerId, int score) {
        ContentValues scoreValues = new ContentValues();
        scoreValues.put(COLUMN_NAME_GAME_ID, gameId);
        scoreValues.put(COLUMN_NAME_PLAYER_ID, playerId);
        scoreValues.put(COLUMN_NAME_SCORE, score);
		return db.insert(TABLE_SCORES, null, scoreValues);
	}
	
	// get all players
	public Cursor getPlayers() {
		String[] columns = new String[]{_ID,COLUMN_NAME_NAME,COLUMN_NAME_COLOR};
		return db.query(TABLE_PLAYERS, columns, null, null, null, null, COLUMN_NAME_NAME);
	}
	
	public Cursor getGamesList() {
		String[] columns = new String[]{_ID,COLUMN_NAME_DESCRIPTION,COLUMN_NAME_CREATION_DATE, COLUMN_NAME_MODIFIED_DATE};
		return db.query(TABLE_GAMES, columns, null, null, null, null, null);
	}
	
	// Get all data for a game
	public Cursor getScores(int gameId, int playerId) {
		String[] columns = new String[]{_ID,COLUMN_NAME_SCORE};
		String selection = COLUMN_NAME_GAME_ID + "=" + gameId + " AND " + COLUMN_NAME_PLAYER_ID + "=" + playerId;
		return db.query(TABLE_SCORES, columns, selection, null, null, null, _ID);
	}
	
	// get a player with no scores
	public Cursor getPlayer(int playerId) {
		String[] columns = new String[]{_ID,COLUMN_NAME_NAME,COLUMN_NAME_COLOR};
		String selection = _ID + "=" + playerId;
		return db.query(TABLE_PLAYERS, columns, selection, null, null, null, null);
	}
	
	// get a players from a game with no scores
	public Cursor getPlayers(int gameId) {
		// grab the string of players, this is stored as a comma seperated list of nums
		Cursor pc = db.query(TABLE_GAMES, new String[]{COLUMN_NAME_PLAYER_IDS}, _ID + "=" + gameId, null, null, null, null);
		
		String[] columns = new String[]{_ID,COLUMN_NAME_NAME,COLUMN_NAME_COLOR};
		String selection = _ID + " IN ( " + pc.getString(pc.getColumnIndex(COLUMN_NAME_PLAYER_IDS)) + " )";
		return db.query(TABLE_PLAYERS, columns, selection, null, null, null, null);
	}
	
	// get a player and load saved scores from game
	public Cursor getPlayerWithScores(int playerId, int gameId) {
		return null;
	}

	public Cursor getGame(long gameId) {
		return null;
	}

	public void deleteAll() {
		db.delete(TABLE_GAMES, null, null);
		db.delete(TABLE_PLAYERS, null, null);
		db.delete(TABLE_SCORES, null, null);
	}
	

	
	// Open helper to load/create DB.
	private static class OpenHelper extends SQLiteOpenHelper {
		OpenHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("create table " + TABLE_GAMES + " (" + 
					_ID + " integer primary key autoincrement, " + COLUMN_NAME_DESCRIPTION + " text not null, " + 
					COLUMN_NAME_PLAYER_IDS + " text not null, " + COLUMN_NAME_CREATION_DATE + " integer not null, " +
					COLUMN_NAME_MODIFIED_DATE + " integer not null)");
			db.execSQL("create table " + TABLE_PLAYERS + " (" + 
					_ID + " integer primary key autoincrement, " + COLUMN_NAME_NAME + " text not null, " + 
					COLUMN_NAME_COLOR + " text)");
			db.execSQL("create table " + TABLE_SCORES + " (" + 
					_ID + " integer primary key autoincrement, " + COLUMN_NAME_GAME_ID + " integer not null, " +
					COLUMN_NAME_PLAYER_ID + " integer not null, " + COLUMN_NAME_SCORE + " integer not null)");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w("ScoreKeep", "Upgrading database, this will drop tables and recreate.");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAMES);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYERS);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORES);
			onCreate(db);
		}
	}
}

package org.homelinux.murray.scorekeep;

import java.util.Date;
import java.util.Random;
import java.util.StringTokenizer;

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

	public static final String KEY_ROW_ID = "_id";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_PLAYER_IDS = "player_ids";
	public static final String KEY_CREATION_DATE = "creation_date";
	public static final String KEY_MODIFIED_DATE = "modified_date";
	public static final String KEY_NAME = "name";
	public static final String KEY_COLOR = "color";
	public static final String KEY_GAME_ID = "game_id";
	public static final String KEY_PLAYER_ID = "player_id";
	public static final String KEY_SCORE = "score";
	
	private static final String DELIMITER = ",";
	
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
		playerValues.put(KEY_NAME, name);
		Random rand = new Random();
		playerValues.put(KEY_COLOR, Color.rgb(rand.nextInt(256),rand.nextInt(256),rand.nextInt(256)));
		return db.insert(TABLE_PLAYERS, null, playerValues);
	}
	
	public long newGame(String description, long[] players) {
		ContentValues gameValues = new ContentValues();
		if(description.isEmpty()) {
			gameValues.put(KEY_DESCRIPTION, gameString);
		} else {
			gameValues.put(KEY_DESCRIPTION, description);
		}
		gameValues.put(KEY_PLAYER_IDS, serialize(players));
		long now = (new Date()).getTime();
		gameValues.put(KEY_CREATION_DATE, now);
		gameValues.put(KEY_MODIFIED_DATE, now);
		return db.insert(TABLE_GAMES, null, gameValues);
	}
	
	public long newScore(int gameId, int playerId, int score) {
        ContentValues scoreValues = new ContentValues();
        scoreValues.put(KEY_GAME_ID, gameId);
        scoreValues.put(KEY_PLAYER_ID, playerId);
        scoreValues.put(KEY_SCORE, score);
		return db.insert(TABLE_SCORES, null, scoreValues);
	}
	
	// get all players
	public Cursor getPlayers() {
		String[] columns = new String[]{KEY_ROW_ID,KEY_NAME,KEY_COLOR};
		return db.query(TABLE_PLAYERS, columns, null, null, null, null, KEY_NAME);
	}
	
	public Cursor getGamesList() {
		String[] columns = new String[]{KEY_ROW_ID,KEY_DESCRIPTION,KEY_CREATION_DATE, KEY_MODIFIED_DATE};
		return db.query(TABLE_GAMES, columns, null, null, null, null, null);
	}
	
	// Get all data for a game
	public Cursor getScores(int gameId, int playerId) {
		String[] columns = new String[]{KEY_ROW_ID,KEY_SCORE};
		String selection = KEY_GAME_ID + "=" + gameId + " AND " + KEY_PLAYER_ID + "=" + playerId;
		return db.query(TABLE_SCORES, columns, selection, null, null, null, KEY_ROW_ID);
	}
	
	// get a player with no scores
	public Cursor getPlayer(int playerId) {
		String[] columns = new String[]{KEY_ROW_ID,KEY_NAME,KEY_COLOR};
		String selection = KEY_ROW_ID + "=" + playerId;
		return db.query(TABLE_PLAYERS, columns, selection, null, null, null, null);
	}
	
	// get a players from a game with no scores
	public Cursor getPlayers(int gameId) {
		// grab the string of players, this is stored as a comma seperated list of nums
		Cursor pc = db.query(TABLE_GAMES, new String[]{KEY_PLAYER_IDS}, KEY_ROW_ID + "=" + gameId, null, null, null, null);
		
		String[] columns = new String[]{KEY_ROW_ID,KEY_NAME,KEY_COLOR};
		String selection = KEY_ROW_ID + " IN ( " + pc.getString(pc.getColumnIndex(KEY_PLAYER_IDS)) + " )";
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
	
	// ghetto serialize
	private String serialize(long[] inputArray) {
		StringBuffer sb = new StringBuffer();
		for ( long num : inputArray ) {
			sb.append(num);
			sb.append(DELIMITER);
		}
		return sb.toString();
	}
	
	// ghetto deserialize
	@SuppressWarnings("unused")
	private long[] deserialize(String inputString) {
		StringTokenizer st = new StringTokenizer(inputString, DELIMITER);
		long[] array = new long[st.countTokens()];
		int n = 0;
		while (st.hasMoreTokens()) {
			array[n++] = Long.valueOf(st.nextToken());
		}
		return array;
	}
	
	// Open helper to load/create DB.
	private static class OpenHelper extends SQLiteOpenHelper {
		OpenHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("create table " + TABLE_GAMES + " (" + 
					KEY_ROW_ID + " integer primary key autoincrement, " + KEY_DESCRIPTION + " text not null, " + 
					KEY_PLAYER_IDS + " text not null, " + KEY_CREATION_DATE + " integer not null, " +
					KEY_MODIFIED_DATE + " integer not null)");
			db.execSQL("create table " + TABLE_PLAYERS + " (" + 
					KEY_ROW_ID + " integer primary key autoincrement, " + KEY_NAME + " text not null, " + 
					KEY_COLOR + " text)");
			db.execSQL("create table " + TABLE_SCORES + " (" + 
					KEY_ROW_ID + " integer primary key autoincrement, " + KEY_GAME_ID + " integer not null, " +
					KEY_PLAYER_ID + " integer not null, " + KEY_SCORE + " integer not null)");
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

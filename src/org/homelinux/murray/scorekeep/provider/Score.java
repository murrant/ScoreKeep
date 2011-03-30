package org.homelinux.murray.scorekeep.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class Score implements BaseColumns {
	//static only
	private Score() {}
	
    /**
     * The table name offered by this provider
     */
    public static final String TABLE_NAME = "scores";

    /*
     * URI definitions
     */

    /**
     * Path parts for the URIs
     */

    /**
     * Path part for the Scores URI
     */
    protected static final String PATH_SCORE = "game/player/score";

    /**
     * Path part for the Scores ID URI
     */
    protected static final String PATH_SCORE_ID = "game/player/score/";

    /**
     * The content:// style URL for this table
     */
    public static final Uri CONTENT_URI =  Uri.parse(ScoresProvider.SCHEME + ScoresProvider.AUTHORITY + "/" + PATH_SCORE);

    /**
     * The content URI base for a single score. Callers must
     * append a numeric note id to this Uri to retrieve a note
     */
    public static final Uri CONTENT_ID_URI_BASE = Uri.parse(ScoresProvider.SCHEME + ScoresProvider.AUTHORITY + "/" + PATH_SCORE_ID);

    /**
     * The content URI match pattern for a single score, specified by its ID. Use this to match
     * incoming URIs or to construct an Intent.
     */
    public static final Uri CONTENT_ID_URI_PATTERN
        = Uri.parse(ScoresProvider.SCHEME + ScoresProvider.AUTHORITY + "/" + PATH_SCORE_ID + "#");
    
    /**
     * 0-relative position of the ID segment in the path part of a ID URI
     */
    public static final int ID_PATH_POSITION = 3;


    /*
     * Column definitions
     */

    /**
     * Column name for the game id
     * <P>Type: INTEGER (long row id of the game)</P>
     */
	public static final String COLUMN_NAME_GAME_ID = "game_id";

    /**
     * Column name of the player id
     * <P>Type: INTEGER (long row id of the player)</P>
     */
	public static final String COLUMN_NAME_PLAYER_ID = "player_id";

    /**
     * Column name for the creation timestamp
     * <P>Type: INTEGER (long from System.curentTimeMillis())</P>
     */
    public static final String COLUMN_NAME_CREATE_DATE = "created";

    /**
     * Column name for the score
     * <P>Type: INTEGER (long of the score, may be negative)</P>
     */
	public static final String COLUMN_NAME_SCORE = "score";
    
    /*
     * MIME type definitions
     */

    /**
     * The MIME type of {@link #CONTENT_URI} providing a directory of scores.
     */
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.scorekeep.score";

    /**
     * The MIME type of a {@link #CONTENT_URI} sub-directory of a single score.
     */
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.scorekeep.score";

    /**
     * The default sort order for this table, oldest scores on top
     */
    public static final String DEFAULT_SORT_ORDER = COLUMN_NAME_CREATE_DATE+" DESC";
}

/**
 *  Copyright 2011 Tony Murray <murraytony@gmail.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.splashmobileproductions.scorekeep.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class Player implements BaseColumns {
	//static only
	private Player() {}
	
    /**
     * The table name offered by this provider
     */
    public static final String TABLE_NAME = "players";

    /*
     * URI definitions
     */

    /**
     * Path parts for the URIs
     */

    /**
     * Path part for the Scores URI
     */
    protected static final String PATH_PLAYER = "game/player";

    /**
     * Path part for the Scores ID URI
     */
    protected static final String PATH_PLAYER_ID = "game/player/";

    /**
     * The content:// style URL for this table
     */
    public static final Uri CONTENT_URI =  Uri.parse(ScoresProvider.SCHEME + ScoresProvider.AUTHORITY + "/" + PATH_PLAYER);

    /**
     * The content URI base for a single player. Callers must
     * append a numeric id
     */
    public static final Uri CONTENT_ID_URI_BASE = Uri.parse(ScoresProvider.SCHEME + ScoresProvider.AUTHORITY + "/" + PATH_PLAYER_ID);

    /**
     * The content URI match pattern for a single player, specified by its ID. Use this to match
     * incoming URIs or to construct an Intent.
     */
    public static final Uri CONTENT_ID_URI_PATTERN
        = Uri.parse(ScoresProvider.SCHEME + ScoresProvider.AUTHORITY + "/" + PATH_PLAYER_ID + "#");
    
    /**
     * 0-relative position of the ID segment in the path part of a ID URI
     */
    public static final int ID_PATH_POSITION = 2;

    /*
     * Column definitions
     */

    /**
     * Column name for the player's name
     * <P>Type: TEXT</P>
     */
	public static final String COLUMN_NAME_NAME = "name";

    /**
     * Column name of the player's color
     * <P>Type: INTEGER (long of color)</P>
     */
	public static final String COLUMN_NAME_COLOR = "color";
	
    /*
     * MIME type definitions
     */

    /**
     * The MIME type of {@link #CONTENT_URI} providing a directory of players.
     */
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.scorekeep.player";

    /**
     * The MIME type of a {@link #CONTENT_URI} sub-directory of a single player.
     */
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.scorekeep.player";

    /**
     * The default sort order for this table, alphabetically
     */
    public static final String DEFAULT_SORT_ORDER = COLUMN_NAME_NAME+" ASC";
}
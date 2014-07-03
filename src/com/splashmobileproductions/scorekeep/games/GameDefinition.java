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
package com.splashmobileproductions.scorekeep.games;

import android.app.Dialog;
import android.util.Log;
import android.view.View;

import com.splashmobileproductions.scorekeep.data.PlayerData;
import com.splashmobileproductions.scorekeep.data.ScoreData;

/**
 * Used to implement games.
 * An xml resource must be designed and set to the variable resource.
 * There are several id's you can assign to buttons to get generic functionality.
 *
 * @author Tony Murray <murraytony@gmail.com>
 */
public abstract class GameDefinition implements Comparable<GameDefinition>, View.OnClickListener {
    private static final String DEBUG_TAG = "ScoreKeep:GameDefinition";
    public final boolean enabled;
    /**
     * Must be defined and unique for each type.
     */
    private final int id;
    private final int resource;
    public String name;

    public GameDefinition(int id, String name, int resource, boolean enabled) {
        this.name = name;
        this.enabled = enabled;
        this.resource = resource;
        this.id = id;
    }

    protected static long parseScore(String mathExp) throws NumberFormatException, ArithmeticException {
        Long score = Long.getLong(mathExp);
        if (mathExp != null && !mathExp.trim().equals("") && score == null) {
            score = Math.round(new MathEval().evaluate(mathExp));
        }
        return score;
    }

    public String toString() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int compareTo(GameDefinition another) {
        return id - another.getGameId();
    }

    public int getDialogResource() {
        Log.d(DEBUG_TAG, "Using super class implementation of getDialogResource");
        return resource;
    }

    public int getGameId() {
        return id;
    }

    //TODO the dialog argument seems wrong

    /**
     * returns null if it could not validate the dialog, it should also display a toast with the problem
     */
    public abstract ScoreData getScore(Dialog dialog, PlayerData player);
}

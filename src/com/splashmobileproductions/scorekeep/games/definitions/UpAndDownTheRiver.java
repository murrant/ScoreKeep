/**
 *  Copyright 2011 Tony Murray <murraytony@gmail.com>
 */
package com.splashmobileproductions.scorekeep.games.definitions;

import android.app.Dialog;
import android.view.View;

import com.splashmobileproductions.scorekeep.R;
import com.splashmobileproductions.scorekeep.data.PlayerData;
import com.splashmobileproductions.scorekeep.data.ScoreData;
import com.splashmobileproductions.scorekeep.games.GameDefinition;
import com.splashmobileproductions.scorekeep.games.GameDefs;

public class UpAndDownTheRiver extends GameDefinition {

    public UpAndDownTheRiver() {
        super(GameDefs.UP_AND_DOWN_THE_RIVER, "Up and Down the River", R.layout.add_score_generic, true);
    }

    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

    @Override
    public ScoreData getScore(Dialog dialog, PlayerData player) {
        // TODO Auto-generated method stub
        return null;
    }

}

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
package com.splashmobileproductions.scorekeep;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.splashmobileproductions.scorekeep.data.PlayerData;
import com.splashmobileproductions.scorekeep.data.ScoreData;
import com.splashmobileproductions.scorekeep.games.GameDefinition;
import com.splashmobileproductions.scorekeep.games.MathEval;

public class AddScoreDialog extends Dialog implements View.OnClickListener, TextView.OnEditorActionListener {
    private static final String DEBUG_TAG = "ScoreKeep:AddScoreDialog";
    private final TextView scoreEdit;
    private final TextView contextEdit;

    private final PlayerData player;
    private final GameDefinition gameDef;
    private final MathEval mathEval = new MathEval();

    public AddScoreDialog(Context parent, PlayerData playerData) {
        super(parent);
        player = playerData;
        gameDef = playerData.game.gameDef;
        Log.d(DEBUG_TAG, "GameDef is null:" + (gameDef == null));
        Log.d(DEBUG_TAG, "Resource id: " + gameDef.getDialogResource());
        setContentView(gameDef.getDialogResource());
        setTitle(playerData.name + " - " + parent.getText(R.string.add_score));

        //generic elements
        scoreEdit = (TextView) findViewById(R.id.score_edit);
        contextEdit = (TextView) findViewById(R.id.context_edit);
        Button minusFive = (Button) findViewById(R.id.minus_5);
        Button minusOne = (Button) findViewById(R.id.minus_1);
        Button plusOne = (Button) findViewById(R.id.plus_1);
        Button plusFive = (Button) findViewById(R.id.plus_5);
        Button ok = (Button) findViewById(R.id.ok);
        Button cancel = (Button) findViewById(R.id.cancel);

        if (scoreEdit != null) scoreEdit.setOnEditorActionListener(this);
        if (contextEdit != null) contextEdit.setOnEditorActionListener(this);

        if (minusFive != null) minusFive.setOnClickListener(this);
        if (minusOne != null) minusOne.setOnClickListener(this);
        if (plusOne != null) plusOne.setOnClickListener(this);
        if (plusFive != null) plusFive.setOnClickListener(this);

        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }


    public void onClick(View v) {
        Log.d(DEBUG_TAG, "View Class " + v.getClass().getName());
        ViewParent vp = v.getParent();
        while (vp != null) {
            Log.d(DEBUG_TAG, "ViewParent Class " + vp.getClass().getName());
            vp = vp.getParent();
        }

        int amount = 0;
        switch (v.getId()) {
            case R.id.minus_5:
                amount = -5;
                break;
            case R.id.minus_1:
                amount = -1;
                break;
            case R.id.plus_1:
                amount = 1;
                break;
            case R.id.plus_5:
                amount = 5;
                break;
            case R.id.ok:
                ScoreData score = gameDef.getScore(this, player);
                if (score == null) return;
                player.addScoreAndContext(score);
            case R.id.cancel:
                dismiss();
                return;
            default:
                // if not a built in function, defer to the game definition
                gameDef.onClick(v);
                return;
        }

        // handle built in add/subtract functions
        if (scoreEdit != null && amount != 0) {
            String startExp = scoreEdit.getText().toString();
            if (startExp.trim().isEmpty()) {
                scoreEdit.setText(Integer.toString(amount));
            } else {
                Long curNum = Long.getLong(startExp);
                if (curNum == null) {
                    try {
                        curNum = Math.round(mathEval.evaluate(scoreEdit.getText().toString()));
                    } catch (Exception e) {
                        Log.d(DEBUG_TAG, "Math Eval failed: " + e);
                        return;
                    }
                }
                scoreEdit.setText(Long.toString(curNum + amount));
            }
        }
    }


    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == KeyEvent.KEYCODE_ENTER) {
            ScoreData score = gameDef.getScore(this, player);
            if (score == null) return false;
            player.addScoreAndContext(score);
            return true;
        }
        return false;
    }
}

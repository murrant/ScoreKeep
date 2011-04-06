package org.homelinux.murray.scorekeep;

import org.homelinux.murray.scorekeep.games.GameDefinition;
import org.homelinux.murray.scorekeep.games.MathEval;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

public class AddScoreDialog extends Dialog implements View.OnClickListener,TextView.OnEditorActionListener {
	private static final String DEBUG_TAG = "ScoreKeep:AddScoreDialog";
	private final TextView scoreEdit;
	private final TextView contextEdit;

	private final PlayerData player;
	private final GameDefinition gameDef;
	private final MathEval mathEval = new MathEval();

	public AddScoreDialog(Context parent, PlayerData playerData) {
		super(parent);
		player = playerData;
		gameDef = playerData.game.game_type;
		//TODO support alternate dialogs
		setContentView(playerData.game.game_type.resource);
		setTitle(playerData.name+" - "+parent.getText(R.string.add_score));
		
		//generic elements
		scoreEdit = (TextView) findViewById(R.id.score_edit);
		contextEdit = (TextView) findViewById(R.id.context_edit);
		Button minusFive = (Button) findViewById(R.id.minus_5);
		Button minusOne = (Button) findViewById(R.id.minus_1);
		Button plusOne = (Button) findViewById(R.id.plus_1);
		Button plusFive = (Button) findViewById(R.id.plus_5);
		Button ok = (Button) findViewById(R.id.ok);
		Button cancel = (Button) findViewById(R.id.cancel);
		
		scoreEdit.setOnEditorActionListener(this);
		contextEdit.setOnEditorActionListener(this);
		
		minusFive.setOnClickListener(this);
		minusOne.setOnClickListener(this);
		plusOne.setOnClickListener(this);
		plusFive.setOnClickListener(this);
		
		ok.setOnClickListener(this);
		cancel.setOnClickListener(this);
	}


	public void onClick(View v) {
		int amount = 0;
		switch(v.getId()) {
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
			scoreIt();
			return;
		case R.id.cancel:
			dismiss();
			return;
		default:
			// if not a built in function, defer to the game definition
			gameDef.onClick(v);
			return;
		}
		
		// handle built in add/subtract functions
		if(amount != 0) {
			String startExp = scoreEdit.getText().toString();
			if(startExp.isEmpty()) {
				scoreEdit.setText(Integer.toString(amount));
			} else {
				Long curNum = Long.getLong(startExp);
				if(curNum == null) {
					try {
						curNum = Math.round(mathEval.evaluate(scoreEdit.getText().toString()));
					} catch(Exception e) {
						Log.d(DEBUG_TAG, "Math Eval failed: "+e);
						return;
					}
				}
				scoreEdit.setText(Long.toString(curNum + amount));
			}
			return;
		}
		
	}


	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if(actionId == EditorInfo.IME_ACTION_DONE || actionId == KeyEvent.KEYCODE_ENTER) {
			scoreIt();
			return true;
		}
		return false;
	}
	
	private void scoreIt() {
		player.addScoreAndContext(gameDef.calculateScore(this), gameDef.getContext(this));
		dismiss();
	}
}

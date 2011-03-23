package org.homelinux.murray.scorekeep;

import java.util.ArrayList;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

public final class Player {
	public String name;
	public ArrayList<Integer> scores;
	public int scoreTotal;
	public LinearLayout scoreList;
	public TextView totalTextView;
	private final Context parentContext;
	
	public Player(Context context, String playerName, int initialScore) {
		name = playerName;
		scoreTotal = initialScore;
		scores = new ArrayList<Integer>();
		parentContext = context;
		scoreList = new LinearLayout(context);
		scoreList.setOrientation(LinearLayout.VERTICAL);
		totalTextView = new TextView(context);
	}
	
	public Player(Context context, String name) {
		this(context,name,0);
	}

	public void addScore(int score) {
		scores.add(score);
		scoreTotal += score;
		totalTextView.setText(Integer.toString(scoreTotal));
		TextView newScoreItem = new TextView(parentContext);
		scoreList.addView(newScoreItem);
	}
	
	public boolean addFormula(String formula) {
		MathEval evaluator = new MathEval();
		double result;
		
		try {
			result = evaluator.evaluate(formula);
		} catch(Exception e) {
			return false;
		}
		
		int intResult = (int) result;
		addScore(intResult);
		
		return true;
	}
}

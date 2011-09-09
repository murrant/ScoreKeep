/**
 *  Copyright 2011 Tony Murray <murraytony@gmail.com>
 */

package com.splashmobileproductions.scorekeep.games.definitions;

import com.splashmobileproductions.scorekeep.R;

import com.splashmobileproductions.scorekeep.PlayerData;
import com.splashmobileproductions.scorekeep.ScoreData;
import com.splashmobileproductions.scorekeep.games.BiddingGame;
import com.splashmobileproductions.scorekeep.games.GameDefs;

import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("unused")
public class FiveHundred extends BiddingGame {
	private static final String DEBUG_TAG = "ScoreKeep:FiveHundred";
	public FiveHundred() {
		super(GameDefs.FIVE_HUNDRED, "Five Hundred", R.layout.scoredialog_fivehundred_bid, R.layout.scoredialog_fivehundred_score, true);
	}

	@Override
	public ScoreData getScore(Dialog dlg, PlayerData player) {
		Long score = null;
		String context = null;
		if(isScoreDialogShown()) {
			TextView trickTextView = (TextView) dlg.findViewById(R.id.score_edit);
			Integer tricks = Integer.parseInt(trickTextView.getText().toString());
			if(tricks == null || (tricks<0||tricks>10)) {
				Toast.makeText(dlg.getContext(), "You must enter a valid number between 1 and 10", Toast.LENGTH_LONG);
				return null;
			}
			Long bidder = player.game.getExtraAsLong("Bidder");
			player.game.putExtra("Bidder", -1);

			// score this player
			score = getScore(player, bidder, tricks);

			// if only 2 teams, score the other team
			if(player.game.getPlayers().size()==2) {
				for(PlayerData pl : player.game.getPlayers()) {
					if(pl.id == player.id) continue;
					// found the other player, got remaining tricks from 10
					pl.addScore(getScore(pl, bidder, (10-tricks)));
				}
			}

		} else {
			Spinner amt = (Spinner) dlg.findViewById(R.id.score_bid_amount);
			String amount = amt.getSelectedItem().toString();

			Spinner tp = (Spinner) dlg.findViewById(R.id.score_bid_type);
			String type = tp.getSelectedItem().toString();
			
			if(amount.isEmpty() && !type.endsWith("Nello")) {
				Toast.makeText(dlg.getContext(), "You must choose a bid ammount", Toast.LENGTH_LONG);
				return null;
			}
			
			context = amount + "  " + type;
			player.game.putExtra("Bidder", player.id);
			// clear everyone else's context.
			for(PlayerData pl : player.game.getPlayers()) {
				if(pl.id == player.id) continue;
				pl.addContext("");
			}
		}
		switchDialog();
		return new ScoreData(score, context);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	private Long getScore(PlayerData player, long bidderId, int tricks) {
		if(player.id == bidderId) {
			Long score = null;
			ScoreData lastScore = player.getLastScore();

			String bidType = lastScore.context.substring(2).trim();
			
			// Handle Nello
			if(bidType.endsWith("Nello")) {
				if(tricks>0) {
					score = new Long(-250);
				} else {
					score = new Long(250);
				}
				if(bidType.equals("Double Nello")) {
					return score * 2;
				}
				return score;
			}
			
			Integer bidAmount = Integer.parseInt(lastScore.context.substring(0, 2).trim());
			if(bidAmount==null) return null;
			
			long suitPoints = 0;
			if(bidType.equals("Spades")) {
				suitPoints = 40;
			} else if(bidType.equals("Clubs")) {
				suitPoints = 60;
			} else if(bidType.equals("Diamonds")) {
				suitPoints = 80;
			} else if(bidType.equals("Hearts")) {
				suitPoints = 100;
			} else if(bidType.equals("No Trump")) {
				suitPoints = 120;
			}

			score = (bidAmount-6)*100+suitPoints;
			// didn't make it, subtract points.
			if(tricks<bidAmount) {
				score = score * -1;
			}
			return score;
		} else {
			return new Long(tricks*10);
		}
	}
}

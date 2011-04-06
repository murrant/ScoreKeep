package org.homelinux.murray.scorekeep.games;

import org.homelinux.murray.scorekeep.R;

import android.app.Dialog;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Generic extends GameDefinition {

	public Generic() {
		super(0, "Generic", R.layout.add_score_generic, true);
	}

	@Override
	public Long calculateScore(Dialog dlg) {
		TextView scoreView = (TextView) dlg.findViewById(R.id.score_edit);
		Long score = null;
		try {
			score = parseScore(scoreView.getText().toString());
		} catch(Exception e) {
			Toast.makeText(dlg.getContext(), R.string.number_parse_failed, Toast.LENGTH_LONG).show();
		}
		return score;
	}

	@Override
	public String getContext(Dialog dlg) {
		TextView contextView = (TextView) dlg.findViewById(R.id.context_edit);
		return contextView.getText().toString();
	}

	public void onClick(View v) {
		// nothing
	}

}

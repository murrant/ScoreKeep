package com.splashmobileproductions.scorekeep;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class Utility {

/**
 * Calling Activity must implement onclick listener.
 * @param title
 * @return
 */
	public static YesNoDialogFragment createYesNoDialog(DialogInterface.OnClickListener listener, int message) {
		YesNoDialogFragment frag = new YesNoDialogFragment(listener);
		Bundle args = new Bundle();
		args.putInt("message", message);
		frag.setArguments(args);
		return frag;
	}

}

class YesNoDialogFragment extends DialogFragment {
	private final DialogInterface.OnClickListener listener;
	public YesNoDialogFragment(OnClickListener listener) {
		this.listener = listener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		int message = getArguments().getInt("message");

		return new AlertDialog.Builder(getActivity())
		.setMessage(message)
		.setPositiveButton(android.R.string.yes, listener )
		.setNegativeButton(android.R.string.no, listener )
		.create();
	}
}
package com.splashmobileproductions.scorekeep;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Bundle;

import java.util.Random;

public class Utility {

    private final static int MIX_COLOR = 0xFFFFFFFF;

    public static int generateRandomColor() {
        Random random = new Random();
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);

        // mix the color
        red = (red + Color.red(MIX_COLOR)) / 2;
        green = (green + Color.green(MIX_COLOR)) / 2;
        blue = (blue + Color.blue(MIX_COLOR)) / 2;

        return Color.rgb(red, green, blue);
    }

    /**
     * Calling Activity must implement onclick listener.
     */
    public static YesNoDialogFragment createYesNoDialog(DialogInterface.OnClickListener listener, int message) {
        YesNoDialogFragment frag = new YesNoDialogFragment(listener);
        Bundle args = new Bundle();
        args.putInt("message", message);
        frag.setArguments(args);
        return frag;
    }

}

@SuppressLint("ValidFragment")
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
                .setPositiveButton(android.R.string.yes, listener)
                .setNegativeButton(android.R.string.no, listener)
                .create();
    }
}
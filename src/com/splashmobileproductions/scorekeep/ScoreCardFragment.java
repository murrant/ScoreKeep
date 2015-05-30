/**
 * Copyright 2011-2013 Tony Murray <murraytony@gmail.com>
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.splashmobileproductions.scorekeep;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.TextView;

import com.splashmobileproductions.scorekeep.data.GameData;

public class ScoreCardFragment extends Fragment {
    private GameData game;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.score_card, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Intent launchingIntent = getActivity().getIntent();
        Uri dataUri = launchingIntent.getData();
        Log.d("ScoreCardFragment", dataUri.toString());

        // load the game data
        game = new GameData(getActivity(), dataUri);

        GridView grid = (GridView) view.findViewById(R.id.score_card_grid);
        TextView desc = (TextView) view.findViewById(R.id.game_desc_title);
        desc.setText(game.description);
        getActivity().setTitle(game.description);
        grid.setAdapter(game);

        // check if wake lock should be enabled and enable it
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        if (settings.getBoolean(SettingsFragment.KEY_SCREEN_ON, false)) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.score_card_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.score_round:
                return false;
            case R.id.new_game:
                startActivity(new Intent(getActivity(), NewGameActivity.class));
                getActivity().overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                getActivity().finish(); // remove this activity
                return true;
            case R.id.change_players:
                //TODO add/remove/reorder players
                return false;
            case R.id.reset_scores:
                game.resetScores();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
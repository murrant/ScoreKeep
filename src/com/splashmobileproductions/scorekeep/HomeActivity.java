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


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.splashmobileproductions.scorekeep.data.RecyclerGameAdapter;
import com.splashmobileproductions.scorekeep.provider.Game;


public class HomeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    @SuppressWarnings("unused")
    private static final String DEBUG_TAG = "ScoreKeep:HomeActivity";
    private static final int GAME_LOADER = 1;
    TransitionManager mTransitionManager;
    Scene mDefaultScene, mHistoryScene;
    RecyclerView mHistoryList;
    private int gamesListFragmentId = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sk_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle("ScoreKeep");
        setSupportActionBar(toolbar);
//        toolbar.bringToFront();


//        if (savedInstanceState == null) {
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.add(R.id.home_base_layout, new GameHistoryFragment()).commit();
//        }


        mHistoryList = (RecyclerView) findViewById(R.id.history_list);
        mHistoryList.setLayoutManager(new LinearLayoutManager(this));



/*
        GameHistoryFragment mGameFragment;

        //set up transitions
        ViewGroup container = (ViewGroup) findViewById(R.id.home_layout);
        mDefaultScene = Scene.getSceneForLayout(container, R.layout.sk_home_default, this);
        mHistoryScene = Scene.getSceneForLayout(container, R.layout.sk_home_list, this);
        TransitionInflater transitionInflater = TransitionInflater.from(this);
        mTransitionManager = transitionInflater.inflateTransitionManager(R.transition.transition_manager, container);
*/
        if (savedInstanceState == null) {
            getSupportLoaderManager().initLoader(GAME_LOADER, null, this);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
//        menu.clear();
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void newGame(View view) {
        NewGameFragment newGameFragment = new NewGameFragment();
        newGameFragment.show(getSupportFragmentManager(), "sk_new_game_dialog");
    }
/*

    public void gotoDefaultHomeScreen(View view) {
        mTransitionManager.transitionTo(mDefaultScene);
    }

    public void gotoHistoryHomeScreen(View view) {
        Fragment gamesListFragment = getSupportFragmentManager().findFragmentById(gamesListFragmentId);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (gamesListFragment == null) {
            gamesListFragment = new GameHistoryFragment();
            gamesListFragmentId = gamesListFragment.getId();

//        ft.setCustomAnimations(R.animator.game_history_animator, 0);
            ft.add(R.id.home_history_frame, gamesListFragment).commitAllowingStateLoss();
        } else {
            ft.show(gamesListFragment).commitAllowingStateLoss();
        }

        mTransitionManager.transitionTo(mHistoryScene);
    }

*/

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
            case R.id.home_menu_manage_players:
                startActivity(new Intent(this, PlayerListActivity.class));
                return true;
            case R.id.home_menu_about:
                //show about dialog
                try {
                    AboutDialogBuilder.create(this).show();
                } catch (PackageManager.NameNotFoundException ignored) {
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        switch (loaderID) {
            case GAME_LOADER:
                return new CursorLoader(this, Game.CONTENT_URI, null, null, null, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d(DEBUG_TAG, "Game List Cursor loaded, count: " + cursor.getCount());
        if (cursor.getCount() > 0) {
//            gotoHistoryHomeScreen(null);
            mHistoryList.setAdapter(new RecyclerGameAdapter(this, cursor));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    public static class AboutDialogBuilder {
        public static AlertDialog create(Context context) throws PackageManager.NameNotFoundException {
            // Try to load the a package matching the name of our own package
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            String versionInfo = pInfo.versionName;

            String aboutTitle = String.format("About %s", context.getString(R.string.app_name));
            String versionString = String.format("Version: %s", versionInfo);
            String aboutText = context.getString(R.string.about_dialog_text);

            // Set up the TextView
            final TextView message = new TextView(context);
            // We'll use a spannable string to be able to make links clickable
            final SpannableString s = new SpannableString(aboutText);

            // Set some padding
            message.setPadding(5, 5, 5, 5);
            // Set up the final string
            message.setText(versionString + "\n\n" + s);
            // Now linkify the text
            Linkify.addLinks(message, Linkify.ALL);

            return new AlertDialog.Builder(context).setTitle(aboutTitle).setCancelable(true).setIcon(R.drawable.icon).setPositiveButton(
                    context.getString(android.R.string.ok), null).setView(message).create();
        }
    }


}

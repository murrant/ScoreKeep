/**
 *  Copyright 2011-2013 Tony Murray <murraytony@gmail.com>
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


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Outline;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.transition.Scene;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.splashmobileproductions.scorekeep.provider.Game;


public class HomeActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {
    @SuppressWarnings("unused")
    private static final String DEBUG_TAG = "ScoreKeep:HomeActivity";
    private static final int GAME_LOADER = 1;
    TransitionManager mTransitionManager;
    Scene mDefaultScene, mHistoryScene;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sk_home_default);
        GameHistoryFragment mGameFragment;

        //set up transitions
        ViewGroup container = (ViewGroup) findViewById(R.id.home_base_layout).getParent();
        mDefaultScene = Scene.getSceneForLayout(container, R.layout.sk_home_default, this);
        mHistoryScene = Scene.getSceneForLayout(container, R.layout.sk_home, this);
        TransitionInflater transitionInflater = TransitionInflater.from(this);
        mTransitionManager = transitionInflater.inflateTransitionManager(R.transition.transition_manager, container);

        if (savedInstanceState == null) {
            getLoaderManager().initLoader(GAME_LOADER, null, this);
        }
        setFabOutline();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        menu.clear();
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void newGame(View view) {
        NewGameFragment newGameFragment = new NewGameFragment();
        newGameFragment.show(getFragmentManager(), "sk_new_game_dialog");
    }

    public void gotoDefaultHomeScreen(View view) {

        mTransitionManager.transitionTo(mDefaultScene);
        setFabOutline();
    }

    public void gotoHistoryHomeScreen(View view) {
        Fragment gamesListFragment = getFragmentManager().findFragmentById(R.layout.games_list);
        if (gamesListFragment == null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
//            ft.setCustomAnimations(R.animator.game_history_animator, 0);
            ft.add(R.id.home_history_frame, new GameHistoryFragment()).commitAllowingStateLoss();
            mTransitionManager.transitionTo(mHistoryScene);

            setFabOutline();
        }
    }

    private void setFabOutline() {
        int size = getResources().getDimensionPixelSize(R.dimen.fab_size);
        Outline outline = new Outline();
        outline.setOval(0, 0, size, size);
        View newButton = findViewById(R.id.home_btn_new);
        newButton.setOutline(outline);
//        newButton.setClipToOutline(true);
//        newButton.setElevation(R.dimen.fab_elevation);
    }

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
                } catch (PackageManager.NameNotFoundException nnfe) {
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
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if (cursor.getCount() > 0) {
            gotoHistoryHomeScreen(null);
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

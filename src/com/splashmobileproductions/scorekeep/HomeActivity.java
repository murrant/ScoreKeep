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
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class HomeActivity extends Activity {
    @SuppressWarnings("unused")
    private static final String DEBUG_TAG = "ScoreKeep:HomeActivity";
    ViewGroup mSceneRoot;
    Scene mCurrentScene;
    TransitionManager mTransitionManager;
    Scene mEmptyScene,mHistoryScene;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sk_home_empty);
        View container = (View) findViewById(R.id.home_container);
        mSceneRoot = (ViewGroup) container.getParent();
        mEmptyScene = Scene.getSceneForLayout(mSceneRoot, R.layout.sk_home_empty, this);
        mHistoryScene = Scene.getSceneForLayout(mSceneRoot, R.layout.sk_home, this);

        mTransitionManager = new TransitionManager();
        mCurrentScene = mEmptyScene;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(mCurrentScene == mEmptyScene) {
            mTransitionManager.transitionTo(mHistoryScene);
        } else {
            mTransitionManager.transitionTo(mEmptyScene);
        }

        return false;
    }


}

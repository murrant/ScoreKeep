/**
 *  Copyright 2011 Tony Murray <murraytony@gmail.com>
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

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;


public class HomeActivity extends FragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		setContentView(R.layout.home_layout);
		
		FrameLayout fragmentFrame = (FrameLayout) findViewById(R.id.home_fragment_frame);
		
		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
				== Configuration.SCREENLAYOUT_SIZE_XLARGE) {
			// on a xlarge screen device ...
			getLayoutInflater().inflate(R.layout.app_logo, fragmentFrame);
		} else {
			// on a non-xlarge screen device (phone, etc)
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.add(R.id.home_fragment_frame, new HomeFragment());
			ft.commit();
		}
		
		ActionBar ab = this.getSupportActionBar();
		if(ab==null) {
			android.util.Log.d("HomeActivity", "Could not access the ActionBar");
		}
    }
}

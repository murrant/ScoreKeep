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
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ScoreCardActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_card_activity);

        Uri dataUri = getIntent().getData();
        // If there is no data associated with the Intent, bring up new game dialog
        if (dataUri == null) {
            startActivity(new Intent(this, NewGameFragment.class));
            finish(); // end the parent activity before we start a new game.
        }
    }

//    @SuppressLint("Override")
//    public void onBackPressed() {
//        super.onBackPressed();
//        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
//    }
}
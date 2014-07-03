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
package com.splashmobileproductions.scorekeep.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.splashmobileproductions.scorekeep.R;

import java.util.List;

public class HistoryAdapter extends ArrayAdapter<ScoreData> {
    private final LayoutInflater mInflater;

    public HistoryAdapter(Context context, List<ScoreData> list) {
        super(context, R.layout.score_history_item, R.id.score, list);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.score_history_item, null);
            holder = new ViewHolder();
            holder.score = (TextView) convertView.findViewById(R.id.score);
            holder.context = (TextView) convertView.findViewById(R.id.context);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ScoreData sd = getItem(position);
        if (sd.score != null) {
            holder.score.setText(sd.score.toString());
        } else {
            holder.score.setText("");
        }
        if (sd.context != null) {
            holder.context.setText(sd.context);
        } else {
            holder.context.setText("");
        }
        //holder.score.setBackgroundColor(android.R.color.black);
        //holder.score.setTextColor(android.R.color.black);
        return convertView;
    }

    public static class ViewHolder {
        TextView score;
        TextView context;
    }
}

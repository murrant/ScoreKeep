package org.homelinux.murray.scorekeep;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class HistoryAdapter extends ArrayAdapter<ScoreData> {
	private final LayoutInflater mInflater;

	public HistoryAdapter(Context context, List<ScoreData> list) {
		super(context, R.layout.score_history_item, R.id.score, list);
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.score_history_item, null);
            holder = new ViewHolder();
            holder.score = (TextView)convertView.findViewById(R.id.score);
            holder.context = (TextView)convertView.findViewById(R.id.context);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        ScoreData sd = getItem(position);
        if(sd.score != null) {
        	holder.score.setText(sd.score.toString());
        } else {
        	holder.score.setText("");
        }
        if(sd.context != null) {
        	holder.context.setText(sd.context);
        } else {
        	holder.context.setText("");
        }
        return convertView;
	}
	
    public static class ViewHolder {
        TextView score;
        TextView context;
    }
}

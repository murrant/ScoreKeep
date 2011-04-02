package org.homelinux.murray.scorekeep;

import java.util.ArrayList;

import org.homelinux.murray.scorekeep.provider.Game;
import org.homelinux.murray.scorekeep.provider.ScoresProvider;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

public final class GameData extends BaseAdapter implements ListAdapter {
	final long id;
	final String description;
	private ArrayList<PlayerData> players = new ArrayList<PlayerData>();
	private final LayoutInflater mInflater;

	/**
	 * Load game data from the database with all players.
	 * 
	 * @param context The calling activitie's context
	 * @param gameId The unique Id of the desired game
	 */
	public GameData(Context context, Uri gameUri) {
		id = ContentUris.parseId(gameUri);
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Cursor gameCursor = context.getContentResolver().query(gameUri, null, null, null, null);
		gameCursor.moveToFirst();
		description = gameCursor.getString(gameCursor.getColumnIndex(Game.COLUMN_NAME_DESCRIPTION));
		String playerIdsString = gameCursor.getString(gameCursor.getColumnIndex(Game.COLUMN_NAME_PLAYER_IDS));
		
		long[] playerIds = ScoresProvider.deserializePlayers(playerIdsString);
		for (long playerId : playerIds) {
			players.add(new PlayerData(context, playerId, this));
		}
	}
	
	public ArrayList<PlayerData> getPlayers() {
		return players;
	}

	public int getCount() {
		return players.size();
	}

	public Object getItem(int position) {
		return players.get(position);
	}
	public PlayerData getPlayerById(long playerId) {
		for (PlayerData pl : players) {
			if(pl.id == playerId) return pl;
		}
		return null;
	}

	public long getItemId(int position) {
		return players.get(position).id;
	}

	public int getItemViewType(int position) {
		return IGNORE_ITEM_VIEW_TYPE;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.badge, null);
            holder = new ViewHolder();
            holder.name = (TextView)convertView.findViewById(R.id.badge_name);
            holder.score = (TextView)convertView.findViewById(R.id.badge_score);
            holder.context = (TextView)convertView.findViewById(R.id.badge_context);
            holder.addButton = (Button) convertView.findViewById(R.id.badge_add);
            holder.historyButton = (Button) convertView.findViewById(R.id.badge_history);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        PlayerData pl = players.get(position);
        holder.name.setText(pl.name);
        holder.score.setText(Long.toString(pl.getTotal()));
        if(pl.getCurrentContext() != null) {
            holder.context.setText(pl.getCurrentContext());
        }
        holder.addButton.setOnClickListener(pl);
        holder.historyButton.setOnClickListener(pl);
        return convertView;
	}

	public int getViewTypeCount() {
		return 1;
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isEmpty() {
		return players.isEmpty();
	}
	
    public static class ViewHolder {
        TextView name;
        TextView score;
        TextView context;
        Button addButton;
        Button historyButton;
    }

	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return true;
	}
}

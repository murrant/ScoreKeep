package org.homelinux.murray.scorekeep;

import java.util.Date;
import java.util.List;

public final class Game {
	public final int gameId;
	public final String gameDescription;
	public final String creationDate;
	private String lastSaved;  // How much overhead? is it worth it?
	private List<Player> players;

	
	public Game(int gameId, String gameDescription, String creationDate, String lastSaved) {
		this.gameId = gameId;
		this.gameDescription = gameDescription;
		this.creationDate = creationDate;
		this.lastSaved = lastSaved;
	}
	
	public List<Player> getPlayers() {
		return players;
	}
	
	public Date getLastSaved() {
		return new Date(lastSaved);
	}
}

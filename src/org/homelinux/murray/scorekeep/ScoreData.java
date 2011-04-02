package org.homelinux.murray.scorekeep;


public final class ScoreData {
	final long id;
	final Long score;
	final long created;
	final String context;
	
	public ScoreData(long id, Long score, String context, long created) {
		this.id = id;
		this.score = score;
		this.created = created;
		this.context = context;
	}
}

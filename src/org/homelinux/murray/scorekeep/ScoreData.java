package org.homelinux.murray.scorekeep;


public final class ScoreData {
	public final long id;
	public final Long score;
	public final String context;
	public final long created;
	
	public ScoreData(long id, Long score, String context, long created) {
		this.id = id;
		this.score = score;
		this.created = created;
		this.context = context;
	}

	public ScoreData(Long score, String context) {
		this.score = score;
		this.context = context;
		id = -1;
		created = -1;
	}
}

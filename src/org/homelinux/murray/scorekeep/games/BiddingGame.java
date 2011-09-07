package org.homelinux.murray.scorekeep.games;

public abstract class BiddingGame extends GameDefinition {
    private final int bidResource;
    private final int scoreResource;
    private boolean scoreDialogShown = false;

    public BiddingGame(int id, String name, int bidResource, int scoreResource, boolean enabled) {
	super(id, name, 0, enabled);
	this.bidResource = bidResource;
	this.scoreResource = scoreResource;
    }

    protected void switchDialog() {
	scoreDialogShown = !scoreDialogShown;
    }
    protected void setBidDialog() {
	scoreDialogShown = false;
    }
    protected void setScoreDialog() {
	scoreDialogShown = true;
    }
    protected boolean isScoreDialogShown() {
	return scoreDialogShown;
    }

    public int getDialogResource() {
	if(scoreDialogShown) {
	    return scoreResource;
	} else {
	    return bidResource;
	}
    }

}

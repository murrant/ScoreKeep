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
package com.splashmobileproductions.scorekeep.games;

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

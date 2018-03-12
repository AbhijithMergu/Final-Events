package com.it.acumen.acumeneventslocal;

/**
 * Created by pavan on 2/28/2018.
 */

public class PlayerDetails {
    private String playerId;
    private String playerName;

    public PlayerDetails(String playerId, String playerName){
        this.playerId = playerId;
        this.playerName = playerName;
    }
    public String getPlayerId() {
        return playerId;
    }

    public String getPlayerName() {
        return playerName;
    }
}

package com.it.acumen.acumeneventslocal;

import java.util.List;

/**
 * Created by pavan on 2/28/2018.
 */

public class Game {
    private String gameId;
    private List<PlayerDetails> playerList;
    private int round1Score;
    private int round2Score;
    private int round3Score;
    private int status;

    public Game(String gameId,List<PlayerDetails> playerList){
        this.gameId = gameId;
        this.playerList = playerList;
        this.round1Score = 0;
        this.round2Score = 0;
        this.round3Score = 0;
        this.status = 0;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public List<PlayerDetails> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<PlayerDetails> playerList) {
        this.playerList = playerList;
    }

    public int getRound1Score() {
        return round1Score;
    }

    public void setRound1Score(int round1Score) {
        this.round1Score = round1Score;
    }

    public int getRound2Score() {
        return round2Score;
    }

    public void setRound2Score(int round2Score) {
        this.round2Score = round2Score;
    }

    public int getRound3Score() {
        return round3Score;
    }

    public void setRound3Score(int round3Score) {
        this.round3Score = round3Score;
    }

    public void setStatus(int status) { this.status = status;}

    public int getStatus() {return status;}
}

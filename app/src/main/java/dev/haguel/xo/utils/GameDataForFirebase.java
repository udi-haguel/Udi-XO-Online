package dev.haguel.xo.utils;

public class GameDataForFirebase {
    private String turn;
    private String winner;
    private String turnCount;
    private String gameBoard;
    private String timeStamp;
    private String isGameEnded;
    private String player1Score;
    private String player2Score;
    private String winningLinePosition;
    private String currentTileClicked;

    public GameDataForFirebase() {
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getTurnCount() {
        return turnCount;
    }

    public void setTurnCount(String turnCount) {
        this.turnCount = turnCount;
    }

    public String getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(String gameBoard) {
        this.gameBoard = gameBoard;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getIsGameEnded() {
        return isGameEnded;
    }

    public void setIsGameEnded(String isGameEnded) {
        this.isGameEnded = isGameEnded;
    }

    public String getPlayer1Score() {
        return player1Score;
    }

    public void setPlayer1Score(String player1Score) {
        this.player1Score = player1Score;
    }

    public String getPlayer2Score() {
        return player2Score;
    }

    public void setPlayer2Score(String player2Score) {
        this.player2Score = player2Score;
    }


    public String getWinningLinePosition() {
        return winningLinePosition;
    }

    public void setWinningLinePosition(String position) {
        this.winningLinePosition = position;
    }

    public String getCurrentTileClicked() {
        return currentTileClicked;
    }

    public void setCurrentTileClicked(String currentTileClicked) {
        this.currentTileClicked = currentTileClicked;
    }
}

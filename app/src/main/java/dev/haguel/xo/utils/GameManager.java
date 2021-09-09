package dev.haguel.xo.utils;

import java.util.Date;

import dev.haguel.xo.entities.BaseGame;
import dev.haguel.xo.entities.Game;

public class GameManager {

    private static GameManager instance;
    private Game game;

    public static GameManager instance(){
        if (instance == null){
            instance = new GameManager();
        }
        return instance;
    }

    public void startNewGame() {
         game = new Game();
    }

    public void setCurrentTileClicked(int clicked){
        game.setCurrentTileClicked(clicked);
    }

    public int getCurrentTileClicked(){
        return game.getCurrentTileClicked();
    }

    public void tileClicked(int index) {
        game.tileClicked(index);
    }

    public BaseGame.EnumWinner checkForWin() {
        return game.checkForWin();
    }

    public BaseGame.EnumTurn getTurn(){
        return game.getTurn();
    }

    public BaseGame.EnumWinner getWinner(){
        return game.getWinner();
    }

    public int getTurnCount(){
        return game.getTurnCount();
    }

    public String getGameBoardString(){
        return game.getGameBoardString();
    }

    public int[] getGameBoard(){
        return game.getGameBoard();
    }

    public Date getTimeStamp(){
        return game.getTimeStamp();
    }

    public void setTurn(String turn){
        if (turn.equals("X"))
            game.setTurn(BaseGame.EnumTurn.X);
        else
            game.setTurn(BaseGame.EnumTurn.O);
    }

    public void setWinner(String winner){
        switch (winner){
            case "X":
                game.setWinner(BaseGame.EnumWinner.X);
                break;
            case "O":
                game.setWinner(BaseGame.EnumWinner.O);
                break;
            case "DRAW":
                game.setWinner(BaseGame.EnumWinner.DRAW);
            default:
                game.setWinner(BaseGame.EnumWinner.NONE);
        }
    }

    public void setTurnCount(String turnCount){
        game.setTurnCount(Integer.parseInt(turnCount));
    }

    public void setGameBoard(String gameBoard){
        game.setGameBoard(gameBoard);
    }

    public boolean hasActiveGame() {
        return game != null;
    }

    public String getWinningLinePosition(){
        return game.getWinningLinePosition();
    }

    public void setWinningLinePosition(String position){
        game.setWinningLinePosition(position);
    }


}

package dev.haguel.xo.entities;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import dev.haguel.xo.R;

public class Game extends BaseGame {

    private String winningLinePosition = "NONE";

    public Game() {
        turn = EnumTurn.X;
        winner = EnumWinner.NONE;
        turnCount = 0;
        gameBoard = new int[9];
        Arrays.fill(gameBoard, -1);
        timeStamp = new Date();
        currentTileClicked = -1;
    }

    public void setCurrentTileClicked(int clicked){
        currentTileClicked = clicked;
    }
    public int getCurrentTileClicked(){
        return currentTileClicked;
    }

    public EnumTurn getTurn() {
        return turn;
    }

    public EnumWinner getWinner(){
        return winner;
    }

    public int getTurnCount(){
        return turnCount;
    }

    public String getGameBoardString(){
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < gameBoard.length; i++) {
            str.append(gameBoard[i]);
            if (i < gameBoard.length-1)
                str.append(",");
        }
        return str.toString();
    }

    public Date getTimeStamp(){
        return timeStamp;
    }


    public void setTurn(EnumTurn turn){
        this.turn = turn;
    }

    public void setWinner(EnumWinner winner){
        this.winner = winner;
    }

    public void setTurnCount(int turnCount){
        this.turnCount = turnCount;
    }

    public void setGameBoard(String gameBoard){
        int[] intArr = new int[this.gameBoard.length];
        String[] strArr = gameBoard.split(",");
        for (int i = 0; i < this.gameBoard.length; i++) {
            intArr[i] = Integer.parseInt(strArr[i]);
        }
        this.gameBoard = intArr;
    }

    public void setTimeStamp(Date timeStamp){
        this.timeStamp = timeStamp;
    }

    public String getWinningLinePosition(){
        return winningLinePosition;
    }

    public void setWinningLinePosition(String winningLinePosition){
        this.winningLinePosition = winningLinePosition;
    }

    public void tileClicked(int index) {
        turnCount++;
        switch (turn){
            case X:
                gameBoard[index] = 0;
                turn = EnumTurn.O;
                break;
            case O:
                gameBoard[index] = 1;
                turn = EnumTurn.X;
                break;
        }
    }

    public int[] getGameBoard(){
        return gameBoard;
    }

    public EnumWinner checkForWin() {
        EnumWinner check = checkForWinner();
        switch (check){
            case X:
                winner = EnumWinner.X;
                break;
            case O:
                winner = EnumWinner.O;
                break;
            case DRAW:
                winner = EnumWinner.DRAW;
                break;
            default:
                winner = EnumWinner.NONE;
                break;
        }

        return winner;
    }

    public EnumWinner checkForWinner(){
        int winner;
        if (gameBoard[0] == gameBoard[1] && gameBoard[1] == gameBoard[2] && gameBoard[2] != -1) {
            // row 1 wins
            setWinningLinePosition("row1");
            winner = gameBoard[0];
        } else if (gameBoard[3] == gameBoard[4] && gameBoard[4] == gameBoard[5] && gameBoard[5] != -1) {
            // row 2 wins
            setWinningLinePosition("row2");
            winner = gameBoard[3];
        } else if(gameBoard[6] == gameBoard[7] && gameBoard[7] == gameBoard[8] && gameBoard[8] != -1) {
            // row 3 wins
            setWinningLinePosition("row3");
            winner = gameBoard[6];
        } else if(gameBoard[0] == gameBoard[3] && gameBoard[3] == gameBoard[6] && gameBoard[6] != -1) {
            // col 1 wins
            setWinningLinePosition("col1");
            winner = gameBoard[0];
        } else if(gameBoard[1] == gameBoard[4] && gameBoard[4] == gameBoard[7] && gameBoard[7] != -1) {
            // col 2 wins
            setWinningLinePosition("col2");
            winner = gameBoard[1];
        } else if(gameBoard[2] == gameBoard[5] && gameBoard[5] == gameBoard[8] && gameBoard[8] != -1) {
            // col 3 wins
            setWinningLinePosition("col3");
            winner = gameBoard[2];
        } else if(gameBoard[0] == gameBoard[4] && gameBoard[4] == gameBoard[8] && gameBoard[8] != -1) {
            // row col 1 wins ( \ )
            setWinningLinePosition("rowcol1");
            winner = gameBoard[0];
        } else if(gameBoard[2] == gameBoard[4] && gameBoard[4] == gameBoard[6] && gameBoard[6] != -1) {
            // row col 3 wins ( / )
            setWinningLinePosition("rowcol3");
            winner = gameBoard[2];
        } else {
            setWinningLinePosition("NONE");
            winner = -1;
        }

        if (winner != -1) {
            switch (winner) {
                case 0:
                    return EnumWinner.X;
                case 1:
                    return EnumWinner.O;
            }
        } else {
            if (turnCount == 9) {
                return EnumWinner.DRAW;
            }
        }
        return EnumWinner.NONE;
    }


}

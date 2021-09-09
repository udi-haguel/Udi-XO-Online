package dev.haguel.xo.entities;

import java.util.Date;

public abstract class BaseGame {
    protected EnumTurn turn;
    protected int turnCount;
    protected int[] gameBoard;
    protected EnumWinner winner;
    protected Date timeStamp;
    protected int currentTileClicked;



    public enum EnumWinner {
        NONE,
        DRAW,
        X,
        O
    }

    public enum EnumTurn {
        X ("X"),
        O ("0");
        private String text;
        EnumTurn(String text) {
            this.text = text;
        }
    }
}

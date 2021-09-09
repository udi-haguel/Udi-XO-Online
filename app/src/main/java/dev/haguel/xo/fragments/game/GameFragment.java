package dev.haguel.xo.fragments.game;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintSet;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import dev.haguel.xo.R;
import dev.haguel.xo.activity.MainActivity;
import dev.haguel.xo.activity.WinnerActivity;
import dev.haguel.xo.entities.BaseGame;
import dev.haguel.xo.utils.EventClickListener;
import dev.haguel.xo.utils.GameDataForFirebase;
import dev.haguel.xo.utils.GameManager;
import dev.haguel.xo.utils.Utils;
import dev.haguel.xo.utils.YoYoCallback;


public class GameFragment extends BaseGameFragment implements EventClickListener {

    public static final String BOARD_TAG = "board_fragment_tag";



    private TextView tvNewGame;
    private TextView tvResetGame;
    private TextView tvExitToMenu;
    private TextView tvXscore;
    private TextView tvOscore;
    private TextView tvWinner;
    private TextView tvXdot;
    private TextView tvOdot;
    private int xScore;
    private int oScore;
    private boolean isOnline;
    private String playerName;
    private String roomName;
    private String role;

    private GameDataForFirebase dataForFirebase;

    private BoardFragment boardFragment;

    private ValueEventListener roomEvent;
    private ValueEventListener gameDataEventListener;


    public GameFragment() {
        // Required empty public constructor
    }

    public static GameFragment newInstance(String roomName, String playerName, String role, boolean isOnline) {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putString("roomName", roomName);
        args.putString("playerName", playerName);
        args.putString("role", role);
        args.putBoolean("isOnline", isOnline);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        tvWinner = view.findViewById(R.id.tvWinner);
        tvXscore = view.findViewById(R.id.tvXscore);
        tvOscore = view.findViewById(R.id.tvOscore);
        tvXdot = view.findViewById(R.id.tvXdot);
        tvOdot = view.findViewById(R.id.tvOdot);
        tvNewGame = view.findViewById(R.id.tvNewGame);
        tvResetGame = view.findViewById(R.id.tvResetGame);
        tvExitToMenu = view.findViewById(R.id.tvExitToMenu);

        top = view.findViewById(R.id.layoutTopGame);
        middle = view.findViewById(R.id.layoutMiddleGame);
        bottom = view.findViewById(R.id.layoutBottomGame);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() == null) return;
        Bundle bundle = getArguments();
        if (bundle == null || !bundle.containsKey("roomName") ||
                !bundle.containsKey("playerName") ||
                !bundle.containsKey("role") ||
                !bundle.containsKey("isOnline")) return;
        roomName = bundle.getString("roomName");
        playerName = bundle.getString("playerName");
        isOnline = bundle.getBoolean("isOnline");
        role = bundle.getString("role");


        setAnimations();
        ((MainActivity) getActivity()).toggleDialog(false);

        topInAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.top_in_animation);
        middleInAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.middle_in_animation);
        bottomInAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_in_animation);

        xScore = oScore = 0;

        tvXscore.setText(String.valueOf(xScore));
        tvOscore.setText(String.valueOf(oScore));

        boardFragment = BoardFragment.newInstance(GameFragment.this);
        Utils.changeFragment(R.id.board_container, getParentFragmentManager().beginTransaction(),
                boardFragment, BOARD_TAG, false);



        tvExitToMenu.setOnClickListener(v->{
            YoYo.with(Techniques.RubberBand)
                    .withListener(new YoYoCallback() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            startAnimations(topOutAnim, middleOutAnim, bottomOutAnim);
                            Utils.changeFragment(R.id.game_fragment_container, getParentFragmentManager().beginTransaction(),
                                    MenuFragment.newInstance(), true);
                        }
                    })
                    .duration(500)
                    .repeat(0)
                    .playOn(tvExitToMenu);

        });

        tvNewGame.setOnClickListener(v -> {
            YoYo.with(Techniques.RubberBand)
                    .withListener(new YoYoCallback() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            GameManager.instance().startNewGame();
                            boardFragment.cleanBoard();
                            tvWinner.setText(R.string.x_turn);
                            updateDataForFirebase();
                        }
                    })
                    .duration(500)
                    .repeat(0)
                    .playOn(tvNewGame);

        });

        tvResetGame.setOnClickListener(v -> {
            YoYo.with(Techniques.RubberBand)
                    .withListener(new YoYoCallback() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            GameManager.instance().startNewGame();
                            boardFragment.cleanBoard();
                            tvWinner.setText(R.string.x_turn);
                            xScore = oScore = 0;
                            tvXscore.setText(String.valueOf(xScore));
                            tvXscore.setText(String.valueOf(oScore));
                            updateDataForFirebase();
                        }
                    })
                    .duration(500)
                    .repeat(0)
                    .playOn(tvResetGame);

        });

        GameManager.instance().startNewGame();
        tvWinner.setText(R.string.x_turn);

        if (isOnline){
            if (role.equals("X")){
                tvXdot.setVisibility(View.VISIBLE);
            } else {
                tvOdot.setVisibility(View.VISIBLE);
            }
            tvNewGame.setVisibility(View.GONE);
            tvResetGame.setVisibility(View.GONE);
            dataForFirebase = new GameDataForFirebase();
            onlineGameEventListener();
            roomEventListener();
        }

    }

    @Override
    public void tileClickListener(ImageView v, int index) {


        BaseGame.EnumWinner winner = GameManager.instance().checkForWin();

        // Determine If Player Can Press,
        if (isOnline) {
            //First Time Only X play
            if (GameManager.instance().getTurnCount() == 0 && role.equals("O")) {
                Toast.makeText(getContext(), "Its X turn!", Toast.LENGTH_SHORT).show();
                return;
            }
            // exit if we have a winner or a draw
            else if(winner != BaseGame.EnumWinner.NONE){
                return;
            }
            //  Disable clicks for O if its X turn
            else if (GameManager.instance().getTurn() == BaseGame.EnumTurn.X && role.equals("O")){
                Toast.makeText(getContext(), "Its X turn!", Toast.LENGTH_SHORT).show();
                return;
            }
            //  Disable clicks for X if its O turn
            else if (GameManager.instance().getTurn() == BaseGame.EnumTurn.O && role.equals("X")){
                Toast.makeText(getContext(), "Its O turn!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Paint Tile If Match Is On
        if (winner == BaseGame.EnumWinner.NONE) {
            v.setClickable(false);
            GameManager.instance().setCurrentTileClicked(index);
            GameManager.instance().tileClicked(index);
            checkForWinner(GameManager.instance().checkForWin());
            if (!isOnline){
                boardFragment.updateGameBoardTiles(index);
                updateTurnText(GameManager.instance().getTurn());
                updateWinningLine();
            }
        }

        // paint winning tiles

    }

    private void updateWinner(BaseGame.EnumWinner winner){
        if (winner != BaseGame.EnumWinner.NONE){
            switch (winner){
                case X:
                    tvWinner.setText(R.string.x_wins);
                    tvXscore.setText(String.valueOf(xScore));
                    break;
                case O:
                    tvWinner.setText(R.string.o_wins);
                    tvOscore.setText(String.valueOf(oScore));
                    break;
                default:
                    tvWinner.setText(R.string.draw);
                    break;
            }
            if (xScore != 5 && oScore != 5) {
                wait2SecondsAndStartNewGame();
            } else {
                moveToWinningActivity();
            }
        }
    }

    private void checkForWinner(BaseGame.EnumWinner winner){
        if (winner != BaseGame.EnumWinner.NONE){
            if (winner == BaseGame.EnumWinner.X)
                xScore++;
            else if (winner == BaseGame.EnumWinner.O)
                oScore++;
            updateWinner(winner);
        }
        updateDataForFirebase();
    }

    private void updateDataForFirebase(){
        if (!isOnline) return;
        boolean isGameEnded = (xScore==5 || oScore==5);
        dataForFirebase.setTurn(String.valueOf(GameManager.instance().getTurn()));
        dataForFirebase.setWinner(String.valueOf(GameManager.instance().getWinner()));
        dataForFirebase.setTurnCount(String.valueOf(GameManager.instance().getTurnCount()));
        dataForFirebase.setGameBoard(String.valueOf(GameManager.instance().getGameBoardString()));
        dataForFirebase.setTimeStamp(String.valueOf(GameManager.instance().getTimeStamp()));
        dataForFirebase.setIsGameEnded(String.valueOf(isGameEnded));
        dataForFirebase.setPlayer1Score(String.valueOf(xScore));
        dataForFirebase.setPlayer2Score(String.valueOf(oScore));
        dataForFirebase.setWinningLinePosition(GameManager.instance().getWinningLinePosition());
        dataForFirebase.setCurrentTileClicked(String.valueOf(GameManager.instance().getCurrentTileClicked()));

        //gameDataRef.setValue(dataForFirebase);
        ((MainActivity)getActivity()).setDBValue(MainActivity.eDBListenerType.Game, dataForFirebase, roomName);
    }

    private void updateDataFromFirebase(GameDataForFirebase data){
        GameManager.instance().setTurn(data.getTurn());
        GameManager.instance().setWinner(data.getWinner());
        GameManager.instance().setTurnCount(data.getTurnCount());
        GameManager.instance().setGameBoard(data.getGameBoard());
        GameManager.instance().setWinningLinePosition(data.getWinningLinePosition());
        GameManager.instance().setCurrentTileClicked(Integer.parseInt(data.getCurrentTileClicked()));
        xScore = Integer.parseInt(data.getPlayer1Score());
        oScore = Integer.parseInt(data.getPlayer2Score());

        tvXscore.setText(String.valueOf(xScore));
        tvOscore.setText(String.valueOf(oScore));

        updateTurnText(GameManager.instance().getTurn());
        updateWinningLine();
    }

    private void setClickableForOnlineGame(String board){
        if (!isOnline) return;
        String[] strArr = board.split(",");
        for (int i = 0; i < strArr.length; i++) {
            if (strArr[i].equals("-1"))
                boardFragment.setClickableByIndex(i, true);
            else
                boardFragment.setClickableByIndex(i, false);
        }
    }

    private void updateGameBoardTiles(int index){
        boardFragment.updateGameBoardTiles(index);
    }

    private void onlineGameEventListener(){
        gameDataEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (!isVisible() || snapshot.getValue(GameDataForFirebase.class) == null) return;
                dataForFirebase = snapshot.getValue(GameDataForFirebase.class);
                updateDataFromFirebase(dataForFirebase);
                //updateGameBoardTiles(GameManager.instance().getGameBoardString());
                updateGameBoardTiles(GameManager.instance().getCurrentTileClicked());
                updateWinner(BaseGame.EnumWinner.valueOf(dataForFirebase.getWinner()));
                if (role.equals("X") &&
                        GameManager.instance().getTurn().equals(BaseGame.EnumTurn.X)) {
                    setClickableForOnlineGame(GameManager.instance().getGameBoardString());
                } else if (role.equals("O") &&
                        GameManager.instance().getTurn().equals(BaseGame.EnumTurn.O)) {
                    setClickableForOnlineGame(GameManager.instance().getGameBoardString());
                } else {
                    // boardFragment.setClickable(false);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        };
        ((MainActivity)getActivity()).registerDbEvents(MainActivity.eDBListenerType.Game, gameDataEventListener, roomName);
    }

    private void roomEventListener() {
        roomEvent = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {
                    //TODO: other player disconnected, new "you won" activity
                    startAnimations(topOutAnim, middleOutAnim, bottomOutAnim);
                    Utils.changeFragment(R.id.game_fragment_container, getParentFragmentManager().beginTransaction(),
                            MenuFragment.newInstance(), true);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
            }
        };
        ((MainActivity)getActivity()).registerDbEvents(MainActivity.eDBListenerType.CurrentRoom, roomEvent, roomName);
    }

    public void updateWinningLine(){
        String winningLine = GameManager.instance().getWinningLinePosition();
        switch (winningLine){
            case "row1":
                loadWinningLineImage(R.drawable.row1, View.VISIBLE);
                break;
            case "row2":
                loadWinningLineImage(R.drawable.row2, View.VISIBLE);
                break;
            case "row3":
                loadWinningLineImage(R.drawable.row3, View.VISIBLE);
                break;
            case "col1":
                loadWinningLineImage(R.drawable.col1, View.VISIBLE);
                break;
            case "col2":
                loadWinningLineImage(R.drawable.col2, View.VISIBLE);
                break;
            case "col3":
                loadWinningLineImage(R.drawable.col3, View.VISIBLE);
                break;
            case "rowcol1":
                loadWinningLineImage(R.drawable.rowcol1, View.VISIBLE);
                break;
            case "rowcol3":
                loadWinningLineImage(R.drawable.rowcol3, View.VISIBLE);
                break;
            default:
                loadWinningLineImage(R.drawable.blank, View.GONE);
                break;
        }
    }

    public void loadWinningLineImage(int drawable, int visibility){
        Picasso.get().load(drawable)
                .error(R.drawable.blank)
                .into(boardFragment.getIvWinningLine());
        boardFragment.getIvWinningLine().setVisibility(visibility);
    }

    public void updateTurnText(BaseGame.EnumTurn eTurn){
        if (eTurn.equals(BaseGame.EnumTurn.X)){
            tvWinner.setText(R.string.x_turn);
        } else {
            tvWinner.setText(R.string.o_turn);
        }
    }

    public void moveToWinningActivity(){
        BaseGame.EnumWinner winner = GameManager.instance().getWinner();
        Intent intent = new Intent(getContext(), WinnerActivity.class);

        if (isOnline) {
            if (winner == BaseGame.EnumWinner.X && role.equals("X")
                    || winner == BaseGame.EnumWinner.O && role.equals("O")) {
                // you won
                intent.putExtra("win", true);
            } else {
                // you lost
                intent.putExtra("win", false);
            }
        } else {
            intent.putExtra("winner", String.valueOf(winner));
        }
        intent.putExtra("isOnline", isOnline);
        intent.putExtra("role", role);

        startActivity(intent);
    }

    public void wait2SecondsAndStartNewGame(){
        new Handler(Looper.getMainLooper()).postDelayed(()->{
            GameManager.instance().startNewGame();
            boardFragment.cleanBoard();
            updateTurnText(GameManager.instance().getTurn());
            updateWinningLine();
        },2000);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isOnline) {
            ((MainActivity)getActivity()).unRegisterDBEvents(MainActivity.eDBListenerType.CurrentRoom, roomEvent, roomName);
            ((MainActivity)getActivity()).deleteRoomFromDB(roomName);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startAnimations(topInAnim, middleInAnim, bottomInAnim);
    }
}

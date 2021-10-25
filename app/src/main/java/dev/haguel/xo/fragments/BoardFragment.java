package dev.haguel.xo.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


import dev.haguel.xo.R;
import dev.haguel.xo.utils.EventClickListener;
import dev.haguel.xo.utils.GameManager;


public class BoardFragment extends Fragment {

    private ImageView[] tiles;
    private LinearLayout mainLinearLayout;


    private ImageView ivWinningLine;

    private EventClickListener eventClickListener;


    public BoardFragment() {
        // Required empty public constructor
    }

    public static BoardFragment newInstance(EventClickListener eventClickListener) {
        BoardFragment fragment = new BoardFragment();
        fragment.eventClickListener = eventClickListener;
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_board, container, false);

        ivWinningLine = view.findViewById(R.id.ivWinningLine);
        mainLinearLayout = view.findViewById(R.id.mainGameLinearLayout);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tiles = new ImageView[9];


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1.0f
        );

        // initializing tiles
        int containers = 3;
        int textInContainer = 3;
        mainLinearLayout.setOrientation(LinearLayout.VERTICAL);
        for (int i = 0, count = 0; i < containers; i++) {
            LinearLayout layout = new LinearLayout(getContext());
            for (int j = 0; j < textInContainer; j++, count++) {
                tiles[count] = new ImageView(getContext());
                tiles[count].setLayoutParams(params);
                layout.setLayoutParams(params);
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layout.addView(tiles[count]);
            }
            mainLinearLayout.addView(layout);
        }

        for (int i = 0; i < tiles.length; i++) {
            Picasso.get().load(R.drawable.blank)
                    .placeholder(R.drawable.blank)
                    .error(R.drawable.blank)
                    .into(tiles[i]);
            int tileIndex = i;
            tiles[i].setOnClickListener(v -> {
                eventClickListener.tileClickListener((ImageView) v, tileIndex);
            });
        }
    }




    public void cleanBoard(){
        for (int i = 0; i < tiles.length; i++) {
            updateGameBoardTiles(i);
        }
        setClickable(true);
    }


    public void setClickable(boolean bool){
        for (int i = 0; i < tiles.length; i++) {
            tiles[i].setClickable(bool);
        }
    }

    public void setClickableByIndex(int index, boolean clickable){
        tiles[index].setClickable(clickable);
    }

    public void updateGameBoardTiles(int index){
        switch (GameManager.instance().getGameBoard()[index]){
            case 0:
                loadImageTile(tiles[index], R.drawable.x);
                break;
            case 1:
                loadImageTile(tiles[index], R.drawable.o);
                break;
            default:
                loadImageTile(tiles[index], R.drawable.blank);
                break;
        }
    }

    public void loadImageTile(ImageView iv, int drawable){
        Picasso.get().load(drawable)
                .placeholder(R.drawable.blank)
                .error(R.drawable.blank)
                .into(iv, new Callback.EmptyCallback(){
                    @Override
                    public void onSuccess() {
                        super.onSuccess();
                        iv.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.tiles_fade_in_animation));
                    }
                });
    }

    public ImageView getIvWinningLine(){
        return ivWinningLine;
    }

}
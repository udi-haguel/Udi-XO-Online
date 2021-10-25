package dev.haguel.xo.fragments;

import android.animation.Animator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


import dev.haguel.xo.R;
import dev.haguel.xo.activity.MainActivity;
import dev.haguel.xo.utils.Utils;
import dev.haguel.xo.utils.YoYoCallback;

public class WaitingForPlayerFragment extends BaseGameFragment {


    private String roomName;
    private String playerName;
    private String role;

    private ValueEventListener currentRoomValueEventListener;

    private TextView exit;



    public WaitingForPlayerFragment(){

    }

    public static WaitingForPlayerFragment newInstance(String roomName, String playerName, String role) {
        WaitingForPlayerFragment fragment = new WaitingForPlayerFragment();
        Bundle args = new Bundle();
        args.putString("roomName", roomName);
        args.putString("playerName", playerName);
        args.putString("role", role);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_waiting_for_player, container, false);

        exit = view.findViewById(R.id.tvStopWaiting);

        top = view.findViewById(R.id.clWaitingTop);
        middle = view.findViewById(R.id.clWaitingMiddle);
        bottom = view.findViewById(R.id.clWaitingBottom);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() == null) return;
        Bundle bundle = getArguments();
        if (bundle == null || !bundle.containsKey("roomName") ||
                !bundle.containsKey("playerName") ||
                !bundle.containsKey("role")) return;
        roomName = bundle.getString("roomName");
        playerName = bundle.getString("playerName");
        role = bundle.getString("role");

        setAnimations();


        exit.setOnClickListener(v->{
            YoYo.with(Techniques.RubberBand)
                    .withListener(new YoYoCallback() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            startAnimations(topOutAnim, rotationOutAnim, bottomOutAnim);
                            Utils.changeFragment(R.id.game_fragment_container, getParentFragmentManager().beginTransaction(),
                                    MenuFragment.newInstance(), true);
                            ((MainActivity)getActivity()).unRegisterDBEvents(MainActivity.eDBListenerType.CurrentRoom, currentRoomValueEventListener, roomName);
                            ((MainActivity)getActivity()).deleteRoomFromDB(roomName);
                        }
                    })
                    .duration(500)
                    .repeat(0)
                    .playOn(v);
        });

        addCurrentRoomEventListener();
    }


    private void addCurrentRoomEventListener(){
        currentRoomValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // check if room has 3 children (player1, player2, roomName)
                if (snapshot.getChildrenCount() == 3){
                    startAnimations(topOutAnim, rotationOutAnim, bottomOutAnim);
                    Utils.changeFragment(R.id.game_fragment_container, getParentFragmentManager().beginTransaction(),
                            GameFragment.newInstance(roomName, playerName, role, true), true);
                    ((MainActivity)getActivity()).unRegisterDBEvents(MainActivity.eDBListenerType.CurrentRoom, currentRoomValueEventListener, roomName);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        ((MainActivity)getActivity()).registerDbEvents(MainActivity.eDBListenerType.CurrentRoom, currentRoomValueEventListener, roomName);
    }

    public String getPlayerName(){
        return playerName;
    }

    public String getRoomName(){
        return roomName;
    }

    @Override
    public void onResume() {
        super.onResume();
        startAnimations(topInAnim, rotationInAnim, bottomInAnim);
    }
}

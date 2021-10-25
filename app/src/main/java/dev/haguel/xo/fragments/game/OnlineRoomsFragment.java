package dev.haguel.xo.fragments.game;

import android.animation.Animator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dev.haguel.xo.R;
import dev.haguel.xo.activity.MainActivity;
import dev.haguel.xo.adapters.RoomAdapter;
import dev.haguel.xo.entities.Room;
import dev.haguel.xo.utils.CreateRoomDialog;
import dev.haguel.xo.utils.Utils;
import dev.haguel.xo.utils.YoYoCallback;

public class OnlineRoomsFragment extends BaseGameFragment implements CreateRoomDialog.CreateRoomDialogListener {

    private RecyclerView rvRooms;
    private TextView tvCreateRoom;
    private TextView tvRoomsExitToMenu;
    private String playerName = "";
    private String roomName = "";
    private String role = "";

    private List<Room> roomsList;

    private ValueEventListener roomsValueEventListener;

    private CreateRoomDialog createRoomDialog;

    public OnlineRoomsFragment() {
        // Required empty public constructor
    }

    public static OnlineRoomsFragment newInstance() {
        return new OnlineRoomsFragment();

    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_online_rooms, container, false);

        rvRooms = view.findViewById(R.id.rvRooms);
        tvCreateRoom = view.findViewById(R.id.tvCreateRoom);
        tvRoomsExitToMenu = view.findViewById(R.id.tvRoomsExitToMenu);
        top = view.findViewById(R.id.clOnlineRoomsTop);
        bottom = view.findViewById(R.id.clOnlineRoomsBottom);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setAnimations();

        toggleDialog(false);

        roomsList = new ArrayList<>();

        roomName = playerName;
        role = "X";

        createRoomDialog = new CreateRoomDialog();
        createRoomDialog.setCreateRoomDialogListener(this);

        // show if new room is available
        addRoomsEventListener();

        tvCreateRoom.setOnClickListener(v -> {
            YoYo.with(Techniques.RubberBand)
                    .withListener(new YoYoCallback() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            createRoomDialog.show(getParentFragmentManager(), "CREATE_ROOM_DIALOG");
                        }
                    })
                    .duration(500)
                    .repeat(0)
                    .playOn(tvCreateRoom);

        });

        tvRoomsExitToMenu.setOnClickListener(v->{
            YoYo.with(Techniques.RubberBand)
                    .withListener(new YoYoCallback() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            startAnimations(topOutAnim, null, bottomOutAnim);
                            Utils.changeFragment(R.id.game_fragment_container, getParentFragmentManager().beginTransaction(),
                                    MenuFragment.newInstance(), true);
                        }
                    })
                    .duration(500)
                    .repeat(0)
                    .playOn(tvRoomsExitToMenu);
        });
    }

    private void addRoomsEventListener(){
        roomsValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (getActivity() == null || !isVisible()) return;

                // show list of rooms
                roomsList.clear();
                Iterable<DataSnapshot> rooms = snapshot.getChildren();
                for (DataSnapshot room : rooms) {
                    //add rooms to the list
                    roomsList.add(room.getValue(Room.class));
                }

                // set recyclerview to show rooms, and handle room click
                RoomAdapter adapter = new RoomAdapter(roomsList);
                rvRooms.setLayoutManager(new LinearLayoutManager(getContext()));
                rvRooms.setAdapter(adapter);
                adapter.setOnRoomClickListener(position -> {
                    if (roomsList.get(position).getPlayer2() == null) {
                        // player2 does not exist, join room as player2
                        Room currentRoom = roomsList.get(position);
                        roomName = currentRoom.getRoomName();
                        role = "O";
                        playerName = "guest";

                        HashMap<String, String> player2 = new HashMap<>();
                        player2.put("roomName", currentRoom.getRoomName());
                        player2.put("player1", currentRoom.player1);
                        player2.put("player2", playerName);
                        ((MainActivity) getActivity()).setDBValue(MainActivity.eDBListenerType.CurrentRoom, player2, roomName);
                        startAnimations(topOutAnim, null, bottomOutAnim);
                        Utils.changeFragment(R.id.game_fragment_container, getParentFragmentManager().beginTransaction(),
                                GameFragment.newInstance(roomName, playerName, role, true), true);
                        ((MainActivity)getActivity()).unRegisterDBEvents(MainActivity.eDBListenerType.RoomsList, roomsValueEventListener, null);
                    } else {
                        Toast.makeText(getContext(), "Room is full", Toast.LENGTH_SHORT).show();
                    }
                });
                rvRooms.scrollToPosition(0);
                toggleDialog(false);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                toggleDialog(false);
            }
        };
        ((MainActivity)getActivity()).registerDbEvents(MainActivity.eDBListenerType.RoomsList, roomsValueEventListener, null);
    }


    @Override
    public void createRoom(String roomNameFromDialog) {
        if (checkIfRoomNameExist(roomNameFromDialog)){
            Toast.makeText(getContext(), "Room name already exist, enter a new name", Toast.LENGTH_SHORT).show();
            return;
        }
        roomName = roomNameFromDialog;
        tvCreateRoom.setText(R.string.room_was_created);
        tvCreateRoom.setEnabled(false);
        role = "X";
        playerName = "host";
        HashMap<String, String> player1 = new HashMap<>();
        player1.put("player1", playerName);
        player1.put("roomName", roomName);
        ((MainActivity)getActivity()).setDBValue(MainActivity.eDBListenerType.CurrentRoom, player1, roomName);
        startAnimations(topOutAnim, null, bottomOutAnim);
        Utils.changeFragment(R.id.game_fragment_container, getParentFragmentManager().beginTransaction(),
                WaitingForPlayerFragment.newInstance(roomName, playerName, role), true);
        createRoomDialog.dismiss();
    }


    public boolean checkIfRoomNameExist(String roomName){
        if (roomsList == null){
            roomsList = new ArrayList<>();
        }
        for (Room room : roomsList) {
            if (roomName.toLowerCase().equals(room.getRoomName().toLowerCase())){
                return true;
            }
        }
        return false;
    }

    private void toggleDialog(boolean toShow){
        if (((MainActivity)getActivity()) != null) {
            ((MainActivity)getActivity()).toggleDialog(toShow);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        startAnimations(topInAnim, null, bottomInAnim);
    }


}
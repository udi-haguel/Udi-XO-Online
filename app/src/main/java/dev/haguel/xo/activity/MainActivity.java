package dev.haguel.xo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dev.haguel.xo.R;
import dev.haguel.xo.fragments.GameFragment;
import dev.haguel.xo.fragments.MenuFragment;
import dev.haguel.xo.fragments.OnlineRoomsFragment;
import dev.haguel.xo.fragments.WaitingForPlayerFragment;
import dev.haguel.xo.utils.LoaderDialog;

public class MainActivity extends AppCompatActivity {

    public enum eDBListenerType {
        RoomsList,
        CurrentRoom,
        Game,
    }

    // loader
    private LoaderDialog loaderDialog;
    // ads
    private AdView mAdView;


    // DATABASE
    DatabaseReference dbRef;


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbRef = FirebaseDatabase.getInstance().getReference();


        loaderDialog = new LoaderDialog(this);
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.game_fragment_container, MenuFragment.newInstance());
        fragmentTransaction.commit();


    }

    public void toggleDialog(boolean toShow){
        if (toShow){
            if (!loaderDialog.isShowing())
                loaderDialog.show();
        } else {
            if(loaderDialog.isShowing())
                loaderDialog.dismiss();
        }
    }

    public void registerDbEvents(eDBListenerType type, ValueEventListener listener, String roomName) {
        switch (type){
            case RoomsList:
                dbRef.child("Rooms").addValueEventListener(listener);
                break;
            case CurrentRoom:
                dbRef.child("Rooms").child(roomName).addValueEventListener(listener);
                break;
            case Game:
                dbRef.child("Rooms").child(roomName).child("GameData").addValueEventListener(listener);
                break;
        }

    }
    public void unRegisterDBEvents(eDBListenerType type, ValueEventListener listener, String roomName){
        switch (type){
            case RoomsList:
                dbRef.child("Rooms").removeEventListener(listener);
                break;
            case CurrentRoom:
                dbRef.child("Rooms").child(roomName).removeEventListener(listener);
                break;
            case Game:
                dbRef.child("Rooms").child(roomName).child("GameData").removeEventListener(listener);
                break;
        }
    }

    public void setDBValue(eDBListenerType type, Object data, String roomName){
        switch (type){
            case RoomsList:
                dbRef.child("Rooms").setValue(data);
                break;
            case CurrentRoom:
                dbRef.child("Rooms").child(roomName).setValue(data);
                break;
            case Game:
                dbRef.child("Rooms").child(roomName).child("GameData").setValue(data);
                break;
        }
    }

    public void deleteRoomFromDB(String roomName){
        dbRef.child("Rooms").child(roomName).removeValue();
    }


    @Override
    public void onBackPressed() {
        // Get Current Fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        Fragment frag = getSupportFragmentManager().findFragmentById(R.id.game_fragment_container);

        if (frag != null){
            if (frag instanceof GameFragment){

                fragmentTransaction.replace(R.id.game_fragment_container, MenuFragment.newInstance());
                fragmentTransaction.commit();

            } else if (frag instanceof OnlineRoomsFragment){

                fragmentTransaction.replace(R.id.game_fragment_container, MenuFragment.newInstance());
                fragmentTransaction.commit();

            } else if (frag instanceof WaitingForPlayerFragment){

                String roomName = ((WaitingForPlayerFragment) frag).getRoomName();
                deleteRoomFromDB(roomName);
                fragmentTransaction.replace(R.id.game_fragment_container, OnlineRoomsFragment.newInstance());
                fragmentTransaction.commit();

            }
            return;
        }
        super.onBackPressed();
    }
}

package dev.haguel.xo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.RunnableFuture;

import dev.haguel.xo.R;
import dev.haguel.xo.fragments.game.GameFragment;
import dev.haguel.xo.fragments.game.MenuFragment;
import dev.haguel.xo.fragments.game.OnlineRoomsFragment;
import dev.haguel.xo.fragments.game.WaitingForPlayerFragment;
import dev.haguel.xo.utils.LoaderDialog;

public class MainActivity extends AppCompatActivity {

    // 1) UI THREAD / MAIN THREAD == (UI CHANGES)
    // -/-^-/-/-/-----------------
    //

    // 2) Thread == The Rest
    // -------
    // -------

    public enum eDBListenerType {
        RoomsList,
        CurrentRoom,
        Game,
    }

    // FIREBASE USER
    private FirebaseUser user;
    private String userID;
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

        /*
        ImageView imageView3 = findViewById(R.id.imageView3);
        RotateAnimation rotate  = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setDuration(1000);
        imageView3.startAnimation(rotate);


        ArrayList<TextView> tvs = new ArrayList<>();
        LinearLayout layout = findViewById(R.id.l1);


        Thread run = new Thread(new Runnable() {
            @Override
            public void run() {
                for (TextView tv : tvs) {
                    layout.post(() -> {
                        layout.addView(tv);
                    });
                }
            }
        });

       Thread threead = new Thread(new Runnable() {
           @Override
           public void run() {

               for(int i=0; i<10_000; i++) {
                   TextView tv = new TextView(MainActivity.this);
                   tv.setText("hello");
                   tvs.add(tv);
               }
               run.start();
           }
       });
       threead.start();

         */





        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, AuthenticationActivity.class));
        } else {
            user = FirebaseAuth.getInstance().getCurrentUser();
            userID = user.getUid();
        }
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

                String playerName = ((WaitingForPlayerFragment) frag).getPlayerName();
                String roomName = ((WaitingForPlayerFragment) frag).getRoomName();
                deleteRoomFromDB(roomName);
                fragmentTransaction.replace(R.id.game_fragment_container, OnlineRoomsFragment.newInstance(playerName));
                fragmentTransaction.commit();

            }
            return;
        }
        super.onBackPressed();
    }
}

/*
1) Handle On Back Press
2) Add Yoyo Animation
3) Add FadeAnimation Between Fragments
4) Console, Upload App "UDI XOXO"
5) Async Task
 */
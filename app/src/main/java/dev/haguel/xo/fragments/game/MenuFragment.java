package dev.haguel.xo.fragments.game;

import android.animation.Animator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import dev.haguel.xo.R;
import dev.haguel.xo.activity.MainActivity;
import dev.haguel.xo.utils.Utils;
import dev.haguel.xo.utils.YoYoCallback;


public class MenuFragment extends BaseGameFragment {

    public static final String ONLINE_ROOMS_FRAGMENT_TAG = "online_rooms_fragment_tag";
    public static final String GAME_FRAGMENT_TAG = "game_fragment_tag";

    private TextView tvLocalGame;
    private TextView tvOnlineGame;


    public MenuFragment() {
        // Required empty public constructor
    }

    public static MenuFragment newInstance() {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        tvLocalGame = view.findViewById(R.id.tvLocalGame);
        tvOnlineGame = view.findViewById(R.id.tvOnlineGame);

        top = view.findViewById(R.id.layoutTopMenu);
        middle = view.findViewById(R.id.layoutMiddleMenu);
        bottom = view.findViewById(R.id.layoutBottomMenu);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setAnimations();



        tvLocalGame.setOnClickListener(v -> {
            YoYo.with(Techniques.RubberBand)
                    .withListener(new YoYoCallback() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            startAnimations(topOutAnim, middleOutAnim, bottomOutAnim);
                            Utils.changeFragment(R.id.game_fragment_container, getParentFragmentManager().beginTransaction(),
                                    GameFragment.newInstance("", "",  "", false), GAME_FRAGMENT_TAG, true);
                        }
                    })
                    .duration(500)
                    .repeat(0)
                    .playOn(tvLocalGame);

        });


        tvOnlineGame.setOnClickListener(v -> {
            YoYo.with(Techniques.RubberBand)
                    .withListener(new YoYoCallback() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (getActivity() == null) return;
                            ((MainActivity)getActivity()).toggleDialog(true);
                            createOrJoinRoom();
                        }
                    })
                    .duration(500)
                    .repeat(0)
                    .playOn(tvOnlineGame);

        });

    }


    private void createOrJoinRoom() {
        Fragment frag = getParentFragmentManager().findFragmentById(R.id.game_fragment_container);
        if ((frag == null || frag instanceof MenuFragment)) {
            // join the room
            startAnimations(topOutAnim, middleOutAnim, bottomOutAnim);
            Utils.changeFragment(R.id.game_fragment_container, getParentFragmentManager().beginTransaction(),
                    OnlineRoomsFragment.newInstance(), ONLINE_ROOMS_FRAGMENT_TAG, true);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startAnimations(topInAnim, middleInAnim, bottomInAnim);
    }
}

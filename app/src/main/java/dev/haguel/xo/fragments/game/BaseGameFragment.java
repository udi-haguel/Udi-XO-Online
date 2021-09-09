package dev.haguel.xo.fragments.game;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import dev.haguel.xo.R;

public abstract class BaseGameFragment extends Fragment {

    protected ConstraintLayout top, middle, bottom;
    protected Animation topInAnim, middleInAnim, bottomInAnim, rotationInAnim;
    protected Animation topOutAnim, middleOutAnim, bottomOutAnim, rotationOutAnim;

    protected void setAnimations(){
        topInAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.top_in_animation);
        middleInAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.middle_in_animation);
        bottomInAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_in_animation);
        rotationInAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.middle_in_rotation_animation);

        topOutAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.top_out_animation);
        middleOutAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.middle_out_animation);
        bottomOutAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_out_animation);
        rotationOutAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.middle_out_rotation_animation);
    }

    protected void startAnimations(Animation topAnim, Animation middleAnim, Animation bottomAnim){
        top.startAnimation(topAnim);
        bottom.startAnimation(bottomAnim);
        if (middleAnim != null)
            middle.startAnimation(middleAnim);
    }

}

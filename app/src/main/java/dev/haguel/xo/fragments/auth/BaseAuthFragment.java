package dev.haguel.xo.fragments.auth;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import dev.haguel.xo.R;

public abstract class BaseAuthFragment extends Fragment {

    protected ConstraintLayout top, middle, bottom;
    protected LinearLayout xo1, xo2;

    protected Animation topInAnim, middleInAnim, bottomInAnim, xoInAnim;
    protected Animation topOutAnim, middleOutAnim, bottomOutAnim, xoOutAnim;

    protected void setAnimations(){
        topInAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.top_in_animation);
        middleInAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.middle_in_animation);
        bottomInAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_in_animation);
        xoInAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_in_animation);

        topOutAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.top_out_animation);
        middleOutAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.middle_out_animation);
        bottomOutAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_out_animation);
        xoOutAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_out_animation);
    }

    protected void startAnimations(Animation topAnim, Animation middleAnim, Animation bottomAnim, Animation xoAnim){
        top.startAnimation(topAnim);
        middle.startAnimation(middleAnim);
        bottom.startAnimation(bottomAnim);
        xo1.startAnimation(xoAnim);
        xo2.startAnimation(xoAnim);
    }
}

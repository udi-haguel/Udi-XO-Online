package dev.haguel.xo.fragments.auth;


import android.animation.Animator;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.NotNull;

import dev.haguel.xo.R;
import dev.haguel.xo.activity.AuthenticationActivity;
import dev.haguel.xo.utils.Utils;
import dev.haguel.xo.utils.YoYoCallback;


public class ForgotPasswordFragment extends BaseAuthFragment {

    private EditText etEmail;
    private TextView tvReset;
    private TextView tvLogin;
    private TextView tvRegister;

    private String emailForLogin = "";

    FirebaseAuth auth;

    public static ForgotPasswordFragment newInstance() {
        ForgotPasswordFragment forgotPasswordFrag = new ForgotPasswordFragment();
        return forgotPasswordFrag;
    }


    @Nullable
   // @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        etEmail = view.findViewById(R.id.etForgotPasswordEmail);
        tvReset = view.findViewById(R.id.tvResetPassword);
        tvLogin = view.findViewById(R.id.tvBackToLogin);
        tvRegister = view.findViewById(R.id.tvBackToRegister);

        top = view.findViewById(R.id.clForgotTop);
        middle = view.findViewById(R.id.clForgotMiddle);
        bottom = view.findViewById(R.id.clForgotBottom);
        xo1 = view.findViewById(R.id.forgotXO1);
        xo2 = view.findViewById(R.id.forgotXO2);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth = FirebaseAuth.getInstance();

        setAnimations();

        tvReset.setOnClickListener(v->{
            YoYo.with(Techniques.RubberBand)
                    .withListener(new YoYoCallback() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            resetPassword();
                        }
                    })
                    .duration(500)
                    .repeat(0)
                    .playOn(tvReset);
        });

        tvLogin.setOnClickListener(v->{
            YoYo.with(Techniques.RubberBand)
                    .withListener(new YoYoCallback() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            startAnimations(topOutAnim, middleOutAnim, bottomOutAnim, xoOutAnim);
                            Utils.changeFragment(R.id.auth_fragment_container, getParentFragmentManager().beginTransaction(),
                                    LoginFragment.newInstance(emailForLogin), true);
                        }
                    })
                    .duration(500)
                    .repeat(0)
                    .playOn(tvLogin);
        });

        tvRegister.setOnClickListener(v->{
            YoYo.with(Techniques.RubberBand)
                    .withListener(new YoYoCallback() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            startAnimations(topOutAnim, middleOutAnim, bottomOutAnim, xoOutAnim);
                            Utils.changeFragment(R.id.auth_fragment_container, getParentFragmentManager().beginTransaction(),
                                    RegisterFragment.newInstance(), true);
                        }
                    })
                    .duration(500)
                    .repeat(0)
                    .playOn(tvRegister);
        });


    }

    private void resetPassword(){
        String email = etEmail.getText().toString().trim();

        if (email.isEmpty()){
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Please provide a valid email");
            etEmail.requestFocus();
            return;
        }

        ((AuthenticationActivity)getActivity()).toggleDialog(true);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
           if (task.isSuccessful()){
               Toast.makeText(getContext(), "Check your email to reset your password", Toast.LENGTH_LONG).show();
               emailForLogin = etEmail.getText().toString().trim();
               etEmail.setText("");
           } else {
               Toast.makeText(getContext(), "Try again! something went wrong, or email does not exist", Toast.LENGTH_LONG).show();
           }
            ((AuthenticationActivity)getActivity()).toggleDialog(false);
        });



    }

    @Override
    public void onResume() {
        super.onResume();
        startAnimations(topInAnim, middleInAnim, bottomInAnim, xoInAnim);
    }
}

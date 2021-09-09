package dev.haguel.xo.fragments.auth;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.NotNull;

import dev.haguel.xo.R;
import dev.haguel.xo.activity.AuthenticationActivity;
import dev.haguel.xo.activity.MainActivity;
import dev.haguel.xo.utils.Utils;
import dev.haguel.xo.utils.YoYoCallback;


public class LoginFragment extends BaseAuthFragment {

    public static final String EMAIL_KEY = "EMAIL_KEY";

    private EditText etLoginEmail;
    private EditText etLoginPassword;
    private TextView tvLogin;
    private TextView tvRegister;
    private TextView tvForgotPassword;

    private FirebaseAuth mAuth;


    public static LoginFragment newInstance(String email) {
        LoginFragment loginFrag = new LoginFragment();

        Bundle bundle = new Bundle();
        bundle.putString(EMAIL_KEY, email);
        loginFrag.setArguments(bundle);

        return loginFrag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        etLoginEmail = view.findViewById(R.id.etLoginEmail);
        etLoginPassword = view.findViewById(R.id.etLoginPassword);
        tvLogin = view.findViewById(R.id.tvLogin);
        tvRegister = view.findViewById(R.id.tvRegisterBtn);
        tvForgotPassword = view.findViewById(R.id.tvForgotPassword);

        top = view.findViewById(R.id.clLoginTop);
        middle = view.findViewById(R.id.clLoginMiddle);
        bottom = view.findViewById(R.id.clLoginBottom);
        xo1 = view.findViewById(R.id.loginXO1);
        xo2 = view.findViewById(R.id.loginXO2);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setAnimations();

        String email;
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(EMAIL_KEY)) {
            email = bundle.getString(EMAIL_KEY);
        } else {
            email = "";
        }


        etLoginEmail.setText(email);

        tvLogin.setOnClickListener(v -> {
            YoYo.with(Techniques.RubberBand)
                    .withListener(new YoYoCallback() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            inputValidateAndLoginUser();
                        }
                    })
                    .duration(500)
                    .repeat(0)
                    .playOn(tvLogin);

        });

        tvRegister.setOnClickListener(v -> {
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

        tvForgotPassword.setOnClickListener(v -> {
            YoYo.with(Techniques.RubberBand)
                    .withListener(new YoYoCallback() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            startAnimations(topOutAnim, middleOutAnim, bottomOutAnim, xoOutAnim);
                            Utils.changeFragment(R.id.auth_fragment_container, getParentFragmentManager().beginTransaction(),
                                    ForgotPasswordFragment.newInstance(), true);
                        }
                    })
                    .duration(500)
                    .repeat(0)
                    .playOn(tvForgotPassword);

        });

        mAuth = FirebaseAuth.getInstance();

    }


    private void inputValidateAndLoginUser() {
        String email = etLoginEmail.getText().toString().trim();
        String password = etLoginPassword.getText().toString().trim();


        if (email.isEmpty()){
            etLoginEmail.setError("Email is required");
            etLoginEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etLoginEmail.setError("Please enter a valid email");
            etLoginEmail.requestFocus();
            return;
        }
        if (password.isEmpty()){
            etLoginPassword.setError("Password is required");
            etLoginPassword.requestFocus();
            return;
        }
        if (password.length() < 6){
            etLoginPassword.setError("Min password length is 6 characters");
            etLoginPassword.requestFocus();
            return;
        }

        ((AuthenticationActivity)getActivity()).toggleDialog(true);
        loginUser(email, password);
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        startActivity(new Intent(getContext(), MainActivity.class));
                    } else {
                        Toast.makeText(getActivity(), "Failed to log in", Toast.LENGTH_LONG).show();
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
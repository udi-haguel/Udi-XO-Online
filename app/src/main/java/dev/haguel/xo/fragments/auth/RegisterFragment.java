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
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import dev.haguel.xo.R;
import dev.haguel.xo.activity.AuthenticationActivity;
import dev.haguel.xo.activity.MainActivity;
import dev.haguel.xo.entities.User;
import dev.haguel.xo.utils.Utils;
import dev.haguel.xo.utils.YoYoCallback;


public class RegisterFragment extends BaseAuthFragment {


    private EditText etRegisterName;
    private EditText etRegisterEmail;
    private EditText etRegisterPassword;
    private EditText etRegisterPassword2;
    private TextView tvRegister;
    private TextView tvAlreadyMember;

    private FirebaseAuth mAuth;


    public static RegisterFragment newInstance() {
        RegisterFragment registerFrag = new RegisterFragment();
        return registerFrag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        etRegisterName = view.findViewById(R.id.etRegisterName);
        etRegisterEmail = view.findViewById(R.id.etRegisterEmail);
        etRegisterPassword = view.findViewById(R.id.etRegisterPassword);
        etRegisterPassword2 = view.findViewById(R.id.etRegisterPassword2);
        tvRegister = view.findViewById(R.id.tvRegister);
        tvAlreadyMember = view.findViewById(R.id.tvAlreadyMember);

        top = view.findViewById(R.id.clRegisterTop);
        middle = view.findViewById(R.id.clRegisterMiddle);
        bottom = view.findViewById(R.id.clRegisterBottom);
        xo1 = view.findViewById(R.id.registerXO1);
        xo2 = view.findViewById(R.id.registerXO2);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mAuth = FirebaseAuth.getInstance();

        setAnimations();

        tvAlreadyMember.setOnClickListener(v -> {
            YoYo.with(Techniques.RubberBand)
                    .withListener(new YoYoCallback() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            startAnimations(topOutAnim, middleOutAnim, bottomOutAnim, xoOutAnim);
                            Utils.changeFragment(R.id.auth_fragment_container, getParentFragmentManager().beginTransaction(),
                                    LoginFragment.newInstance(""), true);
                        }
                    })
                    .duration(500)
                    .repeat(0)
                    .playOn(tvAlreadyMember);
        });


        tvRegister.setOnClickListener(v->{
            YoYo.with(Techniques.RubberBand)
                    .withListener(new YoYoCallback() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            checkUserValidationAndRegister();
                        }
                    })
                    .duration(500)
                    .repeat(0)
                    .playOn(tvRegister);

        });
    }

    private void checkUserValidationAndRegister(){
        String fullName = etRegisterName.getText().toString().trim();
        String email = etRegisterEmail.getText().toString().trim();
        String password = etRegisterPassword.getText().toString().trim();
        String password2 = etRegisterPassword2.getText().toString().trim();

        if (fullName.isEmpty()){
            etRegisterName.setError("Full name is required!");
            etRegisterName.requestFocus();
            return;
        }
        if (email.isEmpty()){
            etRegisterEmail.setError("Email is required!");
            etRegisterEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etRegisterEmail.setError("Please provide valid email");
            etRegisterEmail.requestFocus();
            return;
        }
        if (password.isEmpty()){
            etRegisterPassword.setError("Passowrd is required");
            etRegisterPassword.requestFocus();
            return;
        }
        if (password.length() < 6){
            etRegisterPassword.setError("Min password length should be 6 characters");
            etRegisterPassword.requestFocus();
            return;
        }
        if (password2.isEmpty()){
            etRegisterPassword2.setError("Required field");
            etRegisterPassword2.requestFocus();
            return;
        }
        if (!password.equals(password2)){
            etRegisterPassword2.setError("Password does not match");
            etRegisterPassword2.requestFocus();
            return;
        }

        ((AuthenticationActivity)getActivity()).toggleDialog(true);
        registerUser(fullName, email, password);
    }

    public void registerUser(String fullName, String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(registerTask -> {

                    if (registerTask.isSuccessful()){
                        User user = new User(fullName, email);
                        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(userID).setValue(user);

                        ((AuthenticationActivity)getActivity()).toggleDialog(false);
                        startActivity(new Intent(getContext(), MainActivity.class));
                        startAnimations(topOutAnim, middleOutAnim, bottomOutAnim, xoOutAnim);
                        Utils.changeFragment(R.id.auth_fragment_container, getParentFragmentManager().beginTransaction(),
                                LoginFragment.newInstance(etRegisterEmail.getText().toString().trim()), true);
                    } else {
                        Toast.makeText(getActivity(), "Failed to register", Toast.LENGTH_LONG).show();
                        ((AuthenticationActivity)getActivity()).toggleDialog(false);
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        startAnimations(topInAnim, middleInAnim, bottomInAnim, xoInAnim);
    }
}
package dev.haguel.xo.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import dev.haguel.xo.R;
import dev.haguel.xo.fragments.auth.LoginFragment;
import dev.haguel.xo.utils.LoaderDialog;


public class AuthenticationActivity extends AppCompatActivity {

    // loader
    private LoaderDialog loaderDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        loaderDialog = new LoaderDialog(this);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.auth_fragment_container, LoginFragment.newInstance(""));
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
}
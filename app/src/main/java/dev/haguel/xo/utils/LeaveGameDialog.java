package dev.haguel.xo.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import org.jetbrains.annotations.NotNull;

import dev.haguel.xo.R;

public class LeaveGameDialog extends AppCompatDialogFragment {

    private TextView tvLeaveGame;
    private TextView tvCancel;


    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.leave_game_dialog, null);

        tvCancel = view.findViewById(R.id.btnCancelLeavingDialog);
        tvLeaveGame = view.findViewById(R.id.btnLeaveGameLeavingDialog);

        builder.setView(view);


        return builder.create();
    }



}

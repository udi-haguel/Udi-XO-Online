package dev.haguel.xo.utils;



import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

import dev.haguel.xo.R;

public class CreateRoomDialog extends AppCompatDialogFragment {

    private EditText etRoomName;
    private TextView tvRndNumber;
    private TextView tvCreateRoom;
    private TextView tvCancel;
    private CreateRoomDialogListener listener;
    private int rndNumber;


    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.create_room_dialog, null);


        etRoomName = view.findViewById(R.id.etRoomName);
        tvRndNumber = view.findViewById(R.id.tvRndNumber);
        tvCreateRoom = view.findViewById(R.id.tvCreateRoom);
        tvCancel = view.findViewById(R.id.tvCancel);

        builder.setView(view);

        return builder.create();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    public void setCreateRoomDialogListener(CreateRoomDialogListener listener){
        this.listener = listener;
    }


    @Override
    public void onResume() {
        super.onResume();



        //getDialog().getWindow().getAttributes().windowAnimations = R.style.dialog_animation;

        rndNumber = new Random().nextInt(9000) + 1000;
        tvRndNumber.setText(String.valueOf(rndNumber));

        tvCreateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateRoomName(etRoomName.getText().toString().trim())) {
                    listener.createRoom(etRoomName.getText().toString().trim() + "@" + tvRndNumber.getText().toString().trim());
                }
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() == null) return;
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setWindowAnimations(R.style.dialog_animation);
    }

    private boolean validateRoomName(String roomName){
        if (roomName.isEmpty()){
            etRoomName.setError("Room name is required!");
            etRoomName.requestFocus();
            return false;
        }

        if (roomName.length() < 3){
            etRoomName.setError("Min room name length is 3 characters");
            etRoomName.requestFocus();
            return false;
        }

        if (roomName.length() > 8){
            etRoomName.setError("Max room name length is 8 characters");
            etRoomName.requestFocus();
            return false;
        }

        if (!roomName.matches("[a-zA-z]+")){
            etRoomName.setError("Room name can only contain letters, no symbols, spaces or numbers");
            etRoomName.requestFocus();
            return false;
        }

        return true;
    }

    public interface CreateRoomDialogListener{
        void createRoom(String roomNameFromDialog);
    }
}

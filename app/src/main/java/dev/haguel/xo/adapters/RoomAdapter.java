package dev.haguel.xo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import dev.haguel.xo.R;
import dev.haguel.xo.entities.Room;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder>{

    private List<Room> roomList;
    private OnRoomClickListener mListener;


    public RoomAdapter(List<Room> roomList) {
        this.roomList = roomList;
    }

    public interface OnRoomClickListener {
        void onRoomClick(int position);
    }

    public void setOnRoomClickListener(OnRoomClickListener listener){
        mListener = listener;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.room_item, parent, false);
        return new ViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RoomAdapter.ViewHolder holder, int position) {
        Room room = roomList.get(position);

        holder.tvRoomName.setText(room.roomName.replace("@", "#"));

        if (room.player2 == null){
            holder.tvRoomDetails.setText(R.string.join_room);
        } else {
            holder.tvRoomDetails.setText(R.string.room_is_full);
        }


    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvRoomName;
        TextView tvRoomDetails;

        public ViewHolder(@NonNull @NotNull View itemView, OnRoomClickListener listener) {
            super(itemView);
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            tvRoomDetails = itemView.findViewById(R.id.tvRoomDetails);


            itemView.setOnClickListener(v->{
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    listener.onRoomClick(position);
                }
            });
        }
    }
}

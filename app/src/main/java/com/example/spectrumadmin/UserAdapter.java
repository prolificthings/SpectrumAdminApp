package com.example.spectrumadmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    private final Context mContext;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private List<User> userList = new ArrayList<>();

    public UserAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<User> users) {
        userList.clear();
        userList.addAll(users);
        notifyDataSetChanged();
    }

    @Override
    public UserAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final UserAdapter.MyViewHolder holder, final int position) {
        final User user = userList.get(position);

        holder.myTextView.setText(user.getName());
        holder.myCountryTextView.setText(user.getCountry());
        holder.myWeight.setText(String.valueOf(user.getWeight()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, " " + userList.get(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.myButtonDelete.setText("Delete");

        // delete user from firebase database based upon the key
       holder.myButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Query deleteQuery = databaseReference.child("users").orderByChild("name").equalTo(userList.get(position).toString());
                databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User userTemp = snapshot.getValue(User.class);
                            if (user.getName().equals(userTemp.getName())) {
                                databaseReference.child("users").child(snapshot.getKey().toString()).removeValue();
                                userList.remove(position);
                                notifyDataSetChanged();
                                if (userList.size() == 0) {
                                    MainActivity.textViewEmptyView.setVisibility(View.VISIBLE);
                                }
                                break;

                            }

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView myTextView;
        public TextView myCountryTextView;
        public TextView myWeight;
        public Button myButtonDelete;

        public MyViewHolder(View itemView) {
            super(itemView);
            myTextView = (TextView) itemView.findViewById(R.id.tvName);
            myCountryTextView = (TextView) itemView.findViewById(R.id.tvCountry);
            myWeight=(TextView) itemView.findViewById(R.id.tvWeight);
            myButtonDelete = (Button) itemView.findViewById(R.id.deleteButton);
        }
    }
}

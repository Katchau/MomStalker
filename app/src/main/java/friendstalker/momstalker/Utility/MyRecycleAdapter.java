package friendstalker.momstalker.Utility;

import android.content.Context;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import friendstalker.momstalker.AndroidUser;
import friendstalker.momstalker.Connection;
import friendstalker.momstalker.R;

/**
 * Created by alcin on 27/05/2017.
 */

public class MyRecycleAdapter extends RecyclerView.Adapter<MyRecycleAdapter.ViewHolder>{
    private List<String> listItems;
    private Context context;
    private String type;

    public MyRecycleAdapter(List<String> listItems, String type, Context context){
        this.listItems = listItems;
        this.context = context;
        this.type = type;
    }

    @Override
    public MyRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_test,parent,false);
        return new ViewHolder(v);
    }

    public void clickActionFriend(final MyRecycleAdapter.ViewHolder holder,final int position){
        String obj = listItems.get(position);

        holder.button.setText(obj);
        if(AndroidUser.friends.get(position).active){
            holder.button.setTextColor(Color.RED);
        }
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User friend = AndroidUser.friends.get(position);
                friend.changeState();
                Button b = (Button) v.findViewById(R.id.button);
                if(b != null){
                    if(friend.active)b.setTextColor(Color.RED);
                    else b.setTextColor(Color.BLACK);
                }
            }
        });
    }

    public void clickActionEvents(final MyRecycleAdapter.ViewHolder holder, final int position){
        String obj = listItems.get(position);

        holder.button.setText(obj);
        if(AndroidUser.myEvents.get(position).active){
            holder.button.setTextColor(Color.RED);
        }
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event event = AndroidUser.myEvents.get(position);
                event.changeState();
                Button b = (Button) v.findViewById(R.id.button);
                if(b != null){
                    if(event.active)b.setTextColor(Color.RED);
                    else b.setTextColor(Color.BLACK);
                }
            }
        });
    }

    public void clickActionEvents2(final MyRecycleAdapter.ViewHolder holder, final int position){
        String obj = listItems.get(position);

        holder.button.setText(obj);
        if(AndroidUser.events.get(position).active){
            holder.button.setTextColor(Color.RED);
        }
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event event = AndroidUser.events.get(position);
                event.changeState();
                Button b = (Button) v.findViewById(R.id.button);
                if(b != null){
                    if(event.active)b.setTextColor(Color.RED);
                    else b.setTextColor(Color.BLACK);
                }
            }
        });
    }

    public void clickActionRequest(final MyRecycleAdapter.ViewHolder holder,final int position){
        String obj = listItems.get(position);
        holder.button.setText(obj);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User friend = AndroidUser.friendRequests.get(position);
                Button b = (Button) v.findViewById(R.id.button);
                int id1 = AndroidUser.user.id;
                int id2 = friend.id;
                if(id2 > id1){
                    id2 = id1;
                    id1 = friend.id;
                }
                if(Connection.createAmizade(id1,id2)){
                    b.setVisibility(View.INVISIBLE);
                }
                else{
                    b.setText(b.getText() + " failed accepting request!");
                }
            }
        });
    }

    @Override
    public void onBindViewHolder(final MyRecycleAdapter.ViewHolder holder, final int position) {
        if(type.equals("Friend"))
            clickActionFriend(holder,position);
        if(type.equals("Request"))
            clickActionRequest(holder,position);
        if(type.equals("Mine"))
            clickActionEvents(holder,position);
        if(type.equals("Event"))
            clickActionEvents2(holder,position);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public Button button;

        public ViewHolder(View view){
            super(view);
            button = (Button) view.findViewById(R.id.button);
        }
    }
}

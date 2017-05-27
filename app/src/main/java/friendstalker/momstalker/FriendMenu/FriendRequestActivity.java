package friendstalker.momstalker.FriendMenu;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import friendstalker.momstalker.AndroidUser;
import friendstalker.momstalker.R;
import friendstalker.momstalker.Utility.MyRecycleAdapter;
import friendstalker.momstalker.Utility.User;

public class FriendRequestActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerFriend);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        initFirstData();
        recyclerView.setAdapter(new MyRecycleAdapter(list,"Request",this));
    }

    public void initFirstData(){
        list = new ArrayList<>();
        for(User u : AndroidUser.friendRequests){
            list.add(u.name);
        }
    }
}

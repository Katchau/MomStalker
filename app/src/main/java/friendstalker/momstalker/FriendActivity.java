package friendstalker.momstalker;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import friendstalker.momstalker.FriendMenu.CreateRequestActivity;
import friendstalker.momstalker.FriendMenu.DeleteFriendActivity;
import friendstalker.momstalker.FriendMenu.FriendRequestActivity;
import friendstalker.momstalker.FriendMenu.ViewFriendsActivity;

public class FriendActivity extends AppCompatActivity {
    private Button view = null;
    private Button request = null;
    private Button add = null;
    private Button remove = null;
    private TextView errorMsg = null;
    private Resources res = null;
    private String err = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //must have in order to make http requests
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        view = (Button) findViewById(R.id.friend1);
        request = (Button) findViewById(R.id.friend2);
        add = (Button) findViewById(R.id.friend3);
        remove = (Button) findViewById(R.id.friend4);
        errorMsg = (TextView) findViewById(R.id.errorMsgFriend);

        res = getResources();
        err = res.getString(R.string.error_invalid_action);
        createViewListener(this);
        createRequestListener(this);
        createAddListener(this);
        createRemoveListener(this);
    }

    private void createViewListener(final FriendActivity friend){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidUser.friends = Connection.getAmizades(AndroidUser.user.id);
                if(AndroidUser.friends != null && AndroidUser.friends.size() > 0){
                    Intent intent = new Intent(friend,ViewFriendsActivity.class);
                    startActivity(intent);
                }
                else{
                    errorMsg.setText(err);
                    errorMsg.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void createRequestListener(final FriendActivity friend){
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidUser.friendRequests = Connection.getFRequests(AndroidUser.user.id);
                if(AndroidUser.friendRequests != null && AndroidUser.friendRequests.size() > 0){
                    Intent intent = new Intent(friend,FriendRequestActivity.class);
                    startActivity(intent);
                }
                else{
                    errorMsg.setText(err);
                    errorMsg.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void createAddListener(final FriendActivity friend){
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(friend,CreateRequestActivity.class);
                startActivity(intent);
            }
        });
    }

    private void createRemoveListener(final FriendActivity friend){
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(friend,DeleteFriendActivity.class);
                startActivity(intent);
            }
        });
    }
}

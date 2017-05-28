package friendstalker.momstalker.FriendMenu;

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

import friendstalker.momstalker.AndroidUser;
import friendstalker.momstalker.Connection;
import friendstalker.momstalker.R;
import friendstalker.momstalker.Utility.User;

public class DeleteFriendActivity extends AppCompatActivity {
    private EditText username = null;
    private Button remove = null;
    private TextView errorMsg = null;
    private Resources res = null;
    private String err = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_friend);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        username = (EditText) findViewById(R.id.removeFriendText);
        remove = (Button) findViewById(R.id.removeFriendB);
        errorMsg = (TextView) findViewById(R.id.errorMsgRemoveF);

        res = getResources();
        err = res.getString(R.string.error_invalid_username);

        createButtonListener(this);
    }

    private void createButtonListener(final DeleteFriendActivity deleteFriendActivity){
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                int friendId = 0;
                int ownId = AndroidUser.user.id;
                for(User u : AndroidUser.friends){
                    if(u.name.equals(user)){
                        friendId = u.id;
                        break;
                    }
                }
                if(friendId > 0){
                    if(ownId < friendId)
                        Connection.deleteAmizade(ownId,friendId);
                    else
                        Connection.deleteAmizade(friendId,ownId);

                    Intent intent = new Intent(deleteFriendActivity,ViewFriendsActivity.class);
                    startActivity(intent);
                }
                else{
                    errorMsg.setText(err);
                    errorMsg.setVisibility(View.VISIBLE);
                }
            }
        });
    }

}

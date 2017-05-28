package friendstalker.momstalker.EventMenu;

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
import friendstalker.momstalker.FriendMenu.DeleteFriendActivity;
import friendstalker.momstalker.FriendMenu.ViewFriendsActivity;
import friendstalker.momstalker.R;
import friendstalker.momstalker.Utility.Event;
import friendstalker.momstalker.Utility.User;

public class DeleteEventActivity extends AppCompatActivity {
    private EditText eventName = null;
    private Button remove = null;
    private TextView errorMsg = null;
    private String err = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        eventName = (EditText) findViewById(R.id.removeEventText);
        remove = (Button) findViewById(R.id.removeEventB);
        errorMsg = (TextView) findViewById(R.id.errorMsgRemoveE);
        err = "Event not found!";

        createButtonListener(this);
    }

    private void createButtonListener(final DeleteEventActivity eventE){
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String event = eventName.getText().toString();
                AndroidUser.myEvents = Connection.getEvents(AndroidUser.user.id);
                int eventId = 0;
                for(Event e : AndroidUser.myEvents){
                    if(e.name.equals(event)){
                        eventId = e.id;
                        break;
                    }
                }
                if(eventId > 0){
                    Connection.deleteEvent(eventId);
                    Intent intent = new Intent(eventE,ViewFriendsActivity.class);
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

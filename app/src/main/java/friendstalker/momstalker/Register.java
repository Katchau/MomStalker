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

public class Register extends AppCompatActivity {
    private Button register = null;
    private EditText username = null;
    private EditText password = null;
    private EditText confirmP = null;
    private TextView errorMsg = null;
    private Resources res = null;
    private String err1 = null;
    private String err2 = null;
    private String err3 = null;
    private String err4 = null;
    private String err5 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //must have in order to make http requests
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        register = (Button) findViewById(R.id.registerButton);
        username = (EditText) findViewById(R.id.user_text);
        password = (EditText) findViewById(R.id.pass_text);
        confirmP = (EditText) findViewById(R.id.conf_text);
        errorMsg = (TextView) findViewById(R.id.errorMsg);

        initializeErrorMsgs();
        createButtonListener(this);
    }

    private void initializeErrorMsgs(){
        res = getResources();
        err1 = res.getString(R.string.error_missing_username);
        err2 = res.getString(R.string.error_missing_password);
        err3 = res.getString(R.string.error_missing_confirmation);
        err4 = res.getString(R.string.error_fail_sign);
        err5 = res.getString(R.string.error_different_fields);
    }

    private boolean setErrorMsg(String msg, String error){
        if(msg == null || msg.equals("") || msg.replace(" ", "").equals("") || msg.equals("-1")){
            errorMsg.setText(error);
            errorMsg.setVisibility(View.VISIBLE);
            return true;
        }
        else return false;
    }

    private void createButtonListener(final Register reg){
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String pass = password.getText().toString();
                String conf = confirmP.getText().toString();
                String user = username.getText().toString();
                if(!setErrorMsg(pass, err2) && !setErrorMsg(user,err1) && !setErrorMsg(conf, err3)){
                    if(!pass.equals(conf)){
                        setErrorMsg("",err5);
                    }
                    else if(Connection.createUser(user,pass)){
                        Intent intent = new Intent(reg,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        setErrorMsg("",err4);
                    }
                }
            }
        });
    }



}

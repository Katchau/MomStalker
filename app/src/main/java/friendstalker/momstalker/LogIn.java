package friendstalker.momstalker;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LogIn extends AppCompatActivity {
    private Button login = null;
    private EditText username = null;
    private EditText password = null;
    private TextView errorMsg = null;
    private Resources res = null;
    private String err1 = null;
    private String err2 = null;
    private String err3 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //must have in order to make http requests
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        login = (Button) findViewById(R.id.loginButton);
        username = (EditText) findViewById(R.id.user_text1);
        password = (EditText) findViewById(R.id.pass_text1);
        errorMsg = (TextView) findViewById(R.id.errorMsg1);

        initializeErrorMsgs();
        createButtonListener(this);
    }

    private void initializeErrorMsgs(){
        res = getResources();
        err1 = res.getString(R.string.error_missing_username);
        err2 = res.getString(R.string.error_missing_password);
        err3 = res.getString(R.string.error_incorrect_account);
    }

    private boolean setErrorMsg(String msg, String error){
        if(msg == null || msg.equals("") || msg.replace(" ", "").equals("") || msg.equals("-1")){
            errorMsg.setText(error);
            errorMsg.setVisibility(View.VISIBLE);
            return true;
        }
        else return false;
    }

    private void createButtonListener(final LogIn logIn){
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String pass = password.getText().toString();
                String user = username.getText().toString();
                if(!setErrorMsg(pass, err2) && !setErrorMsg(user,err1) ){
                    String validation = Connection.verifyLogin(user,pass);
                    if(!setErrorMsg(validation,err3)){
                        //TODO ir buscar user e inicializa-lo
                        Intent intent = new Intent(logIn, MainActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

}

package tk.onlinesilkstore.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login_Activity extends AppCompatActivity {
    private Button mToRegister_btn,mLogin_btn;
    private Toolbar mToolbar_Login;
    private EditText mEmail_Input, mPassword_input;
    private ProgressDialog mProgressLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        mAuth = FirebaseAuth.getInstance();
        mToolbar_Login=findViewById(R.id.login_appbar);
        mEmail_Input=findViewById(R.id.Email_Input);
        mPassword_input=findViewById(R.id.Password_Input);
        mProgressLogin=new ProgressDialog(this);
        setSupportActionBar(mToolbar_Login);
        getSupportActionBar().setTitle("Login Page");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mLogin_btn=(Button)findViewById(R.id.Login_Btn);
        mToRegister_btn=(Button)findViewById(R.id.Login_to_Reg_btn);
        mToRegister_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login_Activity.this,Register_Activity.class));

            }
        });

        mLogin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Email=mEmail_Input.getText().toString().trim();
                String Password=mPassword_input.getText().toString().trim();
                mProgressLogin.setTitle("Logging In User");
                mProgressLogin.setMessage("Please wait while we Login User");
                mProgressLogin.setCanceledOnTouchOutside(false);
                mProgressLogin.show();
                Login_user(Email,Password);
            }
        });
    }

    private void Login_user(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    mProgressLogin.dismiss();
                    Intent mainIntent= new Intent(Login_Activity.this,MainActivity.class);
                    //Need to check this
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);

                    finish();
                }
                else
                {
                    mProgressLogin.hide();
                    Toast.makeText(Login_Activity.this,"Error in Logging in",Toast.LENGTH_LONG).show();
                }

            }
        });


    }
}

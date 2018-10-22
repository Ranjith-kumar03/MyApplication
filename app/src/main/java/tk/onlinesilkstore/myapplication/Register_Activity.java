package tk.onlinesilkstore.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register_Activity extends AppCompatActivity {
    private EditText m_Name_Input_reg, m_Email_Input_reg, m_Password_Input_reg;
    private Button m_Register_Btn;
    private FirebaseAuth mAuth;
    private Toolbar mToolbar_RegisterActivity;
    private ProgressDialog mRegprogress;
    private DatabaseReference mdatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        mToolbar_RegisterActivity=findViewById(R.id.layout_activity_reg);
        setSupportActionBar(mToolbar_RegisterActivity);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        m_Register_Btn = findViewById(R.id.Register_Btn);
        m_Name_Input_reg = findViewById(R.id.Name_Input_reg);
        m_Email_Input_reg = findViewById(R.id.Email_Input_reg);
        m_Password_Input_reg = findViewById(R.id.Password_Input_reg);
        mRegprogress=new ProgressDialog(this);


        m_Register_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Display_Name = m_Name_Input_reg.getText().toString().trim();
                String Email = m_Email_Input_reg.getText().toString().trim();
                String Password = m_Password_Input_reg.getText().toString().trim();

                if(!TextUtils.isEmpty(Display_Name)||!TextUtils.isEmpty(Email)||!TextUtils.isEmpty(Password))
                {
                    mRegprogress.setTitle("Registering User");
                    mRegprogress.setMessage("Please wait while we create your Account");
                    mRegprogress.setCanceledOnTouchOutside(false);
                    mRegprogress.show();
                    register_user(Display_Name, Email, Password);

                }



            }
        });
    }

    private void register_user(final String Display_Name, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    FirebaseUser current_user=FirebaseAuth.getInstance().getCurrentUser();
                    String uid=current_user.getUid();
                    Toast.makeText(Register_Activity.this,"user id--"+Display_Name,Toast.LENGTH_LONG).show();
                    Toast.makeText(Register_Activity.this,"user id--"+uid,Toast.LENGTH_LONG).show();
                    mdatabase=FirebaseDatabase.getInstance().getReference().child("users").child(uid);
                    HashMap<String,String> usermap= new HashMap<>();
                    usermap.put("name",Display_Name);
                    usermap.put("status","Iam using my chat");
                    usermap.put("image","default");
                    usermap.put("thump_image","default");
                    mdatabase.setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                mRegprogress.dismiss();
                    Intent mainIntent= new Intent(Register_Activity.this,MainActivity.class);
                    //Need to check this
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();
                            }
                            else
                            {
                                mRegprogress.hide();
                                Toast.makeText(Register_Activity.this,"Some problem in the new",Toast.LENGTH_LONG).show();
                            }
                        }

                    });


                }else
                {
                    mRegprogress.hide();
                    Toast.makeText(Register_Activity.this,"Error in Adding Register",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}



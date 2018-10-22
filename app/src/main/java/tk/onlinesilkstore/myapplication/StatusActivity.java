package tk.onlinesilkstore.myapplication;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {
    private Toolbar mStatusToolbar;
    private EditText mStatusText;
    private Button mStatusBtn;
    private DatabaseReference mDatabase;
    private FirebaseUser mCurrentuser;
    private ProgressDialog mStatusProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        mStatusToolbar=findViewById(R.id.status_appbar_layout);
        setSupportActionBar(mStatusToolbar);
        getSupportActionBar().setTitle("Account Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mCurrentuser=FirebaseAuth.getInstance().getCurrentUser();
        String uid=mCurrentuser.getUid();
        mDatabase=FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        mStatusText=findViewById(R.id.status_text);
        mStatusBtn=findViewById(R.id.status_btn);
        String current_status=getIntent().getStringExtra("status");
        mStatusText.setText(current_status);


        mStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStatusProgress=new ProgressDialog(StatusActivity.this);
                mStatusProgress.setTitle("Saving Changes");
                mStatusProgress.setMessage("Please wait while we Save");
                mStatusProgress.show();
                String status=mStatusText.getText().toString().trim();
                mDatabase.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful())
                        {
                            mStatusProgress.dismiss();


                        }else
                        {
                            Toast.makeText(StatusActivity.this,"Error in updating Status",Toast.LENGTH_LONG).show();
                        }

                    }
                });



            }
        });
    }
}

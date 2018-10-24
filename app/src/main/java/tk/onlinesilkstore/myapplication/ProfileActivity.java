package tk.onlinesilkstore.myapplication;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

   private  TextView mDisname, mStatus, mTotalfriends;
   private Button mSend_Req_Btn, mCancel_Req_Btn;
   private ImageView mImageView;
   private DatabaseReference mUsersDatabase;


   //Friend Request Database
   private DatabaseReference mFriendRequestDatabase;
   private FirebaseUser mCurrent_user;

   private ProgressDialog mProgressBar;
   private String mCurrent_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final String user_id= getIntent().getStringExtra("uid");
        mUsersDatabase=FirebaseDatabase.getInstance().getReference().child("users").child(user_id);

        //Friend Request
        mFriendRequestDatabase=FirebaseDatabase.getInstance().getReference().child("friends_req");
        mCurrent_user=FirebaseAuth.getInstance().getCurrentUser();

        mImageView =findViewById(R.id.activity_profile_imageView);
        mDisname =findViewById(R.id.activity_profile_display_name);
        mStatus =findViewById(R.id.activity_profile_status);
        mTotalfriends =findViewById(R.id.activity_profile_total_friends);
        mSend_Req_Btn =findViewById(R.id.activity_profile_send_friend_req_btn);
        mCancel_Req_Btn =findViewById(R.id.activity_profile_cancel_friend_req_btn);


        mCurrent_state="not_friends";
        mProgressBar= new ProgressDialog(ProfileActivity.this);
        mProgressBar.setTitle("loading User Data");
        mProgressBar.setMessage("Please wait while we upload image");
        mProgressBar.setCanceledOnTouchOutside(false);
        mProgressBar.show();


        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String display_name=dataSnapshot.child("name").getValue().toString();
                String status=dataSnapshot.child("status").getValue().toString();
                String image =dataSnapshot.child("image").getValue().toString();
                mDisname.setText(display_name);
                mStatus.setText(status);

                Picasso.get().load(image).placeholder(R.drawable.silkyadaah).into(mImageView);
                mProgressBar.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ///Sending Friend Request and Entering protocol values

        mSend_Req_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mCurrent_state.equals("not_friends"))
                {
                    mFriendRequestDatabase.child(mCurrent_user.getUid()).child(user_id).child("request_type").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful())
                            {
                             mFriendRequestDatabase.child(user_id).child(mCurrent_user.getUid()).child("request_type").setValue("received").addOnSuccessListener(new OnSuccessListener<Void>() {
                                 @Override
                                 public void onSuccess(Void aVoid) {

                                     Toast.makeText(ProfileActivity.this,"Friend Request Protocol for Friend and Myself set ",Toast.LENGTH_LONG).show();
                                 }
                             });
                            }else
                            {
                                Toast.makeText(ProfileActivity.this,"Cant set Request Protocol",Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                }
            }
        });


    }


}

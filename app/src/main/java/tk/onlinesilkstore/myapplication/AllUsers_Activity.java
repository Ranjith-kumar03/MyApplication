package tk.onlinesilkstore.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllUsers_Activity extends AppCompatActivity {
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
     private DatabaseReference mUserdatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users_);
        mToolbar=findViewById(R.id.alluser_activity_layout);
        mRecyclerView=findViewById(R.id.all_users_list);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All Users of Chat");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mUserdatabase=FirebaseDatabase.getInstance().getReference().child("users");

        mRecyclerView=findViewById(R.id.all_users_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(AllUsers_Activity.this));
    }


    @Override
    protected void onStart() {
        super.onStart();



        FirebaseRecyclerOptions<Users> options=new FirebaseRecyclerOptions.Builder<Users>().setQuery(mUserdatabase,Users.class).build();


      FirebaseRecyclerAdapter<Users,Usersviewholder> adapter=new FirebaseRecyclerAdapter<Users, Usersviewholder>(options) {

          @Override
          protected void onBindViewHolder(@NonNull Usersviewholder holder, int position, @NonNull Users model) {

              holder.setName(model.getName());
              holder.setStatus(model.getStatus());
              holder.setThump_image(model.getThump_image());
              final String uid=getRef(position).getKey();
              holder.mView.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      Intent profileactivity_intent=new Intent(AllUsers_Activity.this,ProfileActivity.class);
                      profileactivity_intent.putExtra("uid",uid);
                      startActivity(profileactivity_intent);
                  }
              });
          }

          @NonNull
          @Override
          public Usersviewholder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

              View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.users_single_layout,parent,false);

              return new Usersviewholder(view);
          }
      };


       mRecyclerView.setAdapter(adapter);
       adapter.startListening();



    }

    public class Usersviewholder extends RecyclerView.ViewHolder{
        View mView;

        public Usersviewholder(@NonNull View itemView) {
            super(itemView);

            mView=itemView;
        }

        public void setName(String name)
        {
            TextView Tv1username=mView.findViewById(R.id.displayname_alluser);
            Tv1username.setText(name);

            Toast.makeText(AllUsers_Activity.this,"We can set name--"+name,Toast.LENGTH_LONG).show();
        }
        public void setStatus(String status)
        {
            TextView Tv2status=mView.findViewById(R.id.display_status_allusers);
            Tv2status.setText(status);
        }
        public void setThump_image(String thumbimage)
        {


            CircleImageView circleImageView=mView.findViewById(R.id.singleuser_display);
            Picasso.get().load(thumbimage).placeholder(R.drawable.silkyadaah).into(circleImageView);


        }
    }
}

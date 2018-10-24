package tk.onlinesilkstore.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SettingActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseUser mCurrentuser;
    private CircleImageView  mCircleImageView;
    private TextView mSettingName, mStatus;
    private Button mStatusBtn,mImageBtn;
    private static final int GALLERY_PICK=1;
    private StorageReference mImageStorage;
    private ProgressDialog mProgressDialog;
    //Tnump Image
    private Bitmap thump_bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mCircleImageView=findViewById(R.id.setting_image);
        mSettingName=findViewById(R.id.setting_display_name);
        mStatus=findViewById(R.id.setting_display_status);
        mStatusBtn=findViewById(R.id.setting_change_status_btn);
        mImageBtn=findViewById(R.id.setting_changeimage_btn);
        mImageStorage=FirebaseStorage.getInstance().getReference();
        mCurrentuser=FirebaseAuth.getInstance().getCurrentUser();
        String current_user_uid=mCurrentuser.getUid();
        mDatabase=FirebaseDatabase.getInstance().getReference().child("users").child(current_user_uid);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name=dataSnapshot.child("name").getValue().toString();
                String status=dataSnapshot.child("status").getValue().toString();
                String image=dataSnapshot.child("image").getValue().toString();
                String thump_image=dataSnapshot.child("thump_image").getValue().toString();

                mSettingName.setText(name);
                mStatus.setText(status);

                if(!image.equals("default")) {

                    Picasso.get().load(image).placeholder(R.drawable.silkyadaah).into(mCircleImageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status= mStatus.getText().toString().trim();
                Intent intentstatus= new Intent(SettingActivity.this,StatusActivity.class);
                intentstatus.putExtra("status",status);
                startActivity(intentstatus);
            }
        });

        mImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryintent=new Intent();
                galleryintent.setType("image/*");
                galleryintent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryintent,"Select Image"),GALLERY_PICK);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_PICK && resultCode==RESULT_OK)
        {
            Uri imageUri=data.getData();
            CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .start(SettingActivity.this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                final String current_user_uid=mCurrentuser.getUid();
                Uri resultUri = result.getUri();

                //Thumb image
                File thump_filepath=new File(resultUri.getPath());
                try {
                    thump_bitmap = new Compressor(this)
                            .setQuality(75)
                            .setMaxHeight(200)
                            .setMaxWidth(200)
                            .compressToBitmap(thump_filepath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thump_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] thump_byte = baos.toByteArray();


                mProgressDialog=new ProgressDialog(SettingActivity.this);
                mProgressDialog.setTitle("Uploading Image....");
                mProgressDialog.setMessage("Please wait while we upload image");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();
                StorageReference filepath=mImageStorage.child("profile_images").child(current_user_uid+".jpg");
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            mImageStorage.child("profile_images").child(current_user_uid+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String download_url=uri.toString();
                                    mDatabase.child("image").setValue(download_url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                mProgressDialog.dismiss();
                                                Toast.makeText(SettingActivity.this, "Sucess uploading Image Link", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(SettingActivity.this, "Image upload not Working", Toast.LENGTH_SHORT).show();
                            mProgressDialog.dismiss();
                        }
                    }
                });

                StorageReference thump_storage_filepath=mImageStorage.child("profile_images").child("thumbs").child(current_user_uid+".jpg");

                UploadTask uploadTask = thump_storage_filepath.putBytes(thump_byte);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if(task.isSuccessful())
                        {
                            mImageStorage.child("profile_images").child("thumbs").child(current_user_uid+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    String Thumb_uri=uri.toString();
                                    mDatabase.child("thump_image").setValue(Thumb_uri).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful())
                                            {
                                                mProgressDialog.dismiss();
                                                Toast.makeText(SettingActivity.this, "Sucess uploading Thumb uri Link", Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                            {
                                                Toast.makeText(SettingActivity.this, "Failed uploading Thumb uri Link", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }
                            });

                        }else
                        {
                            Toast.makeText(SettingActivity.this, "Thump Image upload not Working", Toast.LENGTH_SHORT).show();
                            mProgressDialog.dismiss();
                        }

                    }
                });



            }


            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
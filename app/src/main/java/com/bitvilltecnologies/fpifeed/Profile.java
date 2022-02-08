package com.bitvilltecnologies.fpifeed;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class Profile extends AppCompatActivity {

    private  static final String TAG="VIEWDATABASE";
    private static final int CHOOSE_IMAGE = 101;

    String profileImageurl;
    Uri uriProfileImage;


    private FirebaseDatabase mfirebasedatabase;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    private ListView listView;
    private TextView textView;
    private ImageView imageViewz;
    private ProgressBar progressBarz;
    private  String userID;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.notification:
                    Intent intent =new Intent(Profile.this,NewsFeed.class);
                    finish();
                    startActivity(intent);
                    return true;

                case R.id.schoolportal:
                    Intent intent2 =new Intent(Profile.this,Portal.class);
                    finish();
                    startActivity(intent2);
                    return true;


                case R.id.profile:

                    return true;

            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        BottomNavigationView navigation = (BottomNavigationView)findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.profile);

        listView=findViewById(R.id.listview);
        textView=findViewById(R.id.EV);
        imageViewz=findViewById(R.id.imageView1);
        progressBarz=findViewById(R.id.progress);
        mAuth=FirebaseAuth.getInstance();
        mfirebasedatabase= FirebaseDatabase.getInstance();
        myRef=mfirebasedatabase.getReference("Users");
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        userID =firebaseUser.getUid();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();








        imageViewz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showaImageChooser();

            }
        });



        USERVERIFICATION();
        LoadUserDP();



        myRef.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user =dataSnapshot.getValue(User.class);

                if (user !=null) {



                        String user_name = user.name;
                        String user_email = user.phone;
                        String user_phone = user.regno;
                        String user_address = user.level;

                        ArrayList<String> array = new ArrayList<>();
                        array.add(user_name);
                        array.add(user_email);
                        array.add(user_phone);
                        array.add(user_address);


                        ArrayAdapter adapter = new ArrayAdapter(Profile.this, R.layout.custom, array);
                        listView.setAdapter(adapter);


                    }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Profile.this,databaseError.toString(),Toast.LENGTH_LONG).show();

            }


        });


    }



    private void LoadUserDP() {
        final FirebaseUser firebaseUser = mAuth.getCurrentUser();


        //if (firebaseUser.getPhotoUrl() != null) {

        //Glide.with(Home.this.getApplicationContext()).load(firebaseUser.getPhotoUrl().toString()).into(imageViewz);

        //}else {
        // Glide.with(Home.this.getApplicationContext()).load(R.drawable.dp2).into(imageViewz);
        // }


        if (firebaseUser.getPhotoUrl()!=null){
            Picasso.get().load(firebaseUser.getPhotoUrl().toString()).into(imageViewz);
        }else {

            Toast.makeText(Profile.this,"cant display dp",Toast.LENGTH_LONG).show();



        }
    }

    private void saveUserInformation() {



        final FirebaseUser firebaseUser = mAuth.getCurrentUser();


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Saveing.....");
        progressDialog.show();

        if (firebaseUser != null && profileImageurl != null ) {





            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(Uri.parse(profileImageurl))
                    .build();

            firebaseUser.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            progressDialog.dismiss();

                            if (task.isSuccessful()) {

                                Toast.makeText(Profile.this, "PROFILE UPDATED", Toast.LENGTH_LONG).show();
                            }


                        }

                    });

        }else {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK   && data!=null&&data.getData()!=null){

            uriProfileImage= data.getData();




            try {
                Bitmap bitmap =  MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage );
                imageViewz.setImageBitmap(bitmap);

                uploadImagetofirebasestorage();


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }



    private void uploadImagetofirebasestorage() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading.....");
        progressDialog.show();

        final StorageReference storageReference =FirebaseStorage.getInstance().getReference("profilepics/"+System.currentTimeMillis()+".jpg");

        if (uriProfileImage != null){

            storageReference.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    //profileImageurl=taskSnapshot.getStorage().getDownloadUrl().toString();



                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.e("t","url"+uri.toString());
                            profileImageurl=uri.toString();
                            Toast.makeText(Profile.this,"got url"+uri,Toast.LENGTH_SHORT).show();
                            saveUserInformation();
                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Profile.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });


        }



    }



    private void showaImageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"SELECT DP"),CHOOSE_IMAGE);

    }

    private void USERVERIFICATION() {
        final FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser.isEmailVerified()){
            textView.setText("EMAIL AND REGNO NOT LINKED ");

        }else {
            textView.setText(" YOUR EMAIL AND REGNO HAS BEEN LINKED ");
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(Profile.this,"DATA SENT FOR VERIFICATION",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }


    }



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater MENU = getMenuInflater();
//        MENU.inflate(R.menu.edit,menu);
//        return true;
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()){
//            case R.id.updateprofile:
//                startActivity(new Intent(Home.this,Profileupd.class));
//
//        }
//        return true;
//    }

}



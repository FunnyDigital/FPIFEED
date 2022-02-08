package com.bitvilltecnologies.fpifeed;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ViewActivity extends AppCompatActivity {
TextView titleView,descriptionView, full_description, date;
ImageView imageViews;
FirebaseDatabase firebaseDatabase;
DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        titleView=(TextView)findViewById(R.id.titlezView);
        descriptionView=(TextView)findViewById(R.id.rdiscriptionvView);
        imageViews =(ImageView)findViewById(R.id.rimageviewView);
        full_description=(TextView)findViewById(R.id.full_description);
        date=(TextView)findViewById(R.id.date);


        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("POST");

String post = getIntent().getStringExtra("post");

reference.child(post).addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()){

            String title =dataSnapshot.child("title").getValue().toString();
            String description =dataSnapshot.child("description").getValue().toString();
            String imageurl =dataSnapshot.child("image").getValue().toString();
            String full_descriptions= dataSnapshot.child("full_description").getValue().toString();
            String dates=dataSnapshot.child("date").getValue().toString();

            titleView.setText(title);
            descriptionView.setText(description);
            Picasso.get().load(imageurl).into(imageViews);
            full_description.setText(full_descriptions);
            date.setText(dates);



        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});
    }
}

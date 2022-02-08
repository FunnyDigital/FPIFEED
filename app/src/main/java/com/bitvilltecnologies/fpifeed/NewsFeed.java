package com.bitvilltecnologies.fpifeed;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class NewsFeed extends AppCompatActivity {

    ImageView imageViewno;
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    LinearLayoutManager mLayoutManager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.notification:

                    return true;

                case R.id.schoolportal:
                    Intent intent = new Intent(NewsFeed.this, Portal.class);
                    finish();
                    startActivity(intent);
                    return true;


                case R.id.profile:
                    Intent intent2 = new Intent(NewsFeed.this, Profile.class);
                    finish();
                    startActivity(intent2);
                    return true;

            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.notification);


        mLayoutManager = new LinearLayoutManager(NewsFeed.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);


        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(mLayoutManager);
      //  recyclerView.notify();


        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("POST");


        //imageViewno=findViewById(R.id.imageViewno);
        if (!TextUtils.isEmpty(Config.imageLink))
            Picasso.get().load(Config.imageLink).into(imageViewno);

    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Model, ViewHolder> firebaseRecyclerOptions =
                new FirebaseRecyclerAdapter<Model, ViewHolder>(
                        Model.class,
                        R.layout.customrow,
                        ViewHolder.class,
                        reference
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, Model model, final int position) {

                        viewHolder.setDetails(getApplicationContext(), model.getTitle(), model.getDescription(), model.getImage(), model.getFull_description(),
                       model.getDate() );

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(NewsFeed.this, ViewActivity.class);
                                intent.putExtra("post",getRef(position).getKey());
                                startActivity(intent);
                            }
                        });

                    }
                };

        recyclerView.setAdapter(firebaseRecyclerOptions);
    }

}








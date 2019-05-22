package com.example.macbook.awsproj;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

//import com.amazonaws.amplify.generated.graphql.ListPetsQuery;
//import com.amazonaws.amplify.generated.graphql.OnCreatePetSubscription;
import com.amazonaws.mobileconnectors.appsync.AppSyncSubscriptionCall;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.util.ArrayList;

import javax.annotation.Nonnull;

public class MainActivity extends AppCompatActivity {

//    RecyclerView mRecyclerView;
//    MyAdapter mAdapter;
//    Button btnPubSub;
//
//    private ArrayList<ListPetsQuery.Item> mPets;
//    private final String TAG = MainActivity.class.getSimpleName();
//
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        mRecyclerView = findViewById(R.id.recycler_view);
//        btnPubSub  = (Button) findViewById(R.id.pubsubbtn);
//        btnPubSub.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent pubsIntent= new Intent(MainActivity.this, PubSubActivity.class);
//                startActivity(pubsIntent);
//
//            }
//        });
//
//
//        // use a linear layout manager
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        // specify an adapter (see also next example)
//        mAdapter = new MyAdapter(this);
//        mRecyclerView.setAdapter(mAdapter);
//
//        ClientFactory.init(this);
//        FloatingActionButton btnAddPet = findViewById(R.id.btn_addPet);
//        btnAddPet.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                Intent addPetIntent = new Intent(MainActivity.this, AddPetActivity.class);
//                MainActivity.this.startActivity(addPetIntent);
//            }
//        });
//
//
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//
//        // Query list data when we return to the screen
//        query();
//        subscribe();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        subscriptionWatcher.cancel();
//    }
//
//    public void query(){
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            // Permission is not granted
//            Log.d(TAG, "WRITE_EXTERNAL_STORAGE permission not granted! Requesting...");
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    2);
//        }
//
//        ClientFactory.appSyncClient().query(ListPetsQuery.builder().build())
//                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
//                .enqueue(queryCallback);
//    }
//
//    private GraphQLCall.Callback<ListPetsQuery.Data> queryCallback = new GraphQLCall.Callback<ListPetsQuery.Data>() {
//        @Override
//        public void onResponse(@Nonnull Response<ListPetsQuery.Data> response) {
//
//            mPets = new ArrayList<>(response.data().listPets().items());
//
//            Log.i(TAG, "Retrieved list items: " + mPets.toString());
//
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    mAdapter.setItems(mPets);
//                    mAdapter.notifyDataSetChanged();
//                }
//            });
//        }
//
//        @Override
//        public void onFailure(@Nonnull ApolloException e) {
//            Log.e(TAG, e.toString());
//        }
//    };
//
//    private AppSyncSubscriptionCall subscriptionWatcher;
//
//    private void subscribe(){
//        OnCreatePetSubscription subscription = OnCreatePetSubscription.builder().build();
//        subscriptionWatcher = ClientFactory.appSyncClient().subscribe(subscription);
//        subscriptionWatcher.execute(subCallback);
//    }
//
//    private AppSyncSubscriptionCall.Callback subCallback = new AppSyncSubscriptionCall.Callback() {
//        @Override
//        public void onResponse(@Nonnull Response response) {
//            Log.i("Response", "Received subscription notification: " + response.data().toString());
//
//            // Update UI with the newly added item
//            OnCreatePetSubscription.OnCreatePet data = ((OnCreatePetSubscription.Data)response.data()).onCreatePet();
//
//            final ListPetsQuery.Item addedItem =
//                    new ListPetsQuery.Item(data.__typename(), data.id(), data.name(), data.description(), data.photo());
//
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    mPets.add(addedItem);
//                    mAdapter.notifyItemInserted(mPets.size() - 1);
//                }
//            });
//        }
//
//        @Override
//        public void onFailure(@Nonnull ApolloException e) {
//
//        }
//
//        @Override
//        public void onCompleted() {
//
//        }
//        //...other event handlers ...
//    };
}
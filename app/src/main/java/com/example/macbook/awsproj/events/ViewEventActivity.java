package com.example.macbook.awsproj.events;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.amazonaws.amplify.generated.graphql.CommentOnEventMutation;
import com.amazonaws.amplify.generated.graphql.GetEventQuery;
import com.amazonaws.amplify.generated.graphql.ListEventsQuery;
import com.amazonaws.amplify.generated.graphql.SubscribeToEventCommentsSubscription;
import com.amazonaws.mobileconnectors.appsync.AppSyncSubscriptionCall;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.macbook.awsproj.R;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;

public class ViewEventActivity extends AppCompatActivity {
    public static final String TAG = ViewEventActivity.class.getSimpleName();

    private static ListEventsQuery.Item  event;
    private TextView name, time, where, description, comments;
    private EditText newComment;
    private AppSyncSubscriptionCall<SubscribeToEventCommentsSubscription.Data> subscriptionWatcher;

    public static void startActivity(final Context context, ListEventsQuery.Item e) {
        event = e;
        Intent intent = new Intent(context, ViewEventActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        name = (TextView) findViewById(R.id.viewName);
        time = (TextView) findViewById(R.id.viewTime);
        where = (TextView) findViewById(R.id.viewWhere);
        description = (TextView) findViewById(R.id.viewDescription);
        comments = (TextView) findViewById(R.id.comments);
        newComment = (EditText) findViewById(R.id.new_comment);

        name.setText(event.name());
        time.setText(event.when());
        where.setText(event.where());
        description.setText(event.description());

        refreshEvent(true);
        startSubscription();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (subscriptionWatcher != null) {
            subscriptionWatcher.cancel();
        }
    }

    private void startSubscription() {
        SubscribeToEventCommentsSubscription subscription = SubscribeToEventCommentsSubscription.builder().eventId(event.id()).build();

        subscriptionWatcher = ClientFactory.getInstance(this.getApplicationContext()).subscribe(subscription);
        subscriptionWatcher.execute(subscriptionCallback);
    }

    private AppSyncSubscriptionCall.Callback<SubscribeToEventCommentsSubscription.Data> subscriptionCallback = new AppSyncSubscriptionCall.Callback<SubscribeToEventCommentsSubscription.Data>() {
        @Override
        public void onResponse(final @Nonnull Response<SubscribeToEventCommentsSubscription.Data> response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ViewEventActivity.this, response.data().subscribeToEventComments().eventId().substring(0, 5) + response.data().subscribeToEventComments().content(), Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Subscription response: " + response.data().toString());
                    SubscribeToEventCommentsSubscription.SubscribeToEventComments comment = response.data().subscribeToEventComments();

                    // UI only write
                    addComment(comment.content());

                    // Cache write
                    addCommentToCache(comment);

                    // Show changes from in cache
                    refreshEvent(true);
                }
            });
        }

        @Override
        public void onFailure(final @Nonnull ApolloException e) {
            Log.e(TAG, "Subscription failure", e);
        }

        @Override
        public void onCompleted() {
            Log.d(TAG, "Subscription completed");
        }
    };

    /**
     * Adds the new comment to the event in the cache.
     * @param comment
     */
    private void addCommentToCache(SubscribeToEventCommentsSubscription.SubscribeToEventComments comment) {
        try {
            // Read the old event data
            GetEventQuery getEventQuery = GetEventQuery.builder().id(event.id()).build();
            GetEventQuery.Data readData = ClientFactory.getInstance(ViewEventActivity.this).getStore().read(getEventQuery).execute();
//            Event event = readData.getEvent().fragments().event();

            ListEventsQuery.Item newItem = new ListEventsQuery.Item(readData.getEvent().__typename(), readData.getEvent().id(), readData.getEvent().name(), readData.getEvent().where(), readData.getEvent().when(), readData.getEvent().description(), (ListEventsQuery.Comments) readData.getEvent().comments().items());

            // Create the new comment object
            GetEventQuery.Item newComment = new GetEventQuery.Item(
                    comment.__typename(),
                    comment.eventId(),
                    comment.commentId(),
                    comment.content(),
                    comment.createdAt());

            // Create the new comment list attached to the event
            List<GetEventQuery.Item> items = new LinkedList<GetEventQuery.Item>();
            items.add(0, newComment);

            // Create the new event data
            GetEventQuery.Data madeData = new GetEventQuery.Data(new GetEventQuery.GetEvent(readData.getEvent().__typename(),
                    event.id(),
                    event.description(),
                    event.name(),
                    event.when(),
                    event.where(),
                    new GetEventQuery.Comments(readData.getEvent().comments().__typename(), items, readData.getEvent().comments().nextToken())));

            // Write the new event data
            ClientFactory.getInstance(ViewEventActivity.this).getStore().write(getEventQuery, madeData).execute();
            Log.d(TAG, "Wrote comment to database");
        } catch (ApolloException e) {
            Log.e(TAG, "Failed to update local database", e);
        }
    }

    /**
     * UI triggered method to add a comment. This will read the text box and submit a new comment.
     * @param view
     */
    public void addComment(View view) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(newComment.getWindowToken(), 0);

        Toast.makeText(this, "Submitting comment", Toast.LENGTH_SHORT).show();

        CommentOnEventMutation comment = CommentOnEventMutation.builder().content(newComment.getText().toString())
                .createdAt(new Date().toString())
                .eventId(event.id())
                .build();

        ClientFactory.getInstance(view.getContext())
                .mutate(comment)
                .enqueue(addCommentCallback);
    }

    /**
     * Service response subscriptionCallback confirming receipt of new comment triggered by UI.
     */
    private GraphQLCall.Callback<CommentOnEventMutation.Data> addCommentCallback = new GraphQLCall.Callback<CommentOnEventMutation.Data>() {
        @Override
        public void onResponse(@Nonnull Response<CommentOnEventMutation.Data> response) {
            Log.d(TAG, response.toString());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    clearComment();
                }
            });
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e(TAG, "Failed to make comments mutation", e);
            Log.e(TAG, e.getMessage());
        }
    };

    /**
     * Refresh the event object to latest from service.
     */
    private void refreshEvent(final boolean cacheOnly) {
        GetEventQuery getEventQuery = GetEventQuery.builder().id(event.id()).build();

        ClientFactory.getInstance(getApplicationContext())
                .query(getEventQuery)
                .responseFetcher(cacheOnly ? AppSyncResponseFetchers.CACHE_ONLY : AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(refreshEventCallback);
    }

    private GraphQLCall.Callback<GetEventQuery.Data> refreshEventCallback = new GraphQLCall.Callback<GetEventQuery.Data>() {
        @Override
        public void onResponse(@Nonnull final Response<GetEventQuery.Data> response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (response.errors().size() < 1) {

                        GetEventQuery.GetEvent geEvent = response.data().getEvent();
//                        List<ListEventsQuery.Item1> events =  (ListEventsQuery.Item1)(geEvent.comments().items());
//
//                        ListEventsQuery.Item dataItem= new ListEventsQuery.Item(geEvent.__typename(),geEvent.comments().items(), geEvent.comments().nextToken());
//                        event = dataItem;
                        refreshComments(geEvent);
                    } else {
                        Log.e(TAG, "Failed to get event.");
                    }
                }
            });
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e(TAG, "Failed to get event.");
        }
    };

    /**
     * Triggered by subscriptions/programmatically
     * @param comment
     */
    private void addComment(final String comment) {
        comments.setText(comment + "\n-----------\n");
    }

    /**
     * Reads the comments from the event object and preps it for display.
     */
    private void refreshComments(GetEventQuery.GetEvent event1) {
        StringBuilder stringBuilder = new StringBuilder();
        for (GetEventQuery.Item i : event1.comments().items()) {
            stringBuilder.append(i.content() + "\n---------\n");
        }
        Log.i("Comments", stringBuilder.toString());
        comments.setText(stringBuilder.toString());
    }

    private void clearComment() {
        newComment.setText("");
    }

}

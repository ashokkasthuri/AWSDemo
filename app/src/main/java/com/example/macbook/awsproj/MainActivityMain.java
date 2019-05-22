package com.example.macbook.awsproj;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//import com.amazonaws.amplify.generated.graphql.CreateTodoMutation;
//import com.amazonaws.amplify.generated.graphql.ListTodosQuery;
//import com.amazonaws.amplify.generated.graphql.OnCreateTodoSubscription;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.SignInUIOptions;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.client.UserStateListener;
import com.amazonaws.mobile.client.results.SignInResult;
import com.amazonaws.mobile.client.results.SignUpResult;
import com.amazonaws.mobile.client.results.UserCodeDeliveryDetails;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.amazonaws.mobileconnectors.appsync.AppSyncSubscriptionCall;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferService;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

//import type.CreateTodoInput;

public class MainActivityMain extends AppCompatActivity {

//    private AWSAppSyncClient mAWSAppSyncClient;
//    TextView textView;
//    private AppSyncSubscriptionCall subscriptionWatcher;
//    TextView textView1;
//    String TAG= "tag";
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main_main);
//        textView = (TextView) findViewById(R.id.text);
//        textView1= (TextView) findViewById(R.id.test1);
//        Button fbButton = (Button) findViewById(R.id.fbbutton);
//        Button googleButton = (Button) findViewById(R.id.googlebutton);
//
//        Button storageButton = (Button) findViewById(R.id.storagebutton);
//        Button downloadButton = (Button) findViewById(R.id.downloadebutton);
//        fbButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent fblogin = new Intent(MainActivityMain.this, AuthenticatorActivity.class);
//                startActivity(fblogin);
//
//            }
//        });
//
//        storageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent storageIntent = new Intent(MainActivityMain.this, StorageActivity.class);
//                startActivity(storageIntent);
//            }
//        });
//
//        downloadButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent downloadIntent = new Intent(MainActivityMain.this, DownloadActivity.class);
//                downloadIntent.putExtra("Downlaod", "Download");
//                startActivity(downloadIntent);
//            }
//        });
//
//        //Transfer Service
//        getApplicationContext().startService(new Intent(getApplicationContext(), TransferService.class));
//
//        final String signInChallengeResponse ="signInChallengeResponse";
//
//        final String username = "ashokraj.kasthuri@gmail.com";
//        final String password = "0011815@#Aw";
//
//
////        final String username = "ashokraj.kasthuri@gmail.com";
//        final String code = "0011815@#Aw";
//        final Map<String, String> attributes = new HashMap<>();
//        attributes.put("email", "name@email.com");
//
//        mAWSAppSyncClient = AWSAppSyncClient.builder()
//                .context(getApplicationContext())
//                .awsConfiguration(new AWSConfiguration(getApplicationContext()))
//                .build();
//
//        runMutation();
//        runQuery();
//        subscribe();
//
//
//        AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {
//            @Override
//            public void onResult(UserStateDetails userStateDetails) {
//                switch (userStateDetails.getUserState()){
//                    case SIGNED_IN:
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                textView1.setText("Logged IN");
//                            }
//                        });
//                        break;
//                    case SIGNED_OUT:
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                textView1.setText("Logged OUT");
//                            }
//                        });
//                        break;
//                    default:
//                        AWSMobileClient.getInstance().signOut();
//                        break;
//                }
//            }
//
//            @Override
//            public void onError(Exception e) {
//                Log.e("INIT", e.toString());
//            }
//        });
//
//
//        AWSMobileClient.getInstance().addUserStateListener(new UserStateListener() {
//            @Override
//            public void onUserStateChanged(UserStateDetails userStateDetails) {
//                switch (userStateDetails.getUserState()){
//                    case GUEST:
//                        Log.i("userState", "user is in guest mode");
//                        break;
//                    case SIGNED_OUT:
//                        Log.i("userState", "user is signed out");
//                        break;
//                    case SIGNED_IN:
//                        Log.i("userState", "user is signed in");
//                        break;
//                    case SIGNED_OUT_USER_POOLS_TOKENS_INVALID:
//                        Log.i("userState", "need to login again.");
////                    AWSMobileClient.getInstance().signIn(username, password, null, new Callback<SignInResult>() {
////                        @Override
////                        public void onResult(SignInResult result) {
////
////                        }
////
////                        @Override
////                        public void onError(Exception e) {
////
////                        }
////                        //...
////                    });
//                        break;
//                    case SIGNED_OUT_FEDERATED_TOKENS_INVALID:
//                        Log.i("userState", "user logged in via federation, but currently needs new tokens");
//                        break;
//                    default:
//                        Log.i("userState", "unsupported");
//                }
//            }
//        });
//
//        AWSMobileClient.getInstance().showSignIn(
//                this,
//                SignInUIOptions.builder()
//                        .nextActivity(NextActivity.class)
//                        .logo(R.id.useLogo)
//                        .backgroundColor(R.color.colorPrimaryDark)
//                        .canCancel(false)
//                        .build(),
//                new Callback<UserStateDetails>() {
//                    @Override
//                    public void onResult(UserStateDetails result) {
//                        Log.d(TAG, "onResult: " + result.getUserState());
//                        switch (result.getUserState()){
//                            case SIGNED_IN:
//                                Log.i("INIT", "logged in!");
//                                break;
//                            case SIGNED_OUT:
//                                Log.i(TAG, "onResult: User did not choose to sign-in");
//                                break;
//                            default:
//                                AWSMobileClient.getInstance().signOut();
//                                break;
//                        }
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//                        Log.e(TAG, "onError: ", e);
//                    }
//                }
//        );
//
//        AWSMobileClient.getInstance().signUp(username, password, attributes, null, new Callback<SignUpResult>() {
//            @Override
//            public void onResult(final SignUpResult signUpResult) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.d(TAG, "Sign-up callback state: " + signUpResult.getConfirmationState());
//                        if (!signUpResult.getConfirmationState()) {
//                            final UserCodeDeliveryDetails details = signUpResult.getUserCodeDeliveryDetails();
//                            makeToast("Confirm sign-up with: " + details.getDestination());
//                        } else {
//                            makeToast("Sign-up done.");
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void onError(Exception e) {
//                Log.e(TAG, "Sign-up error", e);
//            }
//        });
//
//
//        AWSMobileClient.getInstance().confirmSignUp(username, code, new Callback<SignUpResult>() {
//            @Override
//            public void onResult(final SignUpResult signUpResult) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.d(TAG, "Sign-up callback state: " + signUpResult.getConfirmationState());
//                        if (!signUpResult.getConfirmationState()) {
//                            final UserCodeDeliveryDetails details = signUpResult.getUserCodeDeliveryDetails();
//                            makeToast("Confirm sign-up with: " + details.getDestination());
//                        } else {
//                            makeToast("Sign-up done.");
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void onError(Exception e) {
//                Log.e(TAG, "Confirm sign-up error", e);
//            }
//        });
//
//        AWSMobileClient.getInstance().resendSignUp("ashokraj.kasthuri@gmail.com", new Callback<SignUpResult>() {
//            @Override
//            public void onResult(SignUpResult signUpResult) {
//                Log.i(TAG, "A verification code has been sent via" +
//                        signUpResult.getUserCodeDeliveryDetails().getDeliveryMedium()
//                        + " at " +
//                        signUpResult.getUserCodeDeliveryDetails().getDestination());
//            }
//
//            @Override
//            public void onError(Exception e) {
//                Log.e(TAG, String.valueOf(e));
//            }
//        });
//
//
//        AWSMobileClient.getInstance().signIn(username, password, null, new Callback<SignInResult>() {
//            @Override
//            public void onResult(final SignInResult signInResult) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.d(TAG, "Sign-in callback state: " + signInResult.getSignInState());
//                        switch (signInResult.getSignInState()) {
//                            case DONE:
//                                makeToast("Sign-in done.");
//                                break;
//                            case SMS_MFA:
//                                makeToast("Please confirm sign-in with SMS.");
//                                break;
//                            case NEW_PASSWORD_REQUIRED:
//                                makeToast("Please confirm sign-in with new password.");
//                                break;
//                            default:
//                                makeToast("Unsupported sign-in confirmation: " + signInResult.getSignInState());
//                                break;
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void onError(Exception e) {
//                Log.e(TAG, "Sign-in error", e);
//            }
//        });
//
//        AWSMobileClient.getInstance().confirmSignIn(signInChallengeResponse, new Callback<SignInResult>() {
//            @Override
//            public void onResult(SignInResult signInResult) {
//                Log.d(TAG, "Sign-in callback state: " + signInResult.getSignInState());
//                switch (signInResult.getSignInState()) {
//                    case DONE:
//                        makeToast("Sign-in done.");
//                        break;
//                    case SMS_MFA:
//                        makeToast("Please confirm sign-in with SMS.");
//                        break;
//                    case NEW_PASSWORD_REQUIRED:
//                        makeToast("Please confirm sign-in with new password.");
//                        break;
//                    default:
//                        makeToast("Unsupported sign-in confirmation: " + signInResult.getSignInState());
//                        break;
//                }
//            }
//
//            @Override
//            public void onError(Exception e) {
//                Log.e(TAG, "Sign-in error", e);
//            }
//        });
//
////        Log.d("details", AWSMobileClient.getInstance().getUsername());   //String
////        Log.d("details", String.valueOf(AWSMobileClient.getInstance().isSignedIn()));  //Boolean
////        Log.d("details", AWSMobileClient.getInstance().getIdentityId());   //String
////
////
////        try {
////            Log.d("details", String.valueOf(AWSMobileClient.getInstance().getTokens()));
////            Log.d("details", AWSMobileClient.getInstance().getTokens().getIdToken().getTokenString());
////
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////
////        Log.d("details", String.valueOf(AWSMobileClient.getInstance().getCredentials()));
//
//
//
//
//        //final oncreate
//    }
//    public void makeToast(String str){
//        Toast.makeText(this, str, Toast.LENGTH_SHORT);
//    }
//
//    public void runMutation(){
//        CreateTodoInput createTodoInput = CreateTodoInput.builder().
//                name("Use AppSync").
//                description("Realtime and Offline").
//                build();
//
//        mAWSAppSyncClient.mutate(CreateTodoMutation.builder().input(createTodoInput).build())
//                .enqueue(mutationCallback);
//    }
//
//    private GraphQLCall.Callback<CreateTodoMutation.Data> mutationCallback = new GraphQLCall.Callback<CreateTodoMutation.Data>() {
//        @Override
//        public void onResponse(@Nonnull Response<CreateTodoMutation.Data> response) {
//            Log.i("Results", "Added Todo");
//        }
//
//        @Override
//        public void onFailure(@Nonnull ApolloException e) {
//            Log.e("Error", e.toString());
//        }
//    };
//
//    public void runQuery(){
//        mAWSAppSyncClient.query(ListTodosQuery.builder().build())
//                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
//                .enqueue(todosCallback);
//    }
//
//    private GraphQLCall.Callback<ListTodosQuery.Data> todosCallback = new GraphQLCall.Callback<ListTodosQuery.Data>() {
//        @Override
//        public void onResponse(@Nonnull Response<ListTodosQuery.Data> response) {
//            Log.i("Results", response.data().listTodos().items().toString());
//        }
//
//        @Override
//        public void onFailure(@Nonnull ApolloException e) {
//            Log.e("ERROR", e.toString());
//        }
//    };
//
//    private void subscribe(){
//        OnCreateTodoSubscription subscription = OnCreateTodoSubscription.builder().build();
//        subscriptionWatcher = mAWSAppSyncClient.subscribe(subscription);
//        subscriptionWatcher.execute(subCallback);
//    }
//
//    private AppSyncSubscriptionCall.Callback subCallback = new AppSyncSubscriptionCall.Callback() {
//        @Override
//        public void onResponse(@Nonnull Response response) {
//            Log.i("Response", response.data().toString());
//        }
//
//        @Override
//        public void onFailure(@Nonnull ApolloException e) {
//            Log.e("Error", e.toString());
//        }
//
//        @Override
//        public void onCompleted() {
//            Log.i("Completed", "Subscription completed");
//        }
//    };

}

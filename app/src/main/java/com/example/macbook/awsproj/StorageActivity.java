package com.example.macbook.awsproj;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtilityOptions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class StorageActivity extends Activity {

    private static final String TAG = StorageActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize the AWSMobileClient if not initialized
        super.onCreate(savedInstanceState);
        AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {
            @Override
            public void onResult(UserStateDetails userStateDetails) {
                Log.i(TAG, String.valueOf(userStateDetails.getUserState()));
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Initialization error.", e);
            }
        });
        uploadWithTransferUtility();
    }

    public void uploadWithTransferUtility() {

        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(getApplicationContext())
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(new AmazonS3Client(AWSMobileClient.getInstance()))
                        .build();

        //TransferThreadPoolSize
        //TransferServiceCheckTimeInterval
        TransferUtilityOptions options = new TransferUtilityOptions();
        options.setTransferThreadPoolSize(8);
        options.setTransferServiceCheckTimeInterval(2 * 60 * 1000); // 2-minutes


        ObjectMetadata myObjectMetadata = new ObjectMetadata();

//create a map to store user metadata
        Map<String, String> userMetadata = new HashMap<String,String>();
        userMetadata.put("myKey","myVal");

//call setUserMetadata on our ObjectMetadata object, passing it our map
        myObjectMetadata.setUserMetadata(userMetadata);

        TransferObserver uploadObserver =
                transferUtility.upload(
                        "public/s3Key.txt",
                        new File("/storage/emulated/0/Download/sample.txt"), myObjectMetadata);

        // Attach a listener to the observer to get state update and progress notifications
        uploadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    // Handle a completed upload.
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                int percentDone = (int)percentDonef;

                Log.d("StorageActivity", "ID:" + id + " bytesCurrent: " + bytesCurrent
                        + " bytesTotal: " + bytesTotal + " " + percentDone + "%");
            }

            @Override
            public void onError(int id, Exception ex) {
                // Handle errors
            }

        });

        // If you prefer to poll for the data, instead of attaching a
        // listener, check for the state and progress in the observer.
        if (TransferState.COMPLETED == uploadObserver.getState()) {
            // Handle a completed upload.
        }

        Log.d("StorageActivity", "Bytes Transferred: " + uploadObserver.getBytesTransferred());
        Log.d("StorageActivity", "Bytes Total: " + uploadObserver.getBytesTotal());
    }
}
package net.ticherhaz.karangancemerlangspm.util;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import net.ticherhaz.karangancemerlangspm.HantarKaranganActivity;
import net.ticherhaz.karangancemerlangspm.R;
import net.ticherhaz.karangancemerlangspm.model.HantarKarangan;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Service to handle uploading files to Firebase Storage.
 */
public class MyUploadService extends MyBaseTaskService {

    /**
     * Intent Actions
     **/
    public static final String ACTION_UPLOAD = "action_upload";
    public static final String UPLOAD_COMPLETED = "upload_completed";
    public static final String UPLOAD_ERROR = "upload_error";
    /**
     * Intent Extras
     **/
    public static final String EXTRA_FILE_URI = "extra_file_uri";
    public static final String EXTRA_USER_UID = "extra_user_uid";
    public static final String EXTRA_USER_NAME = "extra_user_name";
    public static final String EXTRA_USER_SEKOLAH = "extra_user_sekolah";
    private static final String TAG = "MyUploadService";
    // [START declare_ref]
    private StorageReference mStorageRef;
    // [END declare_ref]

    public static IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UPLOAD_COMPLETED);
        filter.addAction(UPLOAD_ERROR);

        return filter;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // [START get_storage_ref]
        mStorageRef = FirebaseStorage.getInstance().getReference();
        // [END get_storage_ref]
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand:" + intent + ":" + startId);
        if (ACTION_UPLOAD.equals(intent.getAction())) {
            Uri fileUri = intent.getParcelableExtra(EXTRA_FILE_URI);
            String userUid = intent.getStringExtra(EXTRA_USER_UID);
            String name = intent.getStringExtra(EXTRA_USER_NAME);
            String sekolah = intent.getStringExtra(EXTRA_USER_SEKOLAH);

            // Make sure we have permission to read the data
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getContentResolver().takePersistableUriPermission(
                        fileUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }

            uploadFromUri(fileUri, userUid, name, sekolah);
        }

        return START_REDELIVER_INTENT;
    }
    // [END upload_from_uri]

    // [START upload_from_uri]
    private void uploadFromUri(final Uri fileUri, final String userUid, final String name, final String sekolah) {
        Log.d(TAG, "uploadFromUri:src:" + fileUri.toString());
        Log.d(TAG, "userUid:src:" + userUid);

        // [START_EXCLUDE]
        taskStarted();
        showProgressNotification(getString(R.string.progress_uploading), 0, 0);
        // [END_EXCLUDE]

        //String dateFormat = "yyyy-MM-dd hh:mm:ss a";
        @SuppressLint("SimpleDateFormat") String timeUid = new SimpleDateFormat("yyyyMMddhhmmssMs").format(new Date());
        // [START get_child_ref]
        // Get a reference to store file at photos/<FILENAME>.jpg
        final StorageReference fileRef = mStorageRef.child("hantarKarangan").child(userUid)
                .child("FILE_" + timeUid);
        //   fileUri.getLastPathSegment()
        // [END get_child_ref]

        // Upload file to Firebase Storage
        Log.d(TAG, "uploadFromUri:dst:" + fileRef.getPath());
        fileRef.putFile(fileUri).
                addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        showProgressNotification(getString(R.string.progress_uploading),
                                taskSnapshot.getBytesTransferred(),
                                taskSnapshot.getTotalByteCount());
                    }
                })
                .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        // Forward any exceptions
                        if (!task.isSuccessful()) {
                            if (task.getException() != null)
                                throw task.getException();
                        }

                        Log.d(TAG, "uploadFromUri: upload success");

                        // Request the public download URL
                        return fileRef.getDownloadUrl();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(@NonNull Uri downloadUri) {
                        // Upload succeeded
                        Log.d(TAG, "uploadFromUri: getDownloadUri success");


                        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        //Make the key
                        final String hantarKaranganUid = databaseReference.push().getKey();
                        //After that we need to update this karangan about the lastuservisited
                        final String tarikh = Calendar.getInstance().getTime().toString();
                        HantarKarangan hantarKarangan = new HantarKarangan(hantarKaranganUid, userUid, name, sekolah, tarikh, String.valueOf(downloadUri));
                        if (hantarKaranganUid != null) {
                            databaseReference.child("hantarKarangan").child(hantarKaranganUid).setValue(hantarKarangan).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Success Uploaded!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (task.getException() != null)
                                            Toast.makeText(getApplicationContext(), "Err:" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                        // [START_EXCLUDE]
                        broadcastUploadFinished(downloadUri, fileUri, userUid, name, sekolah);
                        showUploadFinishedNotification(downloadUri, fileUri, userUid, name, sekolah);
                        taskCompleted();
                        // [END_EXCLUDE]

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Upload failed
                        Log.w(TAG, "uploadFromUri:onFailure", exception);

                        // [START_EXCLUDE]
                        broadcastUploadFinished(null, fileUri, userUid, name, sekolah);
                        showUploadFinishedNotification(null, fileUri, userUid, name, sekolah);
                        taskCompleted();
                        // [END_EXCLUDE]
                    }
                });
    }

    /**
     * Broadcast finished upload (success or failure).
     *
     * @return true if a running receiver received the broadcast.
     */
    private boolean broadcastUploadFinished(@Nullable Uri downloadUrl, @Nullable Uri fileUri, String userUid, String name, String sekolah) {
        boolean success = downloadUrl != null;

        String action = success ? UPLOAD_COMPLETED : UPLOAD_ERROR;

        Intent broadcast = new Intent(action)
                // .putExtra(EXTRA_DOWNLOAD_URL, downloadUrl)
                .putExtra(EXTRA_FILE_URI, fileUri)
                .putExtra(EXTRA_USER_NAME, name)
                .putExtra(EXTRA_USER_SEKOLAH, sekolah)
                .putExtra(EXTRA_USER_UID, userUid);
        return LocalBroadcastManager.getInstance(getApplicationContext())
                .sendBroadcast(broadcast);
    }

    /**
     * Show a notification for a finished upload.
     */
    private void showUploadFinishedNotification(@Nullable Uri downloadUrl, @Nullable Uri fileUri, String userUid, String name, String sekolah) {
        // Hide the progress notification
        dismissProgressNotification();

        // Make Intent to MainActivity
        Intent intent = new Intent(this, HantarKaranganActivity.class)
                //  .putExtra(EXTRA_DOWNLOAD_URL, downloadUrl)
                .putExtra(EXTRA_FILE_URI, fileUri)
                .putExtra(EXTRA_USER_SEKOLAH, sekolah)
                .putExtra(EXTRA_USER_NAME, name)
                .putExtra(EXTRA_USER_UID, userUid)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        boolean success = downloadUrl != null;
        String caption = success ? getString(R.string.upload_success) : getString(R.string.upload_failure);
        showFinishedNotification(caption, intent, success);
    }

}

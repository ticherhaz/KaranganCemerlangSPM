package net.ticherhaz.karangancemerlangspm;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import net.ticherhaz.karangancemerlangspm.utils.MyUploadService;

public class HantarKaranganActivity extends AppCompatActivity {

    public static final String KEY_USER_UID = "key_user_uid";
    // private static final int PERMISSION_CAMERA = 45;
    private static final String KEY_FILE_URI = "key_file_uri";
    private static final String KEY_FILE_NAME = "key_file_name";
    private static final String KEY_FILE_SEKOLAH = "key_file_sekolah";
    private static final String TAG = "Storage#MainActivity";
    private static final int RC_TAKE_PICTURE = 101;

    private Button buttonMuatNaik;
    private EditText editTextName;
    private EditText editTextSekolah;
    private BroadcastReceiver mBroadcastReceiver;
    private ProgressDialog mProgressDialog;

    private Uri mFileUri = null;

    private String userUid = null;
    private String name = null;
    private String sekolah = null;

    private void listID() {
        buttonMuatNaik = findViewById(R.id.button_muat_naik);
        editTextName = findViewById(R.id.edit_text_nama);
        editTextSekolah = findViewById(R.id.edit_text_sekolah);
        retrieveIntent();
    }

    //Retrieve value
    private void retrieveIntent() {
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            userUid = intent.getExtras().getString("userUid");
        }
    }

    //Method check the empty
    private void checkEmptyEditText() {
        name = editTextName.getText().toString();
        sekolah = editTextSekolah.getText().toString();
        if (TextUtils.isEmpty(editTextName.getText().toString()) && TextUtils.isEmpty(editTextSekolah.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Sila isi maklumat anda", Toast.LENGTH_SHORT).show();
        } else {
            //Proceed
            launchFile();
            editTextName.setText("");
            editTextSekolah.setText("");
            editTextName.requestFocus();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hantar_karangan);
        listID();
        // Restore instance state
        if (savedInstanceState != null) {
            mFileUri = savedInstanceState.getParcelable(KEY_FILE_URI);
            userUid = savedInstanceState.getString(KEY_USER_UID);
            name = savedInstanceState.getString(KEY_FILE_NAME);
            sekolah = savedInstanceState.getString(KEY_FILE_SEKOLAH);
        }
        onNewIntent(getIntent());

        // Local broadcast receiver
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "onReceive:" + intent);
                hideProgressDialog();
                if (intent.getAction() != null)
                    switch (intent.getAction()) {
                        case MyUploadService.UPLOAD_COMPLETED:
                        case MyUploadService.UPLOAD_ERROR:
                            onUploadResultIntent(intent);
                            break;
                    }
            }
        };
        setButtonMuatNaik();

    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // Check if this Activity was launched by clicking on an upload notification
        if (intent.hasExtra(MyUploadService.EXTRA_FILE_URI)) {
            onUploadResultIntent(intent);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        //  updateUI(mAuth.getCurrentUser());

        // Register receiver for uploads and downloads
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        //   manager.registerReceiver(mBroadcastReceiver, MyDownloadService.getIntentFilter());
        manager.registerReceiver(mBroadcastReceiver, MyUploadService.getIntentFilter());
    }


    @Override
    public void onStop() {
        super.onStop();
        // Unregister download receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);
        if (requestCode == RC_TAKE_PICTURE) {
            if (resultCode == RESULT_OK) {
                mFileUri = data.getData();
                if (mFileUri != null) {
                    uploadFromUri(mFileUri, userUid, name, sekolah);
                } else {
                    Log.w(TAG, "File URI is null");
                }
            }
        }

    }

    private void uploadFromUri(Uri fileUri, String userUidl, String namel, String sekolahl) {
        Log.d(TAG, "uploadFromUri:src:" + fileUri.toString());

        // Save the File URI
        mFileUri = fileUri;
        userUid = userUidl;
        name = namel;
        sekolah = sekolahl;
        // Clear the last download, if any
        // updateUI(mAuth.getCurrentUser());
        //mDownloadUrl = null;

        // Start MyUploadService to upload the file, so that the file is uploaded
        // even if this Activity is killed or put in the background
        startService(new Intent(this, MyUploadService.class)
                .putExtra(MyUploadService.EXTRA_FILE_URI, fileUri)
                .putExtra(MyUploadService.EXTRA_USER_UID, userUidl)
                .putExtra(MyUploadService.EXTRA_USER_NAME, namel)
                .putExtra(MyUploadService.EXTRA_USER_SEKOLAH, sekolahl)
                .setAction(MyUploadService.ACTION_UPLOAD));

        // Show loading spinner
        showProgressDialog(getString(R.string.progress_uploading));
    }


    private void launchFile() {
        Log.d(TAG, "launchFile");

        // Pick an image from storage
        @SuppressLint("InlinedApi") Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        //intent.setType("image/*");
        // intent.setType("application/msword,application/pdf");
        // intent.addCategory(Intent.CATEGORY_OPENABLE);

        intent.setType("*/*");
        //allows to select data and return it
        //  intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, RC_TAKE_PICTURE);
    }

    @Override
    public void onSaveInstanceState(Bundle out) {
        super.onSaveInstanceState(out);
        out.putParcelable(KEY_FILE_URI, mFileUri);
        out.putString(KEY_USER_UID, userUid);
        out.putString(KEY_FILE_NAME, name);
        out.putString(KEY_FILE_SEKOLAH, sekolah);
    }

    private void onUploadResultIntent(Intent intent) {
        // Got a new intent from MyUploadService with a success or failure
        //   mDownloadUrl = intent.getParcelableExtra(MyUploadService.EXTRA_DOWNLOAD_URL);
        mFileUri = intent.getParcelableExtra(MyUploadService.EXTRA_FILE_URI);
        userUid = intent.getStringExtra(MyUploadService.EXTRA_USER_UID);
        name = intent.getStringExtra(MyUploadService.EXTRA_USER_NAME);
        sekolah = intent.getStringExtra(MyUploadService.EXTRA_USER_SEKOLAH);

        // updateUI(mAuth.getCurrentUser());
    }

    private void showProgressDialog(String caption) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.setMessage(caption);
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    //Method button
    private void setButtonMuatNaik() {
        buttonMuatNaik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmptyEditText();
            }
        });
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

package com.example.myapplication;

import android.Manifest;
import android.app.ActivityManager;
import com.google.firebase.storage.StorageReference;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private StorageReference mStorageRef;
    DatabaseReference database;
    Uri mainUri,dbstoredPath;
    private Button startRecord, stopRecord, playRecord, stopPlay,upload;
    String pathSave = "";
    MediaRecorder mediaRecorder;

    ProgressDialog progressDialog;
    EditText text;

    MediaPlayer mediaPlayer;

    final int REQUEST_PERMISSION_CODE = 1000;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);




        //Initialize View
        startRecord = (findViewById(R.id.captureAudio));
        stopRecord = (findViewById(R.id.stoprecording));
        playRecord = (findViewById(R.id.playaudio));
        stopPlay = (findViewById(R.id.stopaudio));
        upload = (findViewById(R.id.upload));




        //Check Permissions
        if(checkAccessPermission()){
            startRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pathSave = Environment.getExternalStorageDirectory()
                            .getAbsolutePath()+"/"
                            + UUID.randomUUID().toString()+ "_audio_record.3gp";
                    mainUri = Uri.fromFile(new File(pathSave));
                    setUpMediaRecorder();
                    try{
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    playRecord.setEnabled(false);
                    stopPlay.setEnabled(false);

                    Toast.makeText(MainActivity.this, "Recording...", Toast.LENGTH_LONG).show();
                }
            });
            upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadAudio();
                }
            });
            stopRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaRecorder.stop();
                    stopRecord.setEnabled(false);
                    playRecord.setEnabled(true);
                    startRecord.setEnabled(true);
                    stopPlay.setEnabled(false);
                }
            });


            playRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    stopPlay.setEnabled(true);
                    startRecord.setEnabled(false);
                    stopRecord.setEnabled(false);

                    mediaPlayer = new MediaPlayer();
                    try{
                        mediaPlayer.setDataSource(pathSave);
                        mediaPlayer.prepare();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.start();
                    Toast.makeText(MainActivity.this, "Plaing Recorded Audio", Toast.LENGTH_LONG).show();
                }
            });


            stopPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    playRecord.setEnabled(true);
                    startRecord.setEnabled(true);
                    stopRecord.setEnabled(false);
                    stopPlay.setEnabled(false);


                    if(mediaPlayer != null){
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        setUpMediaRecorder();
                    }
                }
            });
        }
        else{
            requestPermission();
        }


    }
    private void uploadAudio() {

//        mProgressDialog.setMessage("Uploading Audio...");//declare it globally and initialize it with passing the current activity i.e this
//        mProgressDialog.show();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference mFilePath = storage.getReferenceFromUrl("gs://fir-videouploading-74284.appspot.com/");
        mFilePath = mStorageRef;

        Uri u = Uri.fromFile(new File(pathSave));
        mFilePath.putFile(u).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

//                mProgressDialog.dismiss();
//                mTextView.setText("Audio has been Uploaded Successfully !!!");
            }
        });
    }
    private void setUpMediaRecorder() {

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(pathSave);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        },REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case REQUEST_PERMISSION_CODE:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this, " Permission Granted", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(this, " Permission Denied", Toast.LENGTH_LONG).show();
                }
            }
            break;


        }

    }

    private boolean checkAccessPermission() {
        int write_permission_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return write_permission_result == PackageManager.PERMISSION_GRANTED &&
                record_audio_result == PackageManager.PERMISSION_GRANTED;
    }}





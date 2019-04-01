package hire.bennett.myapplication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;


import com.example.myapplication.R;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import br.com.bloder.magic.view.MagicButton;

public class MainActivity extends AppCompatActivity {
    private String fileName = null;
    private int recFile=0;
    private TextView phrase;
    private Button startRecord, stopRecord, playRecord, stopPlay,upload,resetRecord;
    String pathSave = "";
    MediaRecorder mediaRecorder;
    private File  instanceRecord;

    private StorageReference storage;
    private ProgressDialog progressDialog;

    private int flag=0;
    private int checker=0;
    MediaPlayer mediaPlayer;

    final int REQUEST_PERMISSION_CODE = 1000;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);

        SharedPreferences settings=getSharedPreferences("Preference",MODE_PRIVATE);
        String firstStart=settings.getString("FirstTimeInstalled","");
        if (firstStart.equals("Yes")){





        }
        else{
            SharedPreferences.Editor editor=settings.edit();
            editor.putString("FirstTimeInstalled","Yes");
            editor.apply();
            TapTargetView.showFor(this,                 // `this` is an Activity
                    TapTarget.forView(findViewById(R.id.captureAudio), "Audio Recorder", "Tap here to start audio recording").tintTarget(false).outerCircleColor(R.color.followers));

        }
        MagicButton btnYoutube = (MagicButton) findViewById(R.id.magic_button_youtube);
        btnYoutube.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checker == 1) {
                    Intent intent = new Intent(MainActivity.this, Video.class);
                    String name = getIntent().getStringExtra("username");
                    String email = getIntent().getStringExtra("email");
                    String number = getIntent().getStringExtra("number");
                    intent.putExtra("username", name);
                    intent.putExtra("email", email);
                    intent.putExtra("number", number);
                    startActivity(intent);
                    finish();
                } else {

                    Toast.makeText(MainActivity.this, "No File is Recorded", Toast.LENGTH_SHORT).show();

                }
            }
        });






        //Initialize View
        phrase=(TextView)findViewById(R.id.phrase);

        Random rand = new Random();
        int rand_int1 = rand.nextInt(3);
        if (rand_int1==0){
            phrase.setText("Peter Piper picked a peck of pickled peppers\n" +
                    "A peck of pickled peppers Peter Piper picked\n" +
                    "If Peter Piper picked a peck of pickled peppers\n" +
                    "Where’s the peck of pickled peppers Peter Piper picked?");

        }
        else if(rand_int1==1){
            phrase.setText("Betty Botter bought some butter\n" +
                    "But she said the butter’s bitter\n" +
                    "If I put it in my batter, it will make my batter bitter\n" +
                    "But a bit of better butter will make my batter better\n" +
                    "So ‘twas better Betty Botter bought a bit of better butter");


        }
        else {
            phrase.setText("How much wood would a woodchuck chuck if a woodchuck could chuck wood?\n" +
                    "He would chuck, he would, as much as he could, and chuck as much wood\n" +
                    "As a woodchuck would if a woodchuck could chuck wood");


        }
        startRecord = (findViewById(R.id.captureAudio));
        resetRecord = (findViewById(R.id.resetrecording));
        upload = (findViewById(R.id.upload));
        storage = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/AudioRecording.3gp";
        instanceRecord = new File(fileName);
        if(!instanceRecord.exists()){
            try {
                instanceRecord.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        startRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkAccessPermission()){

                pathSave = Environment.getExternalStorageDirectory()
                        .getAbsolutePath()+"/"
                        + UUID.randomUUID().toString()+ "_audio_record.3gp";
                setUpMediaRecorder();
                try{
                    mediaRecorder.setMaxDuration(20000);
                    mediaRecorder.prepare();

                    mediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
                        @Override
                        public void onInfo(MediaRecorder mr, int what, int extra) {
                            if(what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED){
                                mediaRecorder.stop();
                                flag=0;

                                recFile=1;
                                Toast.makeText(MainActivity.this, "Recording stops. Limit reached", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    mediaRecorder.start();
                    startRecord.setEnabled(false);
                    flag=1;
                } catch (IOException e) {
                    e.printStackTrace();
                }


                Toast.makeText(MainActivity.this, "Recording...", Toast.LENGTH_SHORT).show();
            }
            else{

                    requestPermission();
                }

            }
        });



        upload.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          if(checkAccessPermission()){


                                          try {
                                              if(flag==1){
                                                  mediaRecorder.stop();

                                                  recFile=1;

                                              }
                                              if(recFile==0) throw new NullPointerException("demo");

                                              uploadAudio();
                                              startRecord.setEnabled(true);
                                              recFile=0;
                                              flag=0;
                                              checker=1;
                                              Toast.makeText(MainActivity.this, "Successfully Updated ", Toast.LENGTH_SHORT).show();

                                          }
                                          catch(Exception e){
                                              Toast.makeText(MainActivity.this, "No clip is recorded ", Toast.LENGTH_SHORT).show();
                                          }
                                      }
                                          else{

                                              requestPermission();
                                          }
                                      }
                                  }





        );


        resetRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkAccessPermission()){
                Toast.makeText(MainActivity.this, "Reset", Toast.LENGTH_SHORT).show();
                try {
                    if (flag == 1) {
                        mediaRecorder.stop();
                        flag = 0;

                    }
                    recFile = 0;
                    mediaRecorder.release();
                    File file = new File(fileName);
                    boolean deleted = file.delete();
                    startRecord.setEnabled(true);
                    if (deleted) {
                        //recordText.setText("Reset");

                        Random rand = new Random();
                        int rand_int1 = rand.nextInt(3);
                        if (rand_int1 == 0) {
                            phrase.setText("Peter Piper picked a peck of pickled peppers\n" +
                                    "A peck of pickled peppers Peter Piper picked\n" +
                                    "If Peter Piper picked a peck of pickled peppers\n" +
                                    "Where’s the peck of pickled peppers Peter Piper picked?");

                        } else if (rand_int1 == 1) {
                            phrase.setText("Betty Botter bought some butter\n" +
                                    "But she said the butter’s bitter\n" +
                                    "If I put it in my batter, it will make my batter bitter\n" +
                                    "But a bit of better butter will make my batter better\n" +
                                    "So ‘twas better Betty Botter bought a bit of better butter");


                        } else {
                            phrase.setText("How much wood would a woodchuck chuck if a woodchuck could chuck wood?\n" +
                                    "He would chuck, he would, as much as he could, and chuck as much wood\n" +
                                    "As a woodchuck would if a woodchuck could chuck wood");


                        }
                    }


                } catch (Exception e) {
                    Log.d("1", e + " ");
                }

            }
                else{

                    requestPermission();
                }
            }
        });





    }

    private void setUpMediaRecorder() {

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(fileName);
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
    }
    private void uploadAudio() {
        progressDialog.setMessage("Uploading ..");
        progressDialog.show();
        Random random = new Random();
        String name=getIntent().getStringExtra("username");
        String email=getIntent().getStringExtra("email");
        String number=getIntent().getStringExtra("number");

        StorageReference filePath = storage.child("Audio Records ").child(name+"-"+email+"-"+number+".3gp");

        Uri uri = Uri.fromFile(new File(fileName));
        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
               //recordText.setText("Upload Fi nished !");

            }
        });

        filePath.putFile(uri).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();

                Random rand = new Random();
                int rand_int1 = rand.nextInt(3);
                if (rand_int1==0){
                    phrase.setText("Peter Piper picked a peck of pickled peppers\n" +
                            "A peck of pickled peppers Peter Piper picked\n" +
                            "If Peter Piper picked a peck of pickled peppers\n" +
                            "Where’s the peck of pickled peppers Peter Piper picked?");

                }
                else if(rand_int1==1){
                    phrase.setText("Betty Botter bought some butter\n" +
                            "But she said the butter’s bitter\n" +
                            "If I put it in my batter, it will make my batter bitter\n" +
                            "But a bit of better butter will make my batter better\n" +
                            "So ‘twas better Betty Botter bought a bit of better butter");


                }
                else {
                    phrase.setText("How much wood would a woodchuck chuck if a woodchuck could chuck wood?\n" +
                            "He would chuck, he would, as much as he could, and chuck as much wood\n" +
                            "As a woodchuck would if a woodchuck could chuck wood");


                }

            }
        });

    return;


    }
}





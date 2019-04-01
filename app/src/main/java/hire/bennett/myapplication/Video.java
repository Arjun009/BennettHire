package hire.bennett.myapplication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.bloder.magic.view.MagicButton;

public class Video extends AppCompatActivity {
    String[] permissions = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,

            Manifest.permission.RECORD_AUDIO,
    };
    MediaRecorder mediaRecorder;
    private File instanceRecord;
    private int flag=0;
    private int checker=0;
    private StorageReference storage;
    private ProgressDialog progressDialog;
    private static int VIDEO_REQUEST=101;
    public Uri videoUri=null;
    private TextView recordText;
    private Button upload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);


        SharedPreferences settings=getSharedPreferences("Preference",MODE_PRIVATE);
        String firstStart=settings.getString("FirstTimeInstalled_1","");
        if (firstStart.equals("Yes")){


        }
        else{
            SharedPreferences.Editor editor=settings.edit();
            editor.putString("FirstTimeInstalled_1","Yes");
            editor.apply();
            TapTargetView.showFor(this,                 // `this` is an Activity
                    TapTarget.forView(findViewById(R.id.captureVideo), "Video Recorder", "Tap here to start video recording").tintTarget(false).outerCircleColor(R.color.deeppurple));

        }



        checkPermissions();
        storage = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        MagicButton btnYoutube = (MagicButton) findViewById(R.id.magic_button_youtube_1);
        btnYoutube.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checker == 1) {
                    Intent intent = new Intent(Video.this, pdf.class);
                    String name = getIntent().getStringExtra("username");
                    String email = getIntent().getStringExtra("email");
                    String number = getIntent().getStringExtra("number");
                    intent.putExtra("username", name);
                    intent.putExtra("email", email);
                    intent.putExtra("number", number);
                    startActivity(intent);
                    finish();
                } else {

                    Toast.makeText(Video.this, "No Video is Recorded", Toast.LENGTH_SHORT).show();

                }
            }
        });



        recordText = findViewById(R.id.record_text);
        upload = (findViewById(R.id.upload));
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(flag==0) throw new NullPointerException("demo");
                progressDialog.setMessage("Uploading ..");
                progressDialog.show();



                    String name=getIntent().getStringExtra("username");
                    String email=getIntent().getStringExtra("email");
                    String number=getIntent().getStringExtra("number");
                StorageReference filePath = storage.child("Video").child(name+"-"+email+"-"+number+".mp4");

                filePath.putFile(videoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        recordText.setText("Upload Finished !");
                        flag=0;
                        Toast.makeText(Video.this, "Successfully Updated ", Toast.LENGTH_SHORT).show();

                        checker=1;

                    }
                });

                filePath.putFile(videoUri).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        recordText.setText("Failed to upload !");

                    }});


                }
                catch(Exception e){
                    Toast.makeText(Video.this, "No Video recorded ", Toast.LENGTH_LONG).show();
                }
            }

        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // do something
            }
            return;
        }
    }
    public void captureVideo(View view) {
        flag=1;
        try {
            Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            videoIntent.putExtra("android.intent.extra.durationLimit", 27);
            videoIntent.putExtra("EXTRA_VIDEO_QUALITY", 0);
            if (videoIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(videoIntent, VIDEO_REQUEST);

            }
        }
        catch(Exception e){
            Toast.makeText(Video.this, "Cannot play video, Record once again. Not allowed to go to another activity.", Toast.LENGTH_LONG).show();

        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==VIDEO_REQUEST && resultCode==RESULT_OK){
            videoUri=data.getData();
        }
    }

//        public void uploadData(Uri videoUri) {
//            progressDialog.setMessage("Uploading ..");
//            progressDialog.show();
//            Random random2 = new Random();
//            String str2 = String.valueOf(random2.nextInt(10000));
//
//
//            StorageReference filePath2 = storage.child("Video").child("new_video"+str2+".mp4");
//            if(videoUri != null){
//                UploadTask uploadTask = filePath2.putFile(videoUri);
//
//                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                        if(task.isSuccessful())
//                            Toast.makeText(Video.this, "Upload Complete", Toast.LENGTH_SHORT).show();
//
//                    }
//                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//
//                    }
//                });
//            }else {
//                Toast.makeText(Video.this, "Nothing to upload", Toast.LENGTH_SHORT).show();
//            }
//
//        }
    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }


}
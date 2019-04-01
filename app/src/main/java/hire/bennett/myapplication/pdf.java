package hire.bennett.myapplication;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.R;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import br.com.bloder.magic.view.MagicButton;

public class pdf extends AppCompatActivity {

    // Pdf is uploading from storage..not from google drive or any other place.. coz its taking pdf from storage path .... other could be content://

    private StorageReference mStorageRef;
    Button uploadFile;
    ProgressDialog dialog;
    private int checker=0;
    public static int Request_Number = 1;



    Button pdf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        pdf = (Button) findViewById(R.id.getPdf);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Uploading Pdf. Please wait...");

        SharedPreferences settings=getSharedPreferences("Preference",MODE_PRIVATE);
        String firstStart=settings.getString("FirstTimeInstalled_4","");
        if (firstStart.equals("Yes")){


        }
        else{
            SharedPreferences.Editor editor=settings.edit();
            editor.putString("FirstTimeInstalled_4","Yes");
            editor.apply();
            TapTargetView.showFor(this,                 // `this` is an Activity
                    TapTarget.forView(findViewById(R.id.getPdf), "Uploaad Resume", "Tap the button to update resume").tintTarget(false).outerCircleColor(R.color.green) );

        }







        MagicButton btnYoutube = (MagicButton) findViewById(R.id.magic_button_youtube_2);
        btnYoutube.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checker == 1) {
                    Intent intent = new Intent(pdf.this, thankyou.class);
                    String name = getIntent().getStringExtra("username");
                    String email = getIntent().getStringExtra("email");
                    String number = getIntent().getStringExtra("number");
                    intent.putExtra("username", name);
                    intent.putExtra("email", email);
                    intent.putExtra("number", number);
                    startActivity(intent);
                    finish();
                } else {

                    Toast.makeText(pdf.this, "No Pdf is Uploaded", Toast.LENGTH_SHORT).show();

                }
            }
        });


        PDF();
    }


    public void PDF() {

        pdf.setOnClickListener(//Listens for a button click.
                new View.OnClickListener() {//Creates a new click listener.
                    @Override
                    public void onClick(View v) {//does what ever code is in here when the button is clicked

                        Intent intent = new Intent();
                        intent.setType("application/pdf");   // its gonna make all the pdf visisble n rest hide..so that gonna make it easy for you to select pdf...
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select a PDF "), 1);


                    }
                }
        );
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //PDF
        try{
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                Uri uri = data.getData();
                //  String SelectedPDF = getPDFPath(selectedUri_PDF);
                String name=getIntent().getStringExtra("username");
                String email=getIntent().getStringExtra("email");
                String number=getIntent().getStringExtra("number");
                StorageReference filePath = mStorageRef.child("Pdfs").child(name+"-"+email+"-"+number);

                dialog.show(); // showing dialoge at the time of putting image


                filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {  // you can add more than one interface to each other ...where you put semicolon to close means method1(listener).methodw2(listener).method3(listener);
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        dialog.dismiss();
                        checker=1;
                        Toast.makeText(getApplicationContext(), "Pdf Uploaded Sucessfully",Toast.LENGTH_SHORT ).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(pdf.this, "Pdf Uploading Failed",Toast.LENGTH_SHORT ).show();

                    }
                });

            }
        }}
        catch (Exception e){
            Toast.makeText(pdf.this, "No pdf selected ", Toast.LENGTH_LONG).show();
        }
    }

/*    public String getPDFPath(Uri uri) {
        final String id = DocumentsContract.getDocumentId(uri);
        final Uri contentUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }*/


}
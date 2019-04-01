package hire.bennett.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;

public class name extends AppCompatActivity {



    public EditText username,email,number;
    private Button btn_signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        username=(EditText)findViewById(R.id.username);
        email=(EditText)findViewById(R.id.email);
        number=(EditText)findViewById(R.id.phone);
        btn_signUp=(Button)findViewById(R.id.btn_signUp);


        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(username.getText().toString()) && !TextUtils.isEmpty(email.getText().toString()) && !TextUtils.isEmpty(number.getText().toString()) ) {
                    Intent intent = new Intent(name.this, MainActivity.class);
                    intent.putExtra("username", username.getText().toString());
                    intent.putExtra("email", email.getText().toString());
                    intent.putExtra("number", number.getText().toString());
                    startActivity(intent);


                }
                else{
                    Toast.makeText(name.this,"Please fill all the entries",Toast.LENGTH_LONG).show();

                }

            }
        });


    }
}

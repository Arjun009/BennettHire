package hire.bennett.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.myapplication.R;

public class thankyou extends AppCompatActivity {
    private TextView thanks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankyou);
        thanks=(TextView)findViewById(R.id.thanks);
        thanks.setText("Thank you, "+getIntent().getStringExtra("username")+" \uD83D\uDCAE  \n We will get back to you shortly. ");
    }
}

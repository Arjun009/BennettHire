package hire.bennett.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.ms.square.android.expandabletextview.ExpandableTextView;


import java.text.SimpleDateFormat;

public class scrollview extends AppCompatActivity implements View.OnClickListener{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollview);

        SharedPreferences settings=getSharedPreferences("Preference",MODE_PRIVATE);
        String firstStart=settings.getString("FirstTimeInstalled_2","");
        if (firstStart.equals("Yes")){


        }
        else{
            SharedPreferences.Editor editor=settings.edit();
            editor.putString("FirstTimeInstalled_2","Yes");
            editor.apply();
            TapTargetView.showFor(this,                 // `this` is an Activity
                    TapTarget.forView(findViewById(R.id.next_1), "Start Screening", "Read all the instructions and tap start to proceed").tintTarget(false).outerCircleColor(R.color.deeppurple) );

        }





        TextView textab=(TextView)findViewById(R.id.textab);
        long date=System.currentTimeMillis();
        SimpleDateFormat aa=new SimpleDateFormat("dd MMM yyyy, h:mm a");
        String datestring=aa.format(date);
        textab.setText(""+datestring);
        ExpandableTextView expTv1 = (ExpandableTextView)findViewById(R.id.expand_text_view);
        expTv1.setText(getString(R.string.in_news));
//        MagicButton btnYoutube=(MagicButton) findViewById(R.id.magic_button_youtube);


        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        Button bu =(Button)findViewById(R.id.next_1);
        bu.setOnClickListener(this);
        CollapsingToolbarLayout cool=(CollapsingToolbarLayout)findViewById(R.id.ab12);
        setSupportActionBar(toolbar);
        cool.setTitle("Bennett Hire");
//        btnYoutube.setMagicButtonClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent ii=new Intent(scrollview.this,value.class);
//                startActivity(ii);
//            }
//        }

//        bu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent ii=new Intent(scrollview.this,value.class);
//                startActivity(ii);
//            }
//        });
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        cool.setCollapsedTitleTextAppearance(R.style.Text123);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(scrollview.this,splash.class);
                startActivity(in);

            }
        });


    }




    @Override
    public void onClick(View v) {

            switch (v.getId()) {
                case R.id.next_1:
                    Intent ii=new Intent(scrollview.this,name.class);
                    startActivity(ii);
                    break;



        }
    }

}
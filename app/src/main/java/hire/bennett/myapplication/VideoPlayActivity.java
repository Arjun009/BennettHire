package hire.bennett.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.net.Uri;
import android.widget.VideoView;

import com.example.myapplication.R;

public class VideoPlayActivity extends AppCompatActivity {

    private VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        mVideoView= findViewById(R.id.videoView);
        Uri videoUri = Uri.parse(getIntent().getExtras().getString("videoUri"));
        mVideoView.setVideoURI(videoUri);
        mVideoView.start();
    }
}

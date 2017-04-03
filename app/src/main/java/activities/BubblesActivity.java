package activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.example.mtmwi.needleyouneed.R;
public class BubblesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bubbles_view);

        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.social_media);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent( v.getContext(), SocialMediaActivity.class );
                startActivity( intent );
            }
        });
    }
}

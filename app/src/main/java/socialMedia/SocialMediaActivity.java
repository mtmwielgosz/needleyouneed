package socialMedia;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.mtmwi.needleyouneed.R;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SocialMediaActivity extends AppCompatActivity {

    CallbackManager callbackManager = CallbackManager.Factory.create();
    FeedAdapter adapter;
    @BindView(R.id.feed_recycler_view)
    RecyclerView feedRecyclerView;
    List<Feed> feedList;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        feedList = new ArrayList<Feed>();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getBaseContext());
        feedRecyclerView.setLayoutManager(mLayoutManager);
        feedRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new FeedAdapter(getBaseContext(), feedList);
        feedRecyclerView.setAdapter(adapter);

/*
        if (!SocialMediaHelper.isLoggedInInstagram()) { // TODO log in Instagram?
            SocialMediaHelper.logInInstagram(this);
        }
*/
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                adapter.clear();
                SocialMediaHelper.synchronizeWithSocialMedia(swipeRefreshLayout, adapter).execute();
            }
        });

}


    //@OnClick(R.id.message_fab)
    public void onViewClicked() {


        // TODO open Bubbles!
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
        SocialMediaHelper.executeInstagramOnResult(requestCode, resultCode);
    }

}

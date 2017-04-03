package activities;

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

import adapters.FeedAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import entities.Feed;
import other.SocialMediaHelper;


public class SocialMediaActivity extends AppCompatActivity {

    CallbackManager callbackManager = CallbackManager.Factory.create();
    FeedAdapter adapter;
    @BindView(R.id.feed_recycler_view)
    RecyclerView feedRecyclerView;
    @BindView(R.id.fb_login_button)
    LoginButton fbLoginButton;
    List<Feed> feedList;

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


        if (!SocialMediaHelper.isLoggedInFacebook()) {
            fbLoginButton.setVisibility(View.VISIBLE);
            fbLoginButton.registerCallback(callbackManager, SocialMediaHelper.getFacebookCallback(fbLoginButton));
        } else {
            fbLoginButton.setVisibility(View.GONE);
        }

        if (!SocialMediaHelper.isLoggedInInstagram()) {
            SocialMediaHelper.logInInstagram(this);
        }

    }


    @OnClick(R.id.message_fab)
    public void onViewClicked() {

      feedList.clear();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        adapter.clear();
        SocialMediaHelper.getFacebookRequest(feedList).executeAndWait();
        AsyncTask at = SocialMediaHelper.getInstagramRequest(feedList).execute();

        try {
            at.get(10000L, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
//
//        try {
//            at1.get(100000L, TimeUnit.MILLISECONDS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (TimeoutException e) {
//            e.printStackTrace();
//        }

        adapter.clear();
        Collections.sort(feedList);
       // adapter = new FeedAdapter(getBaseContext(), feedList);
       // feedRecyclerView.setAdapter(adapter);
        adapter.addAll(feedList);

        //startActivity(new Intent(getApplicationContext(), SendActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
        SocialMediaHelper.executeInstagramOnResult(requestCode, resultCode);
    }

}

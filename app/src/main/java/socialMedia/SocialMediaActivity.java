package socialMedia;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.mtmwi.needleyouneed.R;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.github.fabtransitionactivity.SheetLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import bubbles.BubblesActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SocialMediaActivity extends AppCompatActivity implements SheetLayout.OnFabAnimationEndListener {

    private int REQUEST_CODE_EXPAND_FAB = 5555;
    FeedAdapter adapter;
    @BindView(R.id.feed_recycler_view)
    RecyclerView feedRecyclerView;
    List<Feed> feedList;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.bottom_sheet)
    SheetLayout sheetLayout;
    @BindView(R.id.bubbles_menu)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(!noFeedsAvalaible()) {
            feedList = FeedAgregate.INSTANCE.getFeeds();
        }else{
            SocialMediaHelper.synchronizeWithSocialMedia();
            feedList = FeedAgregate.INSTANCE.getFeeds();
        }
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getBaseContext());
        feedRecyclerView.setLayoutManager(mLayoutManager);
        feedRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new FeedAdapter(getBaseContext(), feedList);
        feedRecyclerView.setAdapter(adapter);

        sheetLayout.setFab(fab);
        sheetLayout.setFabAnimationEndListener(this);

/*
        if (!SocialMediaHelper.isLoggedInInstagram()) { // TODO log in Instagram?
            SocialMediaHelper.logInInstagram(this);
        }
*/
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                adapter.clear();
                SocialMediaHelper.synchronizeWithSocialMedia(swipeRefreshLayout).execute();
                feedList = FeedAgregate.INSTANCE.getFeeds();
                adapter = new FeedAdapter(getBaseContext(), feedList);
            }
        });

}

    private boolean noFeedsAvalaible() {
        return FeedAgregate.INSTANCE.getFeeds().isEmpty();
    }


    @OnClick(R.id.bubbles_menu)
    public void onViewClicked() {

        try {
            getSupportActionBar().hide();
            sheetLayout.expandFab();
        } catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onFabAnimationEnd() {

        Intent intent = new Intent(this, BubblesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent, REQUEST_CODE_EXPAND_FAB);
        overridePendingTransition(0,0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_EXPAND_FAB){
            sheetLayout.contractFab();
            getSupportActionBar().show();
        }

        SocialMediaHelper.executeInstagramOnResult(requestCode, resultCode);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition( R.anim.out, 0);
    }
}

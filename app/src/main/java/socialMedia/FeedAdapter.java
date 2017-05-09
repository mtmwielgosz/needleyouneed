package socialMedia;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mtmwi.needleyouneed.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by mtmwi on 21.03.2017.
 */

class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.MyViewHolder> {

    private Context mContext;
    private List<Feed> feedList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView updatedTime;
        public TextView message;
        public ImageView picture;

        public MyViewHolder(View view) {
            super(view);
            updatedTime = (TextView) view.findViewById(R.id.fb_updated_time);
            message = (TextView) view.findViewById(R.id.fb_message);
            picture = (ImageView) view.findViewById(R.id.fb_picture);
        }
    }

    public FeedAdapter(Context mContext, List<Feed> feedList) {
        this.mContext = mContext;
        this.feedList = getFeedsWithPictures(feedList);
        Collections.sort(this.feedList);
    }

    private List<Feed> getFeedsWithPictures(List<Feed> feedList) {

        List<Feed> result = new ArrayList<Feed>();
        for(Feed feed : feedList) {
            if(feed != null && !JsonConverter.isEmpty(feed.getPicture())) {
                result.add(feed);
            }
        }

        return result;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feed_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final Feed feed = feedList.get(position);
        holder.updatedTime.setText(feed.getUpdatedTime());
        holder.message.setText(feed.getMessage());
        new DownloadImageTask(holder.picture).execute(feed.getPicture());

        View.OnClickListener openFbLink = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(feed.isFaceBook()){
                    openFaceBook(feed);
                }else{
                    openInstagram(feed);
                }
            }
        };

        holder.message.setOnClickListener(openFbLink);
        holder.picture.setOnClickListener(openFbLink);

        if(position % 2 == 0) {
            holder.itemView.setBackgroundColor(Color.parseColor("#bacdff"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#bacdff"));
        }
    }

    private void openInstagram(Feed feed) {
        Intent showPage = new Intent(Intent.ACTION_VIEW, Uri.parse(feed.getLink()));
        showPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(showPage);
    }

    private void openFaceBook(Feed feed) {
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = mContext.getPackageManager().getApplicationInfo("com.facebook.katana", 0);
        }catch (Exception e){}

        if (applicationInfo != null && !applicationInfo.enabled) {
            Intent showPage = new Intent(Intent.ACTION_VIEW, Uri.parse(feed.getLink()));
            showPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(showPage);
        }else{
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(feed.getBrowserAddress()));
            browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(browserIntent);
        }

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap icon = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                icon = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return icon;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


    @Override
    public int getItemCount() {
        return feedList.size();
    }

    public void clear() {

        feedList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Feed> list) {

        feedList.addAll(list);
        Collections.sort(feedList);
        notifyDataSetChanged();
    }
}


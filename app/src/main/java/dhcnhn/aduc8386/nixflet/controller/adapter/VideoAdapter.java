package dhcnhn.aduc8386.nixflet.controller.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dhcnhn.aduc8386.nixflet.R;
import dhcnhn.aduc8386.nixflet.model.Video;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {

    private List<Video> videos;

    public VideoAdapter(List<Video> videos) {
        this.videos = videos;
    }

    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);

        return new VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {

//        String format = String.format("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/%s\" frameborder=\"0\"></iframe>", videos.get(position).getKey());


        String format = "<iframe width=\"100%\" height=\"100%\"\n" +
                "  src=\"https://www.youtube.com/embed/"+videos.get(position).getKey()+"\"\n" +
                "  frameborder=\"0\"allowfullscreen></iframe>";

        Log.d("TAG", "onBindViewHolder: " + videos.get(position).getKey());

        holder.webView.loadData(format, "text/html", "utf-8");
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    static class VideoHolder extends RecyclerView.ViewHolder {

        private final WebView webView;

        public VideoHolder(@NonNull View itemView) {
            super(itemView);

            webView = itemView.findViewById(R.id.web_view_item_video_movie_video);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setLoadsImagesAutomatically(true);
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            webView.getSettings().setAppCacheEnabled(true);
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
//            webView.getSettings().setUseWideViewPort(true);
            webView.setWebViewClient(new WebViewClient());
//            webView.setWebViewClient(new WebViewClient());
        }

//        private class ChromeClient extends WebChromeClient {
//
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.MATCH_PARENT);
//
//            ViewGroup parent;
//            ViewGroup content;
//            View customView;
//
//            public ChromeClient(ViewGroup parent, ViewGroup content){
//                this.parent = parent;
//                this.content = content;
//            }
//
//            @Override
//            public void onShowCustomView(View view, CustomViewCallback callback) {
//                super.onShowCustomView(view, callback);
//
//                customView = view;
//                view.setLayoutParams(layoutParams);
//                parent.addView(view);
//                content.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onHideCustomView() {
//                super.onHideCustomView();
//
//                content.setVisibility(View.VISIBLE);
//                parent.removeView(customView);
//                customView = null;
//            }
//
//        }

    }

}

package MyFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.liu.asus.dianshang.R;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Adapter.Myxyipager;
import bean.Shangpin;
import okhttp3.Call;
import okhttp3.Response;
import utils.Okhttputils;

/**
 * Created by asus on 2017/9/28.
 */

public class frag_xer extends Fragment {
    private View view;
    private  WebView wb;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.frag_xer, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initview();
        setting();
        initdata();

    }
    private void initdata() {
        Intent intent = getActivity().getIntent();
        String xurl = intent.getStringExtra("url");
        wb.loadUrl("https://mitem.jd.hk/ware/view.action?wareId=1988853309&cachekey=fa64038ee4a09d2abdc6a1413a4535aa");
    }
    private void setting() {
        wb.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wb.getSettings().setJavaScriptEnabled(true);
        wb.getSettings() .setSupportZoom(true);
        wb.getSettings().setUseWideViewPort(true);
        wb.getSettings().setLoadWithOverviewMode(true);
        wb.setWebViewClient(new WebViewClient());

    }

    private void initview() {
        wb = view.findViewById(R.id.wb);
    }


}

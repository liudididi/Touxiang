package MyFragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.liu.asus.dianshang.R;
import com.liu.asus.dianshang.Sousuo;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bean.Fenleibean;
import model.Myviewpager;
import okhttp3.Call;
import okhttp3.Response;
import utils.Okhttputils;

/**
 * Created by asus on 2017/9/28.
 */

public class frag_fenlei extends Fragment implements ViewPager.OnPageChangeListener {
    private  View view;
    private List<TextView> textlist;
    private ScrollView tools_scrllview;
    private int scrollViewWidth = 0;
    private Myviewpager tools_viewpager;
    private LinearLayout tools_linear;
    private LinearLayout fl_sousuo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.frag_fl, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tools_linear = view.findViewById(R.id.tools_linear);
        tools_scrllview = view.findViewById(R.id.tools_scrlllview);
        tools_viewpager = view.findViewById(R.id.tools_viewpager);
        fl_sousuo = view.findViewById(R.id.fl_sousuo);
        fl_sousuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),Sousuo.class);
                startActivity(intent);
            }
        });
        tools_scrllview.setSmoothScrollingEnabled(true);
        textlist = new ArrayList<>();
        List<Fragment> fragments=new ArrayList<>();
        fragments.add(new frag_jdcs());
        fragments.add(new frag_qqg());
        fragments.add(new frag_sjsm());
        fragments.add(new frag_nvz());
        fragments.add(new frag_nvz());
        fragments.add(new frag_nx());
        fragments.add(new frag_nvx());
        fragments.add(new frag_nyps());
        fragments.add(new frag_xbsd());
        fragments.add(new frag_mzgh());
        fragments.add(new frag_scp());
        fragments.add(new frag_dnbg());
        fragments.add(new frag_jydq());
        fragments.add(new frag_spsx());
        fragments.add(new frag_jsyl());
        fragments.add(new frag_mytz());
        fragments.add(new frag_wjlq());
        fragments.add(new frag_yybj());
         Myflviewpager  myflviewpager=new Myflviewpager(getChildFragmentManager(),fragments);
        tools_viewpager.setAdapter(myflviewpager);
        tools_viewpager.setOnPageChangeListener(this);
        tools_viewpager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        Okhttputils.requer("http://120.27.23.105/product/getCatagory", new Okhttputils.Backquer() {
            @Override
            public void onfailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "请求数据失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onresponse(Call call, Response response) {
                try {
                    final List<Fenleibean> recyclebeanList=new ArrayList<>();
                    JSONObject json=new JSONObject(response.body().string());
                    JSONArray data = json.getJSONArray("data");
                    for (int i = 0; i <data.length() ; i++) {
                        JSONObject l= (JSONObject) data.get(i);
                        Fenleibean fenleibean=new Fenleibean();
                        fenleibean.name=l.optString("name");
                        fenleibean.imgurl=l.optString("icon");
                        recyclebeanList.add(fenleibean);
                    }
                    if(recyclebeanList!=null&&getActivity()!=null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                    for (int i = 0; i <recyclebeanList.size() ; i++) {
                                        View inflate = View.inflate(getActivity(), R.layout.text, null);
                                        TextView text=inflate.findViewById(R.id.fl_text);
                                        textlist.add(text);
                                        text.setText(recyclebeanList.get(i).name);
                                        tools_linear.addView(inflate);
                                    }
                                    for (int i = 0; i < textlist.size() ; i++) {
                                        final TextView textView=textlist.get(i);
                                        final int finalI = i;
                                        textView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                tools_viewpager.setCurrentItem(finalI);
                                                if(finalI >=textlist.size()/2){
                                                    scrollViewWidth = tools_scrllview.getHeight();
                                                    tools_scrllview.smoothScrollBy(0, scrollViewWidth/2);
                                                }else if(finalI<textlist.size()/2){
                                                    scrollViewWidth = tools_scrllview.getHeight();
                                                    tools_scrllview.smoothScrollBy(0, -scrollViewWidth/2);
                                                }
                                            }
                                        });
                                    }
                                for (int i = 0; i <textlist.size() ; i++) {
                                    TextView text=textlist.get(i);
                                    if(i==0){
                                        text.setTextColor(Color.RED);
                                        text.setBackgroundColor(Color.parseColor("#F3F5F7"));
                                    }else {
                                        text.setTextColor(Color.BLACK);
                                        text.setBackgroundColor(Color.WHITE);
                                    }
                                }

                                }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        for (int i = 0; i <textlist.size() ; i++) {
             TextView text=textlist.get(i);
            if(i==position){
                text.setTextColor(Color.RED);
                text.setBackgroundColor(Color.parseColor("#F3F5F7"));
            }else {
                text.setTextColor(Color.BLACK);
                text.setBackgroundColor(Color.WHITE);
            }
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class  Myflviewpager extends FragmentPagerAdapter{
         private  List<Fragment> fragments;

        public Myflviewpager(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        public Myflviewpager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}

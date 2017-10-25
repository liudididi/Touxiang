package MyFragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.liu.asus.dianshang.CustomScanActivity;


import com.liu.asus.dianshang.R;
import com.liu.asus.dianshang.Sousuo;
import com.liu.asus.dianshang.Xiangqing;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.stx.xhb.xbanner.XBanner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Adapter.Msrecycle;
import Adapter.Myrecycle;
import Adapter.Myvp;
import bean.Recyclebean;
import model.GridSpacingItemDecoration;
import model.ObservableScrollView;
import okhttp3.Call;
import okhttp3.Response;
import utils.Okhttputils;

/**
 * Created by asus on 2017/9/28.
 */

public class frag_shouye  extends Fragment implements XBanner.XBannerAdapter, SwipeRefreshLayout.OnRefreshListener, ObservableScrollView.ScrollViewListener {
    private SwipeRefreshLayout mSwipeRefreshLayout = null;
    private View view;
    private XBanner mBanner;
    private List<String> imgesUrl;
    private ViewPager vp;
    private List<Fragment> fragments;
    private LinearLayout tv_resh;
    private RecyclerView sy_recycle;
    private LinearLayout sy_title;
    private  int hight;
    private model.ObservableScrollView sy_scroll;
    private TextView sy_tvsys,sy_tvxx;
    private ImageView sy_sys,sy_xx;
    private LinearLayout sy_edsou;
    private RecyclerView ms_recycle;
    private TextView tv_hour;
    private TextView tv_minute;
    private TextView tv_second;
    private long mHour = 02;
    private long mMin = 15;
    private long mSecond = 36;
    private boolean isRun = true;
    private ImageView img_che;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.frag_sy, null);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mBanner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        mBanner.stopAutoPlay();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        img_che = view.findViewById(R.id.img_che);
        sy_edsou = view.findViewById(R.id.sy_edsou);
        sy_title = view.findViewById(R.id.sy_title);
        sy_sys = view.findViewById(R.id.sy_sys);
        sy_xx = view.findViewById(R.id.sy_xx);
        sy_tvsys = view.findViewById(R.id.sy_tvsys);
        sy_tvxx = view.findViewById(R.id.sy_tvxx);
        sy_edsou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),Sousuo.class);
                startActivity(intent);
            }
        });
        initmiaoshao();
        initjishi();
        ViewTreeObserver vto =sy_title.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                sy_title.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                hight =   sy_title.getHeight();
                sy_title.getWidth();
                sy_scroll.setScrollViewListener(frag_shouye.this);

            }
        });
        sy_title.setBackgroundColor(Color.argb(0, 0xfd, 0xfd, 0xfd));
        sy_scroll = view.findViewById(R.id.sy_scroll);
        mBanner = (XBanner) view.findViewById(R.id.banner);
        imgesUrl = new ArrayList<>();
        imgesUrl.add("https://img20.360buyimg.com/da/jfs/t10252/155/202356221/165918/2a493086/59c8d510Nbca64f46.jpg");
        imgesUrl.add("https://img14.360buyimg.com/babel/jfs/t10690/52/337512682/75468/f98cbea2/59cc56f1N7188fbe9.jpg");
        imgesUrl.add("https://img1.360buyimg.com/da/jfs/t9634/38/8710660/101124/854d2262/59c360a0Nc18de297.jpg");
        imgesUrl.add("https://img12.360buyimg.com/babel/jfs/t9598/356/19531156/199571/8459c6d5/59c39b93N7bd784d5.jpg");
        mBanner.setData(imgesUrl,null);
        mBanner.setPoinstPosition(XBanner.RIGHT);
        mBanner.setmAdapter(this);
        LinearLayout sy_saoyisao=view.findViewById(R.id.sy_saoyisao);
        sy_saoyisao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new IntentIntegrator(getActivity())
                        .setOrientationLocked(false)
                        .setCaptureActivity(CustomScanActivity.class) // 设置自定义的activity是CustomActivity
                        .initiateScan();
            }
        });
        vp = view.findViewById(R.id.vp);
        fragments = new ArrayList<>();
        frag_pageyi pageyi=new frag_pageyi();
        frag_pageer pageer=new frag_pageer();
        fragments.add(pageyi);
        fragments.add(pageer);
        Myvp myvp=new Myvp(getChildFragmentManager(),fragments);
        vp.setAdapter(myvp);
        mSwipeRefreshLayout = view.findViewById(R.id.container_swipe);
        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        // 设置下拉监听事件
        mSwipeRefreshLayout.setOnRefreshListener(this);
        tv_resh = view.findViewById(R.id.tv_resh);
        sy_recycle = view.findViewById(R.id.sy_recycle);
        getdata();
    }

    private void initjishi() {
        tv_hour = view.findViewById(R.id.tv_hour);
        tv_minute = view.findViewById(R.id.tv_minute);
        tv_second = view.findViewById(R.id.tv_second);
        startRun();
    }
    private Handler timeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1) {
                computeTime();
                if (mHour<10){
                    tv_hour.setText("0"+mHour+"");
                }else {
                    tv_hour.setText("0"+mHour+"");
                }
                if (mMin<10){
                    tv_minute.setText("0"+mMin+"");
                }else {
                    tv_minute.setText(mMin+"");
                }
                if (mSecond<10){
                    tv_second.setText("0"+mSecond+"");
                }else {
                    tv_second.setText(mSecond+"");
                }
            }
        }
    };
    private void startRun() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (isRun) {
                    try {
                        Thread.sleep(1000); // sleep 1000ms
                        Message message = Message.obtain();
                        message.what = 1;
                        timeHandler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 倒计时计算
     */
    private void computeTime() {
        mSecond--;
        if (mSecond < 0) {
            mMin--;
            mSecond = 59;
            if (mMin < 0) {
                mMin = 59;
                mHour--;
            }
        }

}
    private void initmiaoshao() {
        ms_recycle = view.findViewById(R.id.ms_recycle);
        Okhttputils.requer("http://120.27.23.105/ad/getAd", new Okhttputils.Backquer() {
            @Override
            public void onfailure(Call call, IOException e) {
                if(getActivity()!=null){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "请求数据失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
            @Override
            public void onresponse(Call call, Response response) {
                try {
                    final List<Recyclebean> recyclebeanList=new ArrayList<>();
                    JSONObject json=new JSONObject(response.body().string());
                    JSONObject  tuijian = json.getJSONObject("miaosha");
                    JSONArray list = tuijian.getJSONArray("list");
                    for (int i = 0; i <list.length() ; i++) {
                        JSONObject l= (JSONObject) list.get(i);
                        Recyclebean recyclebean=new Recyclebean();
                        recyclebean.imgurl=l.optString("images");
                        recyclebean.zi=l.optString("title");
                        recyclebean.pid=l.optInt("pid");
                        recyclebean.price=l.optInt("bargainPrice");
                        recyclebean.oldprice=l.optInt("price");
                        recyclebean.url=l.optString("detailUrl");
                        recyclebeanList.add(recyclebean);
                    }
                    if(getActivity()!=null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Msrecycle msrecyle=new Msrecycle(getActivity(),recyclebeanList);
                                msrecyle.setOnrecyclik(new Msrecycle.MsOnrecyclik() {
                                    @Override
                                    public void MyreclOnk(ImageView img, int postion) {
                                        Intent intent=new Intent(getActivity(), Xiangqing.class);
                                        intent.putExtra("url",recyclebeanList.get(postion).url);
                                        intent.putExtra("pid",recyclebeanList.get(postion).pid);
                                        getActivity().startActivity(intent);
                                    }
                                });
                                GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),1);
                                gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                                ms_recycle.setLayoutManager(gridLayoutManager);
                                ms_recycle.setAdapter(msrecyle);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });



    }

    private void getdata() {
       Okhttputils.requer("http://120.27.23.105/ad/getAd", new Okhttputils.Backquer() {
           @Override
           public void onfailure(Call call, IOException e) {
               if(getActivity()!=null){
                   getActivity().runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           Toast.makeText(getActivity(), "请求数据失败", Toast.LENGTH_SHORT).show();
                       }
                   });
               }
           }
           @Override
           public void onresponse(Call call, Response response) {
               try {
                   final List<Recyclebean> recyclebeanList=new ArrayList<>();
                   JSONObject json=new JSONObject(response.body().string());
                   JSONObject  tuijian = json.getJSONObject("tuijian");
                   JSONArray list = tuijian.getJSONArray("list");
                   for (int i = 0; i <list.length() ; i++) {
                       JSONObject l= (JSONObject) list.get(i);
                       Recyclebean recyclebean=new Recyclebean();
                       recyclebean.imgurl=l.optString("images");
                       recyclebean.zi=l.optString("title");
                       recyclebean.pid=l.optInt("pid");
                       recyclebean.price=l.optInt("bargainPrice");
                       recyclebean.url=l.optString("detailUrl");
                       recyclebeanList.add(recyclebean);
                   }
                   if(getActivity()!=null){

                       getActivity().runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               Myrecycle myrecycle=new Myrecycle(getActivity(),recyclebeanList);
                               myrecycle.setOnrecyclik(new Myrecycle.Onrecyclik() {
                                   @Override
                                   public void MyreclOnk(ImageView img, int postion) {
                                       Intent intent=new Intent(getActivity(), Xiangqing.class);
                                       intent.putExtra("url",recyclebeanList.get(postion).url);
                                       intent.putExtra("pid",recyclebeanList.get(postion).pid);
                                       getActivity().startActivity(intent);
                                   }
                               });
                               GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),2);
                               sy_recycle.setLayoutManager(gridLayoutManager);
                               sy_recycle.setAdapter(myrecycle);
                               int spanCount = 2;
                               int spacing = 10;
                               boolean includeEdge = false;
                               sy_recycle.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
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
    public void loadBanner(XBanner banner, View view, int position) {
        ImageLoader.getInstance().displayImage(imgesUrl.get(position), (ImageView) view);
    }
    @Override
    public void onRefresh() {
        // 模拟耗时更新操作
        tv_resh.setVisibility(View.VISIBLE);
        TranslateAnimation translateAnimation=new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,0f,
                Animation.RELATIVE_TO_SELF,6f,
                Animation.RELATIVE_TO_SELF,0f,
                Animation.RELATIVE_TO_SELF,0f
                );
        translateAnimation.setDuration(3000);
        translateAnimation.setRepeatMode(Animation.RESTART);
        img_che.startAnimation(translateAnimation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tv_resh.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "已经是最新数据了哦~", Toast.LENGTH_SHORT).show();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        //      Log.i("TAG","y--->"+y+"    height-->"+height);
        if(y<=hight){
            float scale =(float) y /hight;
            float alpha =  (255 * scale);
//          Log.i("TAG","alpha--->"+alpha);
            //layout全部透明
//          layoutHead.setAlpha(scale);
            //只是layout背景透明(仿知乎滑动效果)
            sy_edsou.setBackground(getResources().getDrawable(R.drawable.ed_bg));
            sy_xx.setImageResource(R.drawable.xx);
            sy_sys.setImageResource(R.drawable.saoyisao);
            sy_tvsys.setTextColor(Color.WHITE);
            sy_tvxx.setTextColor(Color.WHITE);
            sy_title.setBackgroundColor(Color.argb((int) alpha, 0xfd, 0xfd, 0xfd));

        }else {
            sy_edsou.setBackground(getResources().getDrawable(R.drawable.ed_bg2));
            sy_xx.setImageResource(R.drawable.xx2);
            sy_sys.setImageResource(R.drawable.saoyisao2);
            sy_title.setBackgroundColor(Color.WHITE);
            sy_tvsys.setTextColor(Color.BLACK);
            sy_tvxx.setTextColor(Color.BLACK);
        }

    }

}

package MyFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.liu.asus.dianshang.R;
import com.liu.asus.dianshang.Sousuo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Adapter.Mygridadpter;
import Adapter.Mylistadpter;
import Adapter.Myxyipager;
import bean.Shangpin;
import model.GridSpacingItemDecoration;
import okhttp3.Call;
import okhttp3.Response;
import utils.Okhttputils;

/**
 * Created by asus on 2017/9/28.
 */

public class frag_xyi extends Fragment {

    private View view;
    private ViewPager xyi_vp;
    private List<Shangpin>  recyclebeanList;
    private TextView xyi_price;
    private TextView xyi_title;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.frag_xyi, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        xyi_vp = view.findViewById(R.id.xyi_vp);
        xyi_price = view.findViewById(R.id.xyi_price);
        xyi_title = view.findViewById(R.id.xyi_titile);
        int pid = getActivity().getIntent().getIntExtra("pid", -1);
        getdata(pid);
        System.out.println("pid======"+pid);

    }
    private void getdata(int pid) {
        Okhttputils.requer("http://120.27.23.105/product/getProductDetail?pid="+pid, new Okhttputils.Backquer() {
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
                    recyclebeanList = new ArrayList<>();
                    JSONObject json = new JSONObject(response.body().string());
                    JSONObject l = json.getJSONObject("data");
                        Shangpin recyclebean = new Shangpin();
                        recyclebean.imgurl = l.optString("images");
                        recyclebean.zi = l.optString("title");
                        recyclebean.price = l.optInt("price");
                        recyclebean.pscid = l.optInt("pscid");
                        recyclebean.sellerid = l.optInt("sellerid");
                        recyclebean.salenum= l.optInt("salenum");
                        recyclebean.url = l.optString("detailUrl");
                        recyclebeanList.add(recyclebean);
                    if(recyclebeanList!=null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                List<String> imglist=new ArrayList<String>();
                                String imgurl = recyclebeanList.get(0).imgurl;
                                String[] split = imgurl.split("\\|");
                                for (int i = 0; i <split.length ; i++) {
                                    imglist.add(split[i]);
                                }
                                Toast.makeText(getActivity(), "搜索成功", Toast.LENGTH_SHORT).show();
                                Myxyipager myxyipager=new Myxyipager(getActivity(),imglist);
                                xyi_vp.setAdapter(myxyipager);
                                xyi_title.setText(recyclebeanList.get(0).zi);
                                xyi_price.setText("￥"+recyclebeanList.get(0).price);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

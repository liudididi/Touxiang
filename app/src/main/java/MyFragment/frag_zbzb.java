package MyFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.liu.asus.dianshang.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Adapter.Myflcycle;
import bean.Fenleibean;
import okhttp3.Call;
import okhttp3.Response;
import utils.Okhttputils;

/**
 * Created by asus on 2017/9/28.
 */

public class frag_zbzb extends Fragment {

    private View view;
    private ImageView img_fltitle;
    private TextView recy_tv1;
    private TextView recy_tv2;
    private TextView recy_tv3;
    private TextView recy_tv4;
    private RecyclerView recy1;
    private RecyclerView recy2;
    private RecyclerView recy3;
    private RecyclerView recy4;
    private List<Fenleibean> list1;
    private List<Fenleibean> list2;
    private List<Fenleibean> list3;
    private List<Fenleibean> list4;
    private ImageView img_jiazai;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.frag_jdcs, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
         initview();
          initdata();

    }

    private void initdata() {
        Okhttputils.requer("http://120.27.23.105/product/getProductCatagory?cid=11", new Okhttputils.Backquer() {
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

                    JSONObject json=new JSONObject(response.body().string());
                    JSONArray data = json.getJSONArray("data");
                    JSONObject l= (JSONObject) data.get(0);
                    JSONArray listresult = l.getJSONArray("list");
                    for (int i = 0; i<listresult.length(); i++) {
                        JSONObject l1= (JSONObject) listresult.get(i);
                        Fenleibean fenleibean=new Fenleibean();
                        fenleibean.name=l1.optString("name");
                        fenleibean.imgurl=l1.optString("icon");
                        fenleibean.xname=l.getString("name");
                        list1.add(fenleibean);
                    }
                    JSONObject result2= (JSONObject) data.get(1);
                    JSONArray list2result = result2.getJSONArray("list");
                    for (int i = 0; i<list2result.length(); i++) {
                        JSONObject l1= (JSONObject)list2result.get(i);
                        Fenleibean fenleibean=new Fenleibean();
                        fenleibean.name=l1.optString("name");
                        fenleibean.imgurl=l1.optString("icon");
                        fenleibean.xname=result2.getString("name");
                        list2.add(fenleibean);
                    }
                    JSONObject result3= (JSONObject) data.get(2);
                    JSONArray list3result = result3.getJSONArray("list");
                    for (int i = 0; i<list3result.length(); i++) {
                        JSONObject l1= (JSONObject)list3result.get(i);
                        Fenleibean fenleibean=new Fenleibean();
                        fenleibean.name=l1.optString("name");
                        fenleibean.imgurl=l1.optString("icon");
                        fenleibean.xname=result3.getString("name");
                        list3.add(fenleibean);
                    }
                    JSONObject result4= (JSONObject) data.get(3);
                    JSONArray list4result = result4.getJSONArray("list");
                    for (int i = 0; i<list4result.length(); i++) {
                        JSONObject l1= (JSONObject)list4result.get(i);
                        Fenleibean fenleibean=new Fenleibean();
                        fenleibean.name=l1.optString("name");
                        fenleibean.imgurl=l1.optString("icon");
                        fenleibean.xname=result4.getString("name");
                        list4.add(fenleibean);
                    }
                    if(list1!=null&&list2!=null&&list3!=null&&list4!=null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                img_jiazai.setVisibility(View.GONE);
                                Myflcycle my=new Myflcycle(getActivity(),list1);
                                recy_tv1.setText(list1.get(0).xname);
                                recy1.setLayoutManager(new GridLayoutManager(getActivity(),3));
                                recy1.setAdapter(my);
                                Myflcycle my2=new Myflcycle(getActivity(),list2);
                                recy_tv2.setText(list2.get(0).xname);
                                recy2.setLayoutManager(new GridLayoutManager(getActivity(),3));
                                recy2.setAdapter(my2);
                                Myflcycle my3=new Myflcycle(getActivity(),list3);
                                recy_tv3.setText(list3.get(0).xname);
                                recy3.setLayoutManager(new GridLayoutManager(getActivity(),3));
                                recy3.setAdapter(my3);
                                Myflcycle my4=new Myflcycle(getActivity(),list4);
                                recy_tv4.setText(list4.get(0).xname);
                                recy4.setLayoutManager(new GridLayoutManager(getActivity(),3));
                                recy4.setAdapter(my4);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void initview() {
        img_fltitle = view.findViewById(R.id.img_fltitle);
        img_jiazai = view.findViewById(R.id.img_jiazai);
        ImageLoader.getInstance().displayImage("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1513020434,22054256&fm=27&gp=0.jpg",img_fltitle);
        recy_tv1 = view.findViewById(R.id.recy_tv1);
        recy_tv2 = view.findViewById(R.id.recy_tv2);
        recy_tv3 = view.findViewById(R.id.recy_tv3);
        recy_tv4 = view.findViewById(R.id.recy_tv4);
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        list3 = new ArrayList<>();
        list4 = new ArrayList<>();
        recy1 = view.findViewById(R.id.recy1);
        recy2 = view.findViewById(R.id.recy2);
        recy3 = view.findViewById(R.id.recy3);
        recy4 = view.findViewById(R.id.recy4);
    }
}

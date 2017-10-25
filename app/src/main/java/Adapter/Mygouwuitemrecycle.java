package Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Size;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.liu.asus.dianshang.MainActivity;
import com.liu.asus.dianshang.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import MyFragment.frag_gouwu;
import bean.Shangpin;
import model.AmountView;
import okhttp3.Call;
import okhttp3.Response;
import utils.Okhttputils;

/**
 * Created by asus on 2017/10/18.
 */

public class Mygouwuitemrecycle extends RecyclerView.Adapter{
    private Context context;
    private List<Shangpin> shangpins;
    private SharedPreferences sp;
    private  int size;
    private  int lin;
    private  int uid;
    private  OnSjitem onsjitem;
    private CheckBox Sjck;
    private  int num;
    private  Onitemclonglik onitemclonglik;
    private Mygwrecycle.Onitemclonglikx itemclonglikx;

    public void setOnitemclonglik(Onitemclonglik onitemclonglik) {
        this.onitemclonglik = onitemclonglik;
    }

    public void setOnsjitem(OnSjitem onsjitem) {
        this.onsjitem = onsjitem;
    }

    public Mygouwuitemrecycle(Context context, List<Shangpin> shangpins,CheckBox checkBox) {
        this.context = context;
        this.shangpins = shangpins;
        sp=context.getSharedPreferences("jdsp",Context.MODE_PRIVATE);
        uid=sp.getInt("uid",-1);
        Sjck=checkBox;
        size=shangpins.size();
        lin=0;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.frag_gwitem, null);
        MyViewholder myViewholder=new MyViewholder(view);
        myViewholder.rootView=view;
        myViewholder.first=false;
        myViewholder.gw_itemck=view.findViewById(R.id.gw_itemck);
        myViewholder.mAmountView = view.findViewById(R.id.amount_view);
        myViewholder.mAmountView.setGoods_storage(200);
        myViewholder.img_tu=view.findViewById(R.id.img_tu);
        myViewholder.tv_title=view.findViewById(R.id.tv_zi);
        myViewholder.tv_price=view.findViewById(R.id.tv_price);
        return  myViewholder;
    }

    private void gengxin(final int uid, int sellerid, int pid, int selected, int num) {
     Okhttputils.requer("http://120.27.23.105/product/updateCarts?uid="+uid+"&&sellerid="+sellerid+"&&pid="+pid+"&&selected="+selected+"&&num="+num, new Okhttputils.Backquer() {
         @Override
         public void onfailure(Call call, IOException e) {

         }
         @Override
         public void onresponse(Call call, Response response) {
             ((Activity)context).runOnUiThread(new Runnable() {
                 @Override
                 public void run() {
                     Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                     Okhttputils.requer("http://120.27.23.105/product/getCarts?uid=" + uid, new Okhttputils.Backquer() {
                         @Override
                         public void onfailure(Call call, IOException e) {

                         }

                         @Override
                         public void onresponse(Call call, Response response) {
                             try {
                                 JSONObject   json = new JSONObject(response.body().string());
                                 JSONArray data = json.getJSONArray("data");
                                 final List<Shangpin> zonglist=new ArrayList<Shangpin>();
                                 for (int i = 0; i <data.length() ; i++) {
                                     JSONObject la= (JSONObject) data.get(i);
                                     JSONArray shang = la.getJSONArray("list");
                                     for (int j = 0; j < shang.length(); j++) {
                                         JSONObject l = (JSONObject) shang.get(j);
                                         Shangpin recyclebean = new Shangpin();
                                         recyclebean.imgurl = l.optString("images");
                                         recyclebean.zi = l.optString("title");
                                         recyclebean.price = l.optInt("price");
                                         recyclebean.pid = l.optInt("pid");
                                         recyclebean.pscid = l.optInt("pscid");
                                         recyclebean.sellerid=l.optInt("sellerid");
                                         recyclebean.selected=l.optInt("selected");
                                         recyclebean.num= l.optInt("num");
                                         recyclebean.bargainPrice= l.optDouble("bargainPrice");
                                         recyclebean.url = l.optString("detailUrl");
                                         if(recyclebean.selected==1){
                                             zonglist.add(recyclebean);
                                         }
                                     }
                                 }
                                 if(zonglist!=null){
                                     ((Activity)context).runOnUiThread(new Runnable() {
                                         @Override
                                         public void run() {
                                            double zongjia=0;
                                             for (int i = 0; i <zonglist.size() ; i++) {
                                                 zongjia+=(zonglist.get(i).bargainPrice*zonglist.get(i).num);
                                             }
                                             frag_gouwu.setzongjia(zongjia);
                                         }
                                     });
                                 }
                             }  catch (Exception e) {
                                 e.printStackTrace();
                             }
                         }
                     });
                 }
             });
         }
         });
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyViewholder myViewholder= (MyViewholder) holder;
        myViewholder.rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onitemclonglik.Onitmlongoclik(uid,shangpins.get(position).pid,position);
                return  true;
            }
        });
        myViewholder.mAmountView.settext(shangpins.get(position).num);
        myViewholder.gw_itemck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myViewholder.gw_itemck.isChecked()){
                    myViewholder.first=true;
                    lin=lin+1;
                    if(lin>size){
                        lin=size;
                    }
                    System.out.println("lin====" + lin);
                    System.out.println("size===" + size);
                    shangpins.get(position).selected=1;
                    if(lin==size){
                        Sjck.setChecked(true);
                        onsjitem.Onitmcheckclik();
                    }
                    gengxin(uid,shangpins.get(position).sellerid,shangpins.get(position).pid,1,myViewholder.num);
                }else {
                    myViewholder.first=false;
                    lin=lin-1;
                    if(lin<=0){
                        lin=0;
                    }
                    onsjitem.Onitmoncheckclik();
                    shangpins.get(position).selected=0;
                    frag_gouwu.setckquanxuan(false);
                    Sjck.setChecked(false);
                    gengxin(uid,shangpins.get(position).sellerid,shangpins.get(position).pid,0,myViewholder.num);
                }
            }
        });
        myViewholder.mAmountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                if(myViewholder.first==false){
                    myViewholder.gw_itemck.setChecked(true);
                    lin=lin+1;
                    if(lin>size){
                        lin=size;
                    }
                    if(lin==size){
                        Sjck.setChecked(true);
                    }
                    myViewholder.first=true;
                    myViewholder.gw_itemck.setChecked(true);
                    myViewholder.num=amount;
                    gengxin(uid,shangpins.get(position).sellerid,shangpins.get(position).pid,1,amount);
                    shangpins.get(position).selected=1;
                    return;
                }else {
                    myViewholder.gw_itemck.setChecked(true);
                    myViewholder.num=amount;
                    gengxin(uid,shangpins.get(position).sellerid,shangpins.get(position).pid,1,amount);
                    shangpins.get(position).selected=1;

                }




            }
        });
        if(shangpins.get(position).selected==1){
            myViewholder.gw_itemck.setChecked(true);
        }else {
            myViewholder.gw_itemck.setChecked(false);
        }
        myViewholder.tv_price.setText("￥"+shangpins.get(position).price);
        myViewholder.tv_title.setText("￥"+shangpins.get(position).zi);
        myViewholder.num=shangpins.get(position).num;
        myViewholder.zong=myViewholder.num*shangpins.get(position).bargainPrice;
        String imgurl = shangpins.get(position).imgurl;
        String[] split = imgurl.split("\\|");
        ImageLoader.getInstance().displayImage(split[0],myViewholder.img_tu);
    }


    @Override
    public int getItemCount() {
        return shangpins.size();
    }
    public class MyViewholder extends RecyclerView.ViewHolder {
        ImageView img_tu;
        TextView tv_title;
        TextView  tv_price;
        AmountView mAmountView;
        CheckBox gw_itemck;
        boolean  first;
        int num;
        double zong;
        View rootView;
        public MyViewholder (View itemView) {
            super(itemView);
        }
    }
    public interface  OnSjitem{
        void Onitmcheckclik();
        void Onitmoncheckclik();
    }

    public interface  Onitemclonglik{
        void Onitmlongoclik(int uid,int pid,int posttion);
    }
    
}

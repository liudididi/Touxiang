package Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;


import com.liu.asus.dianshang.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import MyFragment.frag_gouwu;
import bean.Shangpin;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import utils.Okhttputils;

/**
 * Created by asus on 2017/10/18.
 */

public class Mygwrecycle extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<JSONObject> objects;
    private  List<CheckBox> checkBoxes;
    private SharedPreferences sp;
    private  int uid;
    private  List<Boolean> listflag;
    private  Onitemclonglikx onitemclonglikx;

    public Mygwrecycle(Context context, List<JSONObject> objects, List<CheckBox> checkBoxes) {
        this.context = context;
        this.objects = objects;
        this.checkBoxes=checkBoxes;
        sp=context.getSharedPreferences("jdsp",Context.MODE_PRIVATE);
        uid=sp.getInt("uid",-1);
        listflag=new ArrayList<>();
        for (int i = 0; i <objects.size(); i++) {
            listflag.add(false);
        }
    }
    public  List<CheckBox> getcheclist(){
        return  checkBoxes;
    }
    public  List<Boolean>  getlistflag(){return  listflag;}

    public void setOnitemclonglikx(Onitemclonglikx onitemclonglikx) {
        this.onitemclonglikx = onitemclonglikx;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.gw_item, null);
        Viewholder viewholder=new Viewholder(view);
        viewholder.gw_itemrecycle=view.findViewById(R.id.gw_itemrecycle);
        viewholder.tv_shangjia= view.findViewById(R.id.tv_shangjia);
        viewholder.ck_shangjia= view.findViewById(R.id.ck_shangjia);
        viewholder.Sjsleected=false;
        checkBoxes.add(viewholder.ck_shangjia);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Viewholder viewholder= (Viewholder) holder;
        viewholder.gw_itemrecycle.setLayoutManager(new LinearLayoutManager(context));
          try {
            JSONObject jsonObject = objects.get(position);
            String sellerName= jsonObject.optString("sellerName");
            viewholder.tv_shangjia.setText(sellerName);
            JSONArray shang = jsonObject.getJSONArray("list");
            final List<Shangpin> shangpins=new ArrayList<>();
            for (int i = 0; i < shang.length(); i++) {
                JSONObject l = (JSONObject) shang.get(i);
                Shangpin recyclebean = new Shangpin();
                recyclebean.imgurl = l.optString("images");
                recyclebean.zi = l.optString("title");
                recyclebean.price = l.optInt("price");
                recyclebean.pid = l.optInt("pid");
                recyclebean.pscid = l.optInt("pscid");
                recyclebean.sellerid=l.optInt("sellerid");
                recyclebean.selected=l.optInt("selected");
                recyclebean.bargainPrice= l.optDouble("bargainPrice");
                recyclebean.num= l.optInt("num");
                recyclebean.url = l.optString("detailUrl");
                shangpins.add(recyclebean);
            }
            final Mygouwuitemrecycle mygouwuitemrecycle=new Mygouwuitemrecycle(context,shangpins,viewholder.ck_shangjia);
                 viewholder.gw_itemrecycle.setAdapter(mygouwuitemrecycle);
                 bianli();
              mygouwuitemrecycle. setOnitemclonglik(new Mygouwuitemrecycle.Onitemclonglik() {
                  @Override
                  public void Onitmlongoclik(final int uid, final int pid, final int posttion) {
                      AlertDialog.Builder a=new AlertDialog.Builder(context);
                      a.setMessage("确定要删除该条目么");
                      a.setPositiveButton("确定",new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialogInterface, int i) {

                              Okhttputils.requer("http://120.27.23.105/product/deleteCart?uid="+uid+"&pid="+pid, new Okhttputils.Backquer() {
                                  @Override
                                  public void onfailure(Call call, IOException e) {

                                  }

                                  @Override
                                  public void onresponse(Call call, Response response) {
                                      try {
                                          String  string = response.body().string();
                                          JSONObject json=new JSONObject(string );
                                          final String msg = json.getString("msg");
                                          ((Activity)context).runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {
                                                  Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                                                  shangpins.remove(posttion);
                                                  if(shangpins.size()==0){
                                                      objects.remove(position);
                                                      if(objects.size()==0){
                                                          frag_gouwu.setjiesuanhind();
                                                      }
                                                  }

                                                  notifyDataSetChanged();

                                                  //onitemclonglikx.Onitmlongoclikx();
                                              }
                                          });
                                      } catch (Exception e) {
                                          e.printStackTrace();
                                      }

                                  }
                              });
                          }
                      });
                      a.setNegativeButton("留下", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialogInterface, int i) {

                          }
                      });
                      a.create().show();

                  }
              });
                 mygouwuitemrecycle.setOnsjitem(new Mygouwuitemrecycle.OnSjitem() {
                @Override
                public void Onitmcheckclik() {
                    viewholder.Sjsleected=true;
                    listflag.set(position,true);
                    if(viewholder.Sjsleected==true){
                        viewholder.ck_shangjia.setChecked(true);
                    }else {
                        viewholder.ck_shangjia.setChecked(false);
                    }
                    bianli();
                    viewholder.gw_itemrecycle.setAdapter(mygouwuitemrecycle);
                }
                @Override
                public void Onitmoncheckclik() {
                    viewholder.Sjsleected=false;
                    listflag.set(position,false);
                    if(viewholder.Sjsleected==true){
                        viewholder.ck_shangjia.setChecked(true);
                    }else {
                        viewholder.ck_shangjia.setChecked(false);
                    }
                    bianli();
                     viewholder.gw_itemrecycle.setAdapter(mygouwuitemrecycle);
                     }
                 });
              viewholder.ck_shangjia.setChecked(listflag.get(position));
              if(viewholder.ck_shangjia.isChecked()){
                  for (int i = 0; i <shangpins.size() ; i++) {
                      gengxin(uid,shangpins.get(i).sellerid,shangpins.get(i).pid,1,shangpins.get(i).num);
                      shangpins.get(i).selected=1;
                  }
                  viewholder.gw_itemrecycle.setAdapter(mygouwuitemrecycle);
              }else {
                  for (int i = 0; i <shangpins.size() ; i++) {
                      gengxin(uid,shangpins.get(i).sellerid,shangpins.get(i).pid,0,shangpins.get(i).num);
                      shangpins.get(i).selected=0;
                  }
                  viewholder.gw_itemrecycle.setAdapter(mygouwuitemrecycle);
              }
             viewholder.ck_shangjia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(viewholder.ck_shangjia.isChecked()){
                        listflag.set(position,true);
                        for (int i = 0; i <shangpins.size() ; i++) {
                            gengxin(uid,shangpins.get(i).sellerid,shangpins.get(i).pid,1,shangpins.get(i).num);
                            shangpins.get(i).selected=1;
                        }
                        viewholder.ck_shangjia.setChecked(true);
                        viewholder.gw_itemrecycle.setAdapter(mygouwuitemrecycle);
                    }else {
                        listflag.set(position,false);
                        for (int i = 0; i <shangpins.size() ; i++) {
                            gengxin(uid,shangpins.get(i).sellerid,shangpins.get(i).pid,0,shangpins.get(i).num);
                            shangpins.get(i).selected=0;
                        }
                        viewholder.ck_shangjia.setChecked(false);
                        viewholder.gw_itemrecycle.setAdapter(mygouwuitemrecycle);
                    }
                    bianli();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void bianli() {
        List<Boolean> getlistflag = getlistflag();
        int z=0;
        for (int i = 0; i <getlistflag.size() ; i++) {
            if(getlistflag.get(i)==true){
                z++;
            }else {
                z--;
            }
        }
        if(z==getlistflag.size()){
            frag_gouwu.setckquanxuan(true);
        }else {
            frag_gouwu.setckquanxuan(false);
        }
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
    public int getItemCount() {
        return objects.size();
    }
    public class Viewholder extends RecyclerView.ViewHolder {
        private  RecyclerView gw_itemrecycle;
        private TextView tv_shangjia;
        private CheckBox ck_shangjia;
        private  Boolean  Sjsleected;
        public Viewholder(View itemView) {
            super(itemView);
        }
    }
    public interface  Onitemclonglikx{
        void Onitmlongoclikx();
    }

}

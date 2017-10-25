package Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.liu.asus.dianshang.Dingdan;
import com.liu.asus.dianshang.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import bean.Dingdanbean;
import bean.Fenleibean;

/**
 * Created by asus on 2017/9/29.
 */

public class Myddcycle extends RecyclerView.Adapter<Myddcycle.viewholder> {
    private Context context;
    private List<Dingdanbean> recyclebeen;
    private  Onddrecyclik onddrecyclik;

    public void setOnddrecyclik(Onddrecyclik onrecyclik) {
        this.onddrecyclik = onrecyclik;
    }

    public Myddcycle(Context context, List<Dingdanbean> recyclebeen) {
        this.context = context;
        this.recyclebeen = recyclebeen;
    }

    @Override
    public viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.dinigdan_a, null);
        viewholder viewholder=new viewholder(view);
        viewholder.tv_data= view.findViewById(R.id.tv_data);
        viewholder.tv_price= view.findViewById(R.id.tv_price);
        viewholder.tv_staus= view.findViewById(R.id.tv_staus);
        viewholder.bt_zhifu=view.findViewById(R.id.bt_zhifu);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(final viewholder holder, final int position) {
        holder.tv_data.setText(recyclebeen.get(position).createtime);
        holder.tv_price.setText(recyclebeen.get(position).price+"");
        holder.bt_zhifu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onddrecyclik.MyddreclOnk(holder.bt_zhifu,position);
            }
        });
        int status = recyclebeen.get(position).status;
        switch (status){
            case 0:
                holder.tv_staus.setText("待支付");
                break;
            case 1:
                holder.tv_staus.setText("已支付");
                break;
            case 2:
                holder.tv_staus.setText("已取消");
                break;
        }
    }
    @Override
    public int getItemCount() {
        return recyclebeen.size();
    }
    public class viewholder extends RecyclerView.ViewHolder {
        TextView  tv_staus;
        TextView  tv_price;
        TextView  tv_data;
        Button bt_zhifu;
       public viewholder(View itemView) {
           super(itemView);
       }
   }
   public  interface Onddrecyclik{
         void  MyddreclOnk(Button img, int postion);
    }
}

package Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.liu.asus.dianshang.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import bean.Shangpin;


/**
 * Created by asus on 2017/10/14.
 */

public class Mygridadpter extends RecyclerView.Adapter<Mygridadpter.viewholder> {
    private Context context;
    private List<Shangpin> list;
    private  Ongridclik onrecyclik;

    public void setOngridclik(Ongridclik onrecyclik) {
        this.onrecyclik = onrecyclik;
    }

    public Mygridadpter(Context context, List<Shangpin> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.grid_a, null);
        viewholder viewholder=new viewholder(view);
        viewholder.rootView=view;
        viewholder.img_tu= view.findViewById(R.id.img_tu);
        viewholder.tv_title= view.findViewById(R.id.tv_title);
        viewholder.tv_price= view.findViewById(R.id.tv_price);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(final viewholder holder, final int position) {
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onrecyclik.MyreclOnk(holder.rootView,position);
            }
        });
        String imgurl = list.get(position).imgurl;
        String[] split = imgurl.split("\\|");
        ImageLoader.getInstance().displayImage(split[0],holder.img_tu);
        holder.tv_title.setText(list.get(position).zi);
        holder.tv_price.setText("￥"+list.get(position).price);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        View rootView;
        ImageView img_tu;
        TextView tv_title;
        TextView  tv_price;
        public viewholder(View itemView) {
            super(itemView);
        }
    }
    public  interface Ongridclik{
        void  MyreclOnk(View img, int postion);
    }
}

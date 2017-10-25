package Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.liu.asus.dianshang.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import bean.Recyclebean;

/**
 * Created by asus on 2017/9/29.
 */

public class Myrecycle extends RecyclerView.Adapter<Myrecycle.viewholder> {
    private Context context;
    private List<Recyclebean> recyclebeen;
    private  Onrecyclik onrecyclik;

    public void setOnrecyclik(Onrecyclik onrecyclik) {
        this.onrecyclik = onrecyclik;
    }

    public Myrecycle(Context context, List<Recyclebean> recyclebeen) {
        this.context = context;
        this.recyclebeen = recyclebeen;
    }

    @Override
    public viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.frag_pageitem, null);
        viewholder viewholder=new viewholder(view);
        viewholder.img_tu= view.findViewById(R.id.img_tu);
        viewholder.tv_zi= view.findViewById(R.id.tv_zi);
        viewholder.tv_price= view.findViewById(R.id.tv_price);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(final viewholder holder, final int position) {

        String imgurl = recyclebeen.get(position).imgurl;
        String[] split = imgurl.split("\\|");
        ImageLoader.getInstance().displayImage(split[0],holder.img_tu);
        holder.img_tu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onrecyclik.MyreclOnk(holder.img_tu,position);
            }
        });
        holder.tv_zi.setText(recyclebeen.get(position).zi);
        holder.tv_price.setText("￥"+recyclebeen.get(position).price);
    }
    @Override
    public int getItemCount() {
        return recyclebeen.size();
    }
    public class viewholder extends RecyclerView.ViewHolder {
        ImageView img_tu;
        TextView  tv_zi;
        TextView  tv_price;
       public viewholder(View itemView) {
           super(itemView);
       }
   }
   public  interface Onrecyclik{
         void  MyreclOnk(ImageView img,int postion);
    }
}

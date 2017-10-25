package Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.liu.asus.dianshang.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import bean.Recyclebean;

/**
 * Created by asus on 2017/9/29.
 */

public class Msrecycle extends RecyclerView.Adapter<Msrecycle.viewholder> {
    private Context context;
    private List<Recyclebean> recyclebeen;
    private  MsOnrecyclik onrecyclik;

    public void setOnrecyclik(MsOnrecyclik onrecyclik) {
        this.onrecyclik = onrecyclik;
    }

    public Msrecycle(Context context, List<Recyclebean> recyclebeen) {
        this.context = context;
        this.recyclebeen = recyclebeen;
    }

    @Override
    public viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.frag_msitem, null);
        viewholder viewholder=new viewholder(view);
        viewholder.img_tu= view.findViewById(R.id.img_tu);
        viewholder.tv_price= view.findViewById(R.id.tv_price);
        viewholder.old_price= view.findViewById(R.id.old_price);
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
        holder.tv_price.setText("￥"+recyclebeen.get(position).price);
        holder.old_price.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
        holder.old_price.setText("￥"+recyclebeen.get(position).oldprice);
    }
    @Override
    public int getItemCount() {
        return recyclebeen.size();
    }
    public class viewholder extends RecyclerView.ViewHolder {
        ImageView img_tu;
        TextView  tv_price;
        TextView  old_price;
       public viewholder(View itemView) {
           super(itemView);
       }
   }
   public  interface MsOnrecyclik{
         void  MyreclOnk(ImageView img, int postion);
    }
}

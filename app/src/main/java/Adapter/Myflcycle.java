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

import bean.Fenleibean;
import bean.Recyclebean;

/**
 * Created by asus on 2017/9/29.
 */

public class Myflcycle extends RecyclerView.Adapter<Myflcycle.viewholder> {
    private Context context;
    private List<Fenleibean> recyclebeen;
    private  Onflrecyclik onrecyclik;

    public void setOnrecyclik(Onflrecyclik onrecyclik) {
        this.onrecyclik = onrecyclik;
    }

    public Myflcycle(Context context, List<Fenleibean> recyclebeen) {
        this.context = context;
        this.recyclebeen = recyclebeen;
    }

    @Override
    public viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.frag_flitem, null);
        viewholder viewholder=new viewholder(view);
        viewholder.img_tu= view.findViewById(R.id.img_tu);
        viewholder.tv_name= view.findViewById(R.id.tv_name);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(final viewholder holder, final int position) {
        ImageLoader.getInstance().displayImage(recyclebeen.get(position).imgurl,holder.img_tu);
        holder.img_tu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onrecyclik.MyreclOnk(holder.img_tu,position);
            }
        });
        holder.tv_name.setText(recyclebeen.get(position).name);
    }
    @Override
    public int getItemCount() {
        return recyclebeen.size();
    }
    public class viewholder extends RecyclerView.ViewHolder {
        ImageView img_tu;
        TextView  tv_name;
       public viewholder(View itemView) {
           super(itemView);
       }
   }
   public  interface Onflrecyclik{
         void  MyreclOnk(ImageView img, int postion);
    }
}

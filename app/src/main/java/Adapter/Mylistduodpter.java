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

public class Mylistduodpter extends RecyclerView.Adapter {
    private Context context;
    private List<Shangpin> list;
    private  Onrecyclik onrecyclik;

    public void setOnrecyclik(Onrecyclik onrecyclik) {
        this.onrecyclik = onrecyclik;
    }

    public Mylistduodpter(Context context, List<Shangpin> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder holder = null;
        switch (viewType){
            case 0:
                view = View.inflate(context, R.layout.list_a, null);
                holder = new ViewHolderOne(view);
                final ViewHolderOne holderOne = (ViewHolderOne) holder;
                holderOne.img_tu = view.findViewById(R.id.img_tu);
                holderOne.tv_title= view.findViewById(R.id.tv_title);
                holderOne.tv_price= view.findViewById(R.id.tv_price);
                break;
            case 1:
                view = View.inflate(context, R.layout.list_b, null);
                holder = new ViewHolderTwo(view);
                final ViewHolderTwo holderTwo = (ViewHolderTwo) holder;
                holderTwo.img_tu = view.findViewById(R.id.img_tu);
                holderTwo.tv_title= view.findViewById(R.id.tv_title);
                holderTwo.tv_price= view.findViewById(R.id.tv_price);
                break;
        }
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        if (position%2==0){
            return 0;
        } else {
            return 1;
        }
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case 0:
                final ViewHolderOne holderOne = (ViewHolderOne) holder;
                String imgurl = list.get(position).imgurl;
                String[] split = imgurl.split("\\|");
                ImageLoader.getInstance().displayImage(split[0],holderOne .img_tu);
                holderOne .tv_title.setText(list.get(position).zi);
                holderOne .tv_price.setText("￥"+list.get(position).price);
                break;
            case 1:
                ViewHolderTwo holderTwo = (ViewHolderTwo) holder;
                String imgurls = list.get(position).imgurl;
                String[] splits = imgurls.split("\\|");
                ImageLoader.getInstance().displayImage(splits[0],holderTwo .img_tu);
                holderTwo .tv_title.setText(list.get(position).zi);
                holderTwo .tv_price.setText("￥"+list.get(position).price);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ViewHolderOne extends RecyclerView.ViewHolder {
       public  ImageView img_tu;
        public  TextView tv_title;
        public   TextView  tv_price;
        public ViewHolderOne(View itemView) {
            super(itemView);
        }
    }
    public class ViewHolderTwo extends RecyclerView.ViewHolder {
        ImageView img_tu;
        TextView tv_title;
        TextView  tv_price;
        public ViewHolderTwo(View itemView) {
            super(itemView);
        }
    }
    public  interface Onrecyclik{
        void  MyreclOnk(ImageView img, int postion);
    }
}

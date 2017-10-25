package MyFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.liu.asus.dianshang.MainActivity;
import com.liu.asus.dianshang.MemessageActivity;
import com.liu.asus.dianshang.R;
import com.meg7.widget.CustomShapeImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import model.CircleRoundImageView;
import okhttp3.Call;
import okhttp3.Response;
import utils.Okhttputils;

import static com.liu.asus.dianshang.R.id.img_icon;

/**
 * Created by asus on 2017/9/28.
 */

public class frag_wode extends Fragment {

    private View view;
    private TextView wd_dl;
    private CustomShapeImageView img_denglu;
    private SharedPreferences sp;
    private int uid;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.myitem, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initview();
        wd_dl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean memessage = sp.getBoolean("memessage", false);
                if(memessage==true){
                    Intent intent=new Intent(getActivity(), MemessageActivity.class);
                    intent.putExtra("uid",uid);
                    getActivity().startActivity(intent);
                }else {
                    Intent intent=new Intent(getActivity(), MainActivity.class);
                    getActivity().startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        uid = sp.getInt("uid",-1);
        if(uid !=-1) {
            Okhttputils.requer("http://120.27.23.105/user/getUserInfo?uid=" + uid, new Okhttputils.Backquer() {
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
                        JSONObject json = new JSONObject(response.body().string());
                        JSONObject data = json.getJSONObject("data");
                        final String icon = data.optString("icon");
                        if (icon != null && icon.length() > 8) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    sp.edit().putString("icon", icon).commit();
                                    ImageLoader.getInstance().clearDiskCache();
                                    ImageLoader.getInstance().clearMemoryCache();
                                    ImageLoader.getInstance().displayImage(icon, img_denglu);
                                }
                            });
                        }else {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    img_denglu.setImageResource(R.drawable.icon);
                                }
                            });
                        }
                        final String nickname = data.optString("nickname");
                        if(nickname.equals("null")){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String name = sp.getString("name", null);
                                    wd_dl.setText(name);
                                }
                            });
                        }else {
                            sp.edit().putString("name",nickname).commit();
                            wd_dl.setText(nickname);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }else {
            img_denglu.setImageResource(R.drawable.icon);
            wd_dl.setText("登录/注册");
        }
    }
    private void initview() {
        wd_dl = view.findViewById(R.id.wd_dl);
        img_denglu = view.findViewById(R.id.img_denglu);
        img_denglu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean memessage = sp.getBoolean("memessage", false);
                if(memessage==true){
                    Intent intent=new Intent(getActivity(), MemessageActivity.class);
                    intent.putExtra("uid",uid);
                    getActivity().startActivity(intent);
                }else {
                    Intent intent=new Intent(getActivity(), MainActivity.class);
                    getActivity().startActivity(intent);
                }
            }
        });
        sp = getActivity().getSharedPreferences("jdsp", Context.MODE_PRIVATE);
    }
}

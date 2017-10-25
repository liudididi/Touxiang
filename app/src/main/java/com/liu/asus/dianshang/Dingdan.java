package com.liu.asus.dianshang;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import Adapter.Myddcycle;
import Adapter.Mygwrecycle;
import bean.Dingdanbean;
import model.GridSpacingItemDecoration;
import okhttp3.Call;
import okhttp3.Response;
import pay.PayResult;
import pay.SignUtils;
import utils.Okhttputils;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import static com.liu.asus.dianshang.R.id.gw_jiesuan;

public class Dingdan extends AppCompatActivity  {
    // 商户PID
    public static final String PARTNER = "2088901305856832";
    // 商户收款账号
    public static final String SELLER = "8@qdbaiu.com";
    // 商户（应用）私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAM" +
            "/KCxg/OIj6er2GEig0DOkHqBSzEPHGigMbTXP1k2nrxEHeE59xOOuy" +
            "ovQH/A1LgbuVKyOac3uAN4GXIBEoozRVzDhu5dobeNm48BPcpYSAfvN3K" +
            "/5GLacvJeENqsiBx8KufM/9inlHaDRQV7WhX1Oe2ckat1EkdHwxxQgc" +
            "36NhAgMBAAECgYEAwn3sWpq6cUR65LD8h9MIjopTImTlpFjgz72bhsHD" +
            "ZK6A+eJDXcddrwh7DI34t/0IBqu+QEoOc/f0fIEXS9hMwTvFY59XG7M8" +
            "M6SdeaAbemrGmZ1IdD6YDmpbQFHn2ishaYF0YDZIkBS3WLDFrtk/efaar" +
            "BCpGAVXeEiVQE4LewECQQD5W1rpkq+dHDRzzdtdi1bJ479wun5CfmVDV" +
            "b2CJH7Iz0t8zyp/iEVV2QELnxZMphmoSzKaLXutTTj2OImpzCtRAkEA1" +
            "VMxG6nQq9NkU51H1+I3mlUXRZ0XeFA1GFJ7xWpNRAVhEWlDz2zy9v/g" +
            "X+RFpNC3f5uznycas70Xp78ew43TEQJAZFFqi9mlqTF1sLk67bFnIyX" +
            "rGPEOZrXvC13tNfR0xVkQZ4/46wHp0xXQo9pG4GNaoyhNnVV7EkelCPn" +
            "J+HPZYQJAQh6T9QgQZoGR8hyovPAf3dUL7oa/VIo/urcuJ8VIB5JHQNdI" +
            "rk0NjaNHj1E4iNosVgATj3vWWel9IIArb99QkQJAKvfm78lwnImtg5IM6" +
            "04hdn/Wu1XF8tpxsKLWcnfchMr0bM9rCmKmhAY+wdmqSyPZRiNb1QaaaD" +
            "TqJxLy6AnQ+Q==";
    //    // 支付宝公钥
//    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCd6rV3vOE578e6V" +
//            "lGEakZpPdsX2QmGdIfi/yHe cg1CIEWzX9wn2LNFGtu1EzYQyKACG/RKeog0pUJEVGfBG30zFdNY2YocYJNdPtA" +
//            "DqhJbS0GJm7f8 1vRiLKtOwKjdiz9oMEwxhc/5fysfMbercidRmlCDPU9BNL1UPb9bAx25JwIDAQAB";
    private static final int SDK_PAY_FLAG = 1;
    //阿里云服务器，用真机测试
//    private static final String URL_JSON = "http://101.200.142.201:8080/alipayServer/AlipayDemo";
    //private static final String URL_JSON = "http://192.168.190.1:8080/alipayServer/AlipayDemo";
    //本机服务器，用真机访问不到，用模拟器能访问，但是支付宝不允许模拟器支付
//	private static final String URL_JSON = "http://169.254.63.148:8080/alipayServer/AlipayDemo";

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    System.out.println("resultInfo:"+resultInfo);

                    String resultStatus = payResult.getResultStatus();
                    System.out.println("resultStatus:"+resultStatus);
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(Dingdan.this, "支付成功", Toast.LENGTH_SHORT).show();
                        int weizhi = sp.getInt("weizhi", 0);
                        list.get(weizhi).status=1;
                        int orderid = list.get(weizhi).orderid;
                        myddcycle.notifyDataSetChanged();
                        Okhttputils.requer("http://120.27.23.105/product/updateOrder?uid="+uid+"&status=1&orderId="+orderid, new Okhttputils.Backquer() {
                            @Override
                            public void onfailure(Call call, IOException e) {

                            }

                            @Override
                            public void onresponse(Call call, Response response) {

                            }
                        });

                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(Dingdan.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(Dingdan.this, "支付失败", Toast.LENGTH_SHORT).show();
                            int weizhi = sp.getInt("weizhi", 0);
                            list.get(weizhi).status=2;
                            int orderid = list.get(weizhi).orderid;
                            myddcycle.notifyDataSetChanged();
                            Okhttputils.requer("http://120.27.23.105/product/updateOrder?uid="+uid+"&status=2&orderId="+orderid, new Okhttputils.Backquer() {
                                @Override
                                public void onfailure(Call call, IOException e) {

                                }

                                @Override
                                public void onresponse(Call call, Response response) {

                                }
                            });
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };










    private ImageView img_fanhui;
    private SharedPreferences sp;
    private XRecyclerView dingdan_recycle;
    private int page;
    private GridSpacingItemDecoration gsid;
    private List<Dingdanbean> list;
    private Myddcycle myddcycle;
    private int uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dingdan);
        sp = getSharedPreferences("jdsp", Context.MODE_PRIVATE);
        page=1;
        uid = sp.getInt("uid", -1);
        initview();
        int spanCount = 2;
        int spacing = 10 ;
        boolean includeEdge = false;
        gsid = new GridSpacingItemDecoration(spanCount, spacing, includeEdge);
    }

    private void initview() {
        img_fanhui = (ImageView) findViewById(R.id.tv_fanhui);
        img_fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(Dingdan.this,HomeActivity.class);
                sp.edit().putBoolean("gouwuche",true).commit();
                startActivity(in);
            }
        });
        uid = sp.getInt("uid", -1);
        dingdan_recycle = (XRecyclerView) findViewById(R.id.dingdan_recycle);
        dingdan_recycle.setLayoutManager(new LinearLayoutManager(this));
        dingdan_recycle.setRefreshProgressStyle(13);
        dingdan_recycle.setLaodingMoreProgressStyle(18);//加载样式
        dingdan_recycle.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        list = new ArrayList<Dingdanbean>();
                        page=1;
                        myddcycle=null;
                        getdata();
                        dingdan_recycle.refreshComplete();
                    }
                }, 2000);

            }

            @Override
            public void onLoadMore() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page++;
                        getdata();
                        dingdan_recycle.loadMoreComplete();
                    }
                }, 2000);

            }
        });
        list = new ArrayList<Dingdanbean>();
        getdata();


    }
    private void getdata() {
        Okhttputils.requer("http://120.27.23.105/product/getOrders?uid="+uid+"&&page="+page, new Okhttputils.Backquer() {
            @Override
            public void onfailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Dingdan.this, "网络较差，稍后重试", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onresponse(Call call, Response response) {
                try {
                    String string=response.body().string();
                    JSONObject json = new JSONObject(string);
                    JSONArray data = json.getJSONArray("data");
                    for (int i = 0; i <data.length() ; i++) {
                        JSONObject d= (JSONObject) data.get(i);
                        Dingdanbean dingdanbean=new Dingdanbean();
                        dingdanbean.createtime=d.optString("createtime");
                        dingdanbean.price=d.optInt("price");
                        dingdanbean.orderid=d.optInt("orderid");
                        dingdanbean.status=d.optInt("status");
                        list.add(dingdanbean);
                    }
                    if(list !=null){
                       runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(myddcycle==null){
                                    myddcycle= new Myddcycle(Dingdan.this, list);
                                    dingdan_recycle.setAdapter(myddcycle);
                                }else {
                                    myddcycle.notifyDataSetChanged();
                                }
                                myddcycle.setOnddrecyclik(new Myddcycle.Onddrecyclik() {
                                    @Override
                                    public void MyddreclOnk(Button img, int postion) {
                                        sp.edit().putInt("weizhi",postion).commit();
                                        paySignFromClient();
                                    }
                                });
                            }
                        });
                    }
                }  catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void paySignFromClient() {
        //获得订单信息
        String orderInfo = getOrderInfo("来自仿京东的测试商品", "购买一部手机", "0.01");
        //进行加密签名
        String sign = sign(orderInfo);
        //通过URLEncoder进行编码
        try {
            sign = URLEncoder.encode(sign, "utf-8");//统一编码
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //拼装最终的支付结果信息
        StringBuffer sb = new StringBuffer(orderInfo);
        sb.append("&sign=\"");
        sb.append(sign);
        sb.append("\"&");
        sb.append(getSignType());
        //获取必须来自服务端
        final String payInfo = sb.toString();//获得最终的支付信息
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(Dingdan.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);
                Log.i("TAG", "走了pay支付方法.............");

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
    private String getOrderInfo(String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }
    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }


}

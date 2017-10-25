package com.liu.asus.dianshang;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import MyFragment.frag_faxian;
import MyFragment.frag_fenlei;
import MyFragment.frag_gouwu;
import MyFragment.frag_shouye;
import MyFragment.frag_wode;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout m_shouye;
    private LinearLayout m_fenlei;
    private LinearLayout m_faxian;
    private LinearLayout m_gouwu;
    private LinearLayout m_wode;
    private MyFragment.frag_shouye frag_shouye;
    private MyFragment.frag_wode frag_wode;
    private MyFragment.frag_fenlei frag_fenlei;
    private MyFragment.frag_faxian frag_faxian;
    private MyFragment.frag_gouwu  frag_gouwu;
    private ImageView img_shouye,img_wode,img_fenlei,img_faxian,img_gouwu;
    private SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sp = getSharedPreferences("jdsp", Context.MODE_PRIVATE);
        initview();

    }
    private void initview() {
        m_shouye = (LinearLayout) findViewById(R.id.m_shouye);
        m_fenlei = (LinearLayout) findViewById(R.id.m_fenlei);
        m_faxian = (LinearLayout) findViewById(R.id.m_faxian);
        m_gouwu = (LinearLayout) findViewById(R.id.m_gouwu);
        m_wode = (LinearLayout) findViewById(R.id.m_wode);
        m_shouye.setOnClickListener(this);
        m_fenlei.setOnClickListener(this);
        m_faxian.setOnClickListener(this);
        m_gouwu.setOnClickListener(this);
        m_wode.setOnClickListener(this);
        img_faxian = (ImageView) findViewById(R.id.img_faxian);
        img_shouye = (ImageView) findViewById(R.id.img_shouye);
        img_wode = (ImageView) findViewById(R.id.img_wode);
        img_fenlei = (ImageView) findViewById(R.id.img_fenlei);
        img_gouwu = (ImageView) findViewById(R.id.img_gouwu);
        frag_shouye = new frag_shouye();
        frag_wode=new frag_wode();
        frag_faxian=new frag_faxian();
        frag_gouwu=new frag_gouwu();
        frag_fenlei=new frag_fenlei();
        if(sp.getBoolean("gouwuche",false)){
            img_shouye.setImageResource(R.drawable.shouye1);
            img_faxian.setImageResource(R.drawable.faxian1);
            img_gouwu.setImageResource(R.drawable.gouwuche2);
            img_wode.setImageResource(R.drawable.wode1);
            img_fenlei.setImageResource(R.drawable.fenlei1);
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_main,frag_gouwu).commit();
        }else {
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_main,new frag_shouye()).show(frag_shouye).commit();
              sp.edit().putBoolean("gouwuche",false).commit();
        }

}
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.m_shouye:
                img_shouye.setImageResource(R.drawable.shouye2);
                img_faxian.setImageResource(R.drawable.faxian1);
                img_gouwu.setImageResource(R.drawable.gouwuche1);
                img_wode.setImageResource(R.drawable.wode1);
                img_fenlei.setImageResource(R.drawable.fenlei1);
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_main,new frag_shouye()).show(frag_shouye).commit();
                sp.edit().putBoolean("gouwuche",false).commit();
                sp.edit().putBoolean("wode",false).commit();
                break;
            case  R.id.m_fenlei:
                img_shouye.setImageResource(R.drawable.shouye1);
                img_faxian.setImageResource(R.drawable.faxian1);
                img_gouwu.setImageResource(R.drawable.gouwuche1);
                img_wode.setImageResource(R.drawable.wode1);
                img_fenlei.setImageResource(R.drawable.fenlei2);
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_main,frag_fenlei).commit();
                sp.edit().putBoolean("gouwuche",false).commit();
                sp.edit().putBoolean("wode",false).commit();
                break;
            case R.id.m_faxian:
                img_shouye.setImageResource(R.drawable.shouye1);
                img_faxian.setImageResource(R.drawable.faxian2);
                img_gouwu.setImageResource(R.drawable.gouwuche1);
                img_wode.setImageResource(R.drawable.wode1);
                img_fenlei.setImageResource(R.drawable.fenlei1);
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_main,frag_faxian).commit();
                sp.edit().putBoolean("gouwuche",false).commit();
                sp.edit().putBoolean("wode",false).commit();
                break;
            case  R.id.m_gouwu:
                img_shouye.setImageResource(R.drawable.shouye1);
                img_faxian.setImageResource(R.drawable.faxian1);
                img_gouwu.setImageResource(R.drawable.gouwuche2);
                img_wode.setImageResource(R.drawable.wode1);
                img_fenlei.setImageResource(R.drawable.fenlei1);
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_main,frag_gouwu).commit();
                sp.edit().putBoolean("gouwuche",true).commit();
                sp.edit().putBoolean("wode",false).commit();
                break;
            case R.id.m_wode:
                img_shouye.setImageResource(R.drawable.shouye1);
                img_faxian.setImageResource(R.drawable.faxian1);
                img_gouwu.setImageResource(R.drawable.gouwuche1);
                img_wode.setImageResource(R.drawable.wode2);
                img_fenlei.setImageResource(R.drawable.fenlei1);
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_main,frag_wode).commit();
                sp.edit().putBoolean("gouwuche",false).commit();
                sp.edit().putBoolean("wode",false).commit();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(sp.getBoolean("gouwuche",false)){
            img_shouye.setImageResource(R.drawable.shouye1);
            img_faxian.setImageResource(R.drawable.faxian1);
            img_gouwu.setImageResource(R.drawable.gouwuche2);
            img_wode.setImageResource(R.drawable.wode1);
            img_fenlei.setImageResource(R.drawable.fenlei1);
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_main,frag_gouwu).commit();
            sp.edit().putBoolean("gouwuche",false).commit();
        }
        else  if(sp.getBoolean("wode",false)){
            img_shouye.setImageResource(R.drawable.shouye1);
            img_faxian.setImageResource(R.drawable.faxian1);
            img_gouwu.setImageResource(R.drawable.gouwuche1);
            img_wode.setImageResource(R.drawable.wode2);
            img_fenlei.setImageResource(R.drawable.fenlei1);
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_main,frag_wode).commit();
            sp.edit().putBoolean("wode",false).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_main,new frag_shouye()).show(frag_shouye).commit();
            sp.edit().putBoolean("gouwuche",false).commit();
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(intentResult != null) {
            if(intentResult.getContents() == null) {
                Toast.makeText(this,"内容为空",Toast.LENGTH_LONG).show();
            } else {
                // ScanResult 为 获取到的字符串
                String ScanResult = intentResult.getContents();
                Toast.makeText(this,ScanResult,Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode,resultCode,data);
        }
    }

    @Override
    public MenuInflater getMenuInflater() {
        return super.getMenuInflater();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        System.exit(0);
    }
}

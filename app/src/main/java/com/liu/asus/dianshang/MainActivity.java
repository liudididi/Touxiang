package com.liu.asus.dianshang;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import presenter.Changepresent;
import presenter.Loginpresent;
import presenter.Regpresent;

public class MainActivity extends AppCompatActivity implements Login,Reg{

    private Button bt_login;
    private Loginpresent loginpresent;
    private EditText ed_phone;
    private EditText ed_password;
    private ProgressBar pb;
    private TextView bt_zhuce;
    private Regpresent regpresent;
    private ImageView img_icon;
    private SharedPreferences sp;
    private ImageView img_chahao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initview();
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginpresent.login(ed_phone.getText().toString(),ed_password.getText().toString());
            }
        });
        bt_zhuce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regpresent.Reg(ed_phone.getText().toString(),ed_password.getText().toString());
            }
        });

    }
    private void initview() {
        sp = getSharedPreferences("jdsp", Context.MODE_PRIVATE);
        bt_login = (Button) findViewById(R.id.bt_login);
        img_chahao = (ImageView) findViewById(R.id.img_chahao);
        img_chahao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(MainActivity.this,HomeActivity.class);
                sp.edit().putBoolean("wode",true).commit();
                startActivity(in);
            }
        });
        bt_zhuce = (TextView) findViewById(R.id.bt_zhuce);
        ed_phone = (EditText) findViewById(R.id.ed_name);
        ed_password = (EditText) findViewById(R.id.ed_password);
        img_icon = (ImageView) findViewById(R.id.img_icon);
        pb = (ProgressBar) findViewById(R.id.pb);
        loginpresent=new Loginpresent(this);
        regpresent = new Regpresent(this);
    }
    @Override
    public void showprogressbar() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pb.setVisibility(View.VISIBLE);
            }
        });
    }
    @Override
    public void hindprogressbar() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pb.setVisibility(View.GONE);
            }
        });

    }
    @Override
    public void nameError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void passError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void successreg(String code, final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void faillreg(String code, final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void successlogin(String code, final String msg,int uid) {
                sp.edit().clear().commit();
                sp.edit().putInt("uid",uid).commit();
                sp.edit().putString("name",ed_phone.getText().toString()).commit();
                sp.edit().putBoolean("memessage",true).commit();
        Intent in=new Intent(MainActivity.this,HomeActivity.class);
        sp.edit().putBoolean("wode",true).commit();
        startActivity(in);

    }
    @Override
    public void faillogin(String code, final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onfailure(Call call, IOException e) {
        runOnUiThread(new Runnable() {
        @Override
        public void run() {
            Toast.makeText(MainActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
        }
    });
}
}

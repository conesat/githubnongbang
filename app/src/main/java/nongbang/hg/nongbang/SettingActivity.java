package nongbang.hg.nongbang;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import nongbang.hg.nongbang.tools.Vibration;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView wdzhendong,wdlingsheng,wdzhuangtailan,wdbanben,wdfangwen,wdfenxiang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_setting);
        InitView();
    }

    public void InitView(){

        //震动通知按钮
        wdzhendong=(TextView)findViewById(R.id.wd_zhendong);
        wdzhendong.setOnClickListener(this);
        //铃声通知按钮
        wdlingsheng=(TextView)findViewById(R.id.wd_lingsheng);
        wdlingsheng.setOnClickListener(this);
        //状态栏通知按钮
        wdzhuangtailan=(TextView)findViewById(R.id.wd_zhuangtailan);
        wdzhuangtailan.setOnClickListener(this);
        //版本更新按钮
        wdbanben=(TextView)findViewById(R.id.wd_banben);
        wdbanben.setOnClickListener(this);
        //分享按钮
        wdfenxiang=(TextView)findViewById(R.id.wd_fenxiang);
        wdfenxiang.setOnClickListener(this);
        //访问我们按钮
        wdfangwen=(TextView)findViewById(R.id.wd_fangwen);
        wdfangwen.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        Vibrator vibrator=(Vibrator) getSystemService(VIBRATOR_SERVICE);
        Vibration vibration=new Vibration(vibrator);
        switch (v.getId()){
            case R.id.wd_zhendong:
                if (wdzhendong.getText().toString().compareTo("关 >")==0){
                    vibration.beginVibration();
                    wdzhendong.setText("开 >");
                    ShowText("震动提示已打开");
                }else {
                    vibration.closeVibration();
                    wdzhendong.setText("关 >");
                    ShowText("震动提示已关闭");
                }
                break;
            case R.id.wd_lingsheng:
                startActivity(new Intent(SettingActivity.this,test.class));
                if (wdlingsheng.getText().toString().compareTo("关 >")==0){
                    wdlingsheng.setText("开 >");
                    ShowText("声音提示已打开");
                }else {
                    wdlingsheng.setText("关 >");
                    ShowText("声音提示已关闭");
                }
                break;
            case R.id.wd_zhuangtailan:
                if (wdzhuangtailan.getText().toString().compareTo("关 >")==0){
                    wdzhuangtailan.setText("开 >");
                    ShowText("状态栏信息已打开");
                }else {
                    wdzhuangtailan.setText("关 >");
                    ShowText("状态栏信息已关闭");
                }
                break;
            case R.id.wd_banben:
                break;
            case R.id.wd_fenxiang:
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                Uri uri = Uri.parse("http://39.108.70.152/"); intent.putExtra(Intent.EXTRA_STREAM,uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, getTitle()));
                break;
            case R.id.wd_fangwen:
                Intent intent1 = new Intent();
                intent1.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("http://39.108.70.152/");
                intent1.setData(content_url);
                startActivity(intent1);
                break;

        }
    }

    /*
弹出提示
 */
    public void ShowText(String s){
        Toast.makeText(SettingActivity.this,s,Toast.LENGTH_SHORT).show();
    }

}

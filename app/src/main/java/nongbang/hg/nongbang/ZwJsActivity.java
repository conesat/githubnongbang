package nongbang.hg.nongbang;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import nongbang.hg.nongbang.MSQLite.ZwDb;
import nongbang.hg.nongbang.util.FindFloder;

/**
 * 查看详细情况
 */
public class ZwJsActivity extends AppCompatActivity implements View.OnClickListener{

    private Button jiaruzhuangyuan;
    private ImageButton jsback;
    private ImageView zwjsiv;
    private TextView name;
    private ZwDb zwDb;
    private SQLiteDatabase db;
    private String NAME;
    private TextView jsmingcheng,jsxueming,jsfenlei,jsguangzhao,jswendu,jsjianjie,jsturang,jsfeiliao,jsbaike;
    private String ACT,XUEMING;//启动来源 学名
    private Uri baike;
    ArrayList<File> files;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.zwjs_layout);
        jsback=(ImageButton) findViewById(R.id.jsback);
        zwjsiv=(ImageView)findViewById(R.id.zwjsiv);
        jiaruzhuangyuan=(Button)findViewById(R.id.jiaruzhuangyuan);
        name=(TextView)findViewById(R.id.jsmingzi);
        jsmingcheng=(TextView)findViewById(R.id.jsmingcheng);
        jsxueming=(TextView)findViewById(R.id.jsxueming);
        jsfenlei=(TextView)findViewById(R.id.jsfenlei);
        jsguangzhao=(TextView)findViewById(R.id.jsguangzhao);
        jswendu=(TextView)findViewById(R.id.jswendu);
        jsjianjie=(TextView)findViewById(R.id.jsjianjie);
        jsturang=(TextView)findViewById(R.id.jsturang);
        jsfeiliao=(TextView)findViewById(R.id.jsfeiliao);
        jsbaike=(TextView)findViewById(R.id.jsbaike);
        jsbaike.setOnClickListener(this);
        jiaruzhuangyuan.setOnClickListener(this);
        jsback.setOnClickListener(this);
        zwjsiv.setOnClickListener(this);
        NAME=this.getIntent().getExtras().getString("name");
        XUEMING=this.getIntent().getExtras().getString("xueming");
        ACT=this.getIntent().getExtras().getString("ACT");
        name.setText(NAME);
        zwDb=new ZwDb(this);
        db=zwDb.OpenZwdb();
        Cursor cursor= db.rawQuery("select * from zw where xueming == '"+XUEMING+"'",null);//查询
        if (cursor!=null){
            while (cursor.moveToNext()){
                jsmingcheng.setText(NAME);
                jsxueming.setText(cursor.getString(cursor.getColumnIndex("xueming")));
                jsfenlei.setText(cursor.getString(cursor.getColumnIndex("fenlei")));
                jsguangzhao.setText(cursor.getString(cursor.getColumnIndex("guangzhao")));
                jswendu.setText(cursor.getString(cursor.getColumnIndex("wendud"))+"-"+cursor.getString(cursor.getColumnIndex("wendug"))+"度");
                jsturang.setText(cursor.getString(cursor.getColumnIndex("turang")));
                jsfeiliao.setText(cursor.getString(cursor.getColumnIndex("feiliao")));
                jsjianjie.setText(cursor.getString(cursor.getColumnIndex("jianjie")));
                baike= Uri.parse(cursor.getString(cursor.getColumnIndex("baike")));


            }
            cursor.close();//释放游标
        }
        db.close();//关闭数据库
        if (ACT.compareTo("MyZWActivity")==0){
            jiaruzhuangyuan.setVisibility(View.GONE);
        }

        files=FindFloder.FindFromFloder(Environment.getExternalStorageDirectory() + "/.nongbang/zwdata/images/" + jsxueming.getText().toString());
        if (files.size()>0)
        Picasso
                .with(this)
                .load("file://"+String.valueOf(files.get(files.size()-1)))
                .error(R.drawable.zanwei)
                .placeholder(R.drawable.zanwei)
                .into(zwjsiv);
        else {
            Picasso
                    .with(this)
                    .load(R.drawable.zanwei)
                    .into(zwjsiv);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.jsback:
                if (ACT.compareTo("MyZWActivity")==0){
                    ZwJsActivity.this.finish();
                }else if (ACT.compareTo("MainActivity")==0) {
                    //startActivityForResult(new Intent(ZwJsActivity.this, MainActivity.class),124);
                    ZwJsActivity.this.finish();
                }
                break;
            case R.id.zwjsiv:
                PhotoShopActivity.NAME = NAME;
                PhotoShopActivity.images =files;
                startActivity((new Intent(ZwJsActivity.this, PhotoShopActivity.class)).putExtra("ACT","ZwJsActivity"));
                //startActivity(new Intent(ZwJsActivity.this,PhotoShopActivity.class));
                break;
            case R.id.jiaruzhuangyuan:
                Intent intent=new Intent(ZwJsActivity.this,AddZwActivity.class);
                intent.putExtra("name",name.getText().toString());
                intent.putExtra("xueming",jsxueming.getText().toString());
                startActivity(intent);
                break;
            case R.id.jsbaike:
                Intent intent1 = new Intent();
                intent1.setAction("android.intent.action.VIEW");
                intent1.setData(baike);
                startActivity(intent1);
                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            if (ACT.compareTo("MyZWActivity")==0){
                ZwJsActivity.this.finish();
            }else if (ACT.compareTo("MainActivity")==0) {
              //  startActivityForResult(new Intent(ZwJsActivity.this, MainActivity.class),124);
                ZwJsActivity.this.finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }


}

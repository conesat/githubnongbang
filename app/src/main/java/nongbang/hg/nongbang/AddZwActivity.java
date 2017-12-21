package nongbang.hg.nongbang;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;

import nongbang.hg.nongbang.MSQLite.ZWDBOpenHelper;
import nongbang.hg.nongbang.util.Checknetwork;
import nongbang.hg.nongbang.util.SaveImage;

/**
 * 中间图标点作物里的加入庄园
 */
public class AddZwActivity extends AppCompatActivity implements View.OnClickListener {


    private ZWDBOpenHelper ZWDBOpenHelper;
    private SQLiteDatabase db;
    private String name,xueming,eddate;
    private EditText edname,edsay;
    private Button addtianjia,rilifanhui,riliqueding;
    private ImageButton addback;
    private TextView textViewrili;
    private DatePicker date = null;
    private View CustomView;
    private SharedPreferences sp;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_addzw);
        edname=(EditText)findViewById(R.id.addmingcheng);
        edsay=(EditText)findViewById(R.id.addsay);
        addback=(ImageButton) findViewById(R.id.addback);
        imageView=(ImageView)findViewById(R.id.add_iv);
        addback.setOnClickListener(this);
        addtianjia=(Button)findViewById(R.id.addtianjia);
        addtianjia.setOnClickListener(this);
        db= openOrCreateDatabase("mzw.db",MODE_PRIVATE,null);
        textViewrili=(TextView)findViewById(R.id.addriqi);
        textViewrili.setOnClickListener(this);

        sp = getSharedPreferences("config", MODE_PRIVATE);
        if (sp.getString("city", "").equals("")) {
            if (Checknetwork.isNetworkAvailable(AddZwActivity.this)) {
                startActivity(new Intent(AddZwActivity.this,ChoiceCityActivity.class));
            }else {
                Toast.makeText(AddZwActivity.this,"无网络连接无法获取地名",Toast.LENGTH_SHORT).show();
            }
        }
        xueming=this.getIntent().getExtras().getString("xueming");

        Picasso
                .with(this)
                .load("file://"+Environment.getExternalStorageDirectory() + "/.nongbang/zwdata/images/"+xueming+"/j1.jpg")
                .resize(300,300)
                .error(R.drawable.zanwei)
                .placeholder(R.drawable.zanwei)
                .into(imageView);
    }

    private class OnDateChangedListenerImpl implements DatePicker.OnDateChangedListener {
        @Override
        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            AddZwActivity.this.setDateTime();
        }
    }
    public  void setDateTime(){
        this.textViewrili.setText(this.date.getYear() + "-"
                + (this.date.getMonth() + 1) + "-" + this.date.getDayOfMonth());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addback:
                AddZwActivity.this.finish();
                break;
            case R.id.addtianjia:
                if (db!=null){
                    if (edname.getText().toString().compareTo("")==0){
                        Toast.makeText(AddZwActivity.this,"名称不能为空",Toast.LENGTH_SHORT).show();
                        return;
                    }else if (edsay.getText().toString().compareTo("")==0){
                        Toast.makeText(AddZwActivity.this,"寄语不能为空",Toast.LENGTH_SHORT).show();
                        return;
                    }else if(textViewrili.getText().toString().compareTo("")==0){
                        Toast.makeText(AddZwActivity.this,"日期不能为空",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ContentValues values=new ContentValues();
                    values.put("name",edname.getText().toString());
                    values.put("riqi",textViewrili.getText().toString());
                    values.put("say",edsay.getText().toString());
                    values.put("xueming",xueming);
                    if(db.insert("mzw",null,values)==-1){
                        Toast.makeText(AddZwActivity.this,"添加失败该作物名已存在",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(AddZwActivity.this,"加入成功",Toast.LENGTH_SHORT).show();
                        File file=new File(Environment.getExternalStorageDirectory()+"/.nongbang/images"+"/"+edname.getText().toString());
                        if(file.exists()){
                            deleteDirWihtFile(file);
                        }
                        try {
                            file.mkdir();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        file=new File(Environment.getExternalStorageDirectory()+"/.nongbang/images"+"/"+edname.getText().toString()+"noImage");
                        try {
                            file.mkdir();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        SaveImage saveImage=new SaveImage(Environment.getExternalStorageDirectory()+"/.nongbang/images"+"/"+edname.getText().toString()+"noImage");
                        saveImage.saveDrawable(imageView.getDrawable(),"no.jpg",Bitmap.CompressFormat.JPEG);
                        MainActivity.mainActivity.finish();
                        startActivity(new Intent(AddZwActivity.this,MainActivity.class));
                        AddZwActivity.this.finish();
                    }
                    values.clear();

                 }
                break;
            case R.id.addriqi:
                Builder builder=myBuilder(AddZwActivity.this);
                final AlertDialog dialog=builder.show();
                date=(DatePicker)CustomView.findViewById(R.id.datePicker);
                this.date.init(this.date.getYear(), this.date.getMonth(), this.date.getDayOfMonth(), new OnDateChangedListenerImpl());
                rilifanhui=(Button)CustomView.findViewById(R.id.riliback);
                rilifanhui.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                riliqueding=(Button)CustomView.findViewById(R.id.riliqueding);
                riliqueding.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textViewrili.setText(date.getYear() + "-"
                                + (date.getMonth() + 1) + "-" + date.getDayOfMonth());
                        dialog.dismiss();
                    }
                });
                break;
        }
    }

    public static void deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDirWihtFile(file); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }

    protected Builder myBuilder(AddZwActivity dialogWindows) {
        final LayoutInflater inflater=this.getLayoutInflater();
        AlertDialog.Builder builder=new AlertDialog.Builder(dialogWindows);
        CustomView=inflater.inflate(R.layout.rililayout, null);
        return builder.setView(CustomView);
    }
}
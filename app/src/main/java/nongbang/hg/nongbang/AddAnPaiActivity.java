package nongbang.hg.nongbang;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import nongbang.hg.nongbang.StaticClass.StaticVariable;

/**
 * 庄园作物的添加安排
 */
public class AddAnPaiActivity extends AppCompatActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {
    private ImageButton finish;
    private ImageButton back;
    private Button riqi;
    private EditText title,describe;
    private SQLiteDatabase db;
    public static String NAME;
    private CheckBox watering,sunshine,moving,trim,fertilizer,weed,pyrethrum,pollination;
    public static AddAnPaiActivity addactivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_addanpai);
        riqi=(Button) findViewById(R.id.addap_riqi);
        finish=(ImageButton)findViewById(R.id.add_finish);
        back=(ImageButton)findViewById(R.id.add_back);
        title=(EditText)findViewById(R.id.addap_title);
        describe=(EditText)findViewById(R.id.addap_describe);
        watering=(CheckBox)findViewById(R.id.addap_watering);
        watering.setOnCheckedChangeListener(this);
        sunshine=(CheckBox)findViewById(R.id.addap_sunshine);
        sunshine.setOnCheckedChangeListener(this);
        moving=(CheckBox)findViewById(R.id.addap_moving);
        moving.setOnCheckedChangeListener(this);
        trim=(CheckBox)findViewById(R.id.addap_trim);
        trim.setOnCheckedChangeListener(this);
        fertilizer=(CheckBox)findViewById(R.id.addap_fertilizer);
        fertilizer.setOnCheckedChangeListener(this);
        weed=(CheckBox)findViewById(R.id.addap_weed);
        weed.setOnCheckedChangeListener(this);
        pyrethrum=(CheckBox)findViewById(R.id.addap_pyrethrum);
        pyrethrum.setOnCheckedChangeListener(this);
        pollination=(CheckBox)findViewById(R.id.addap_pollination);
        pollination.setOnCheckedChangeListener(this);
        finish.setOnClickListener(this);
        back.setOnClickListener(this);
        riqi.setOnClickListener(this);
        addactivity=this;
    }

   @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            Intent intent=new Intent(AddAnPaiActivity.this,MyZWActivity.class);
            intent.putExtra("name",NAME);
            startActivity(intent);
            AddAnPaiActivity.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_back:
                Intent intent1=new Intent(AddAnPaiActivity.this,MyZWActivity.class);
                intent1.putExtra("name",NAME);
                startActivity(intent1);
                AddAnPaiActivity.this.finish();
                break;
            case R.id.addap_riqi:
                startActivity(new Intent(AddAnPaiActivity.this,APRQActivity.class));
                break;
            case R.id.add_finish:
                if (riqi.getText().toString().compareTo("点击设置提醒日期")==0){
                    Toast.makeText(AddAnPaiActivity.this, "请点底部部设置日期和时间", Toast.LENGTH_LONG).show();
                    return;
                }
                if (title.getText().toString().compareTo("")!=0) {
                    if (describe.getText().toString().compareTo("") != 0) {
                        db = openOrCreateDatabase("map.db", MODE_PRIVATE, null);
                        Cursor cursor= db.rawQuery("select * from map",null);//查询
                        ContentValues values = new ContentValues();
                        values.put("_id", cursor.getCount());
                        values.put("name", NAME);
                        values.put("title", title.getText().toString());
                        values.put("describe", describe.getText().toString());
                        values.put("time", StaticVariable.APRTIME);
                        values.put("date", StaticVariable.APRQDATE);
                        values.put("repeat", StaticVariable.APRQREPEAT);
                        if (db.insert("map", null, values) != -1) {
                            Toast.makeText(AddAnPaiActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(AddAnPaiActivity.this, "添加失败 未知错误", Toast.LENGTH_SHORT).show();
                        }
                        db.close();
                    } else {
                        Toast.makeText(AddAnPaiActivity.this, "描述不应为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }else {
                    Toast.makeText(AddAnPaiActivity.this, "标题不应为空", Toast.LENGTH_SHORT).show();
                    return;
                }
              //  startActivity(new Intent(AddAnPaiActivity.this,MyZWActivity.class));
                Intent intent=new Intent(AddAnPaiActivity.this,MyZWActivity.class);
                intent.putExtra("name",NAME);
                startActivity(intent);
                AddAnPaiActivity.this.finish();
                break;
        }
    }

    public static void setTitle(){
        if (StaticVariable.APRQDATE.toString().compareTo("")==0 && StaticVariable.APRQREPEAT.toString().compareTo("")!=0){
           addactivity.riqi.setText("每周"+StaticVariable.APRQREPEAT+"重复提醒");
        }
        if (StaticVariable.APRQDATE.toString().compareTo("")!=0 && StaticVariable.APRQREPEAT.toString().compareTo("")==0){
            addactivity.riqi.setText(StaticVariable.APRQDATE.split("-")[0]+"年"+StaticVariable.APRQDATE.split("-")[1]+"月"+StaticVariable.APRQDATE.split("-")[2]+"日"+"提醒");
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String s="";
        if (watering.isChecked())
            s="浇水 "+s;
        if (sunshine.isChecked())
            s="晒阳光 "+s;
        if (moving.isChecked())
            s="搬移 "+s;
        if (trim.isChecked())
            s="修剪 "+s;
        if (fertilizer.isChecked())
            s="施肥 "+s;
        if (weed.isChecked())
            s="除草 "+s;
        if (pyrethrum.isChecked())
            s="除虫 "+s;
        if (pollination.isChecked())
            s="授粉 "+s;
        if (s.compareTo("")!=0){
            title.setText(s);
        }else {
            title.setText(null);
        }
    }
}

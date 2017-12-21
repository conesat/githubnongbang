package nongbang.hg.nongbang;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nongbang.hg.nongbang.MSQLite.ZWDBOpenHelper;
import nongbang.hg.nongbang.MyAdapter.FindZyListAdapter;

/*
庄园查找作物
 */
public class FindZyActivity extends AppCompatActivity{
    private List<Map<String,Object>> alllist=new ArrayList<Map<String,Object>>();//所有搜索结果数组
    private FindZyListAdapter adapter;//显示适配器
    private ListView listView;//显示结果控件
    private LinearLayout textView;
    private ZWDBOpenHelper ZWDBOpenHelper;
    private SQLiteDatabase db;
    private EditText findtext;
    private ImageButton imageButton;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_findzy);
        ZWDBOpenHelper =new ZWDBOpenHelper(FindZyActivity.this,"mzw.db");
        db= ZWDBOpenHelper.getWritableDatabase();
        initView();
    }


    /*
    初始化控件
     */
    public void initView(){
        textView=(LinearLayout)findViewById(R.id.findzw_nofind);
        listView=(ListView)findViewById(R.id.findzw_listview);
        findtext= (EditText) findViewById(R.id.findzw_editText);
        imageButton=(ImageButton)findViewById(R.id.findzw_imagebutton);
        progressBar=(ProgressBar)findViewById(R.id.findzw_progressBar);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (findtext.getText().toString().compareTo("")!=0) {
                    progressBar.setVisibility(View.VISIBLE);
                    onLoad(findtext.getText().toString());
                }
            }
        });
        findtext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 当按了搜索之后关闭软键盘
                    ((InputMethodManager) findtext.getContext().getSystemService(
                            Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            FindZyActivity.this.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    if (findtext.getText().toString().compareTo("")!=0) {
                        progressBar.setVisibility(View.VISIBLE);
                        onLoad(findtext.getText().toString());
                    }
                    return true;
                }
                return false;
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(FindZyActivity.this,MyZWActivity.class);
                intent.putExtra("name",((TextView)view.findViewById(R.id.zymingzi)).getText().toString());
                startActivity(intent);
            }
        });
        findtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    if (findtext.getText().toString().compareTo("")!=0) {
                        progressBar.setVisibility(View.VISIBLE);
                        onLoad(findtext.getText().toString());
                    }

                }else {

                }
            }
        });
        adapter=new FindZyListAdapter(alllist,this);
        listView.setAdapter(adapter);

    }

    public void findZwByName(String name){

        alllist.clear();
        Cursor cursor= db.rawQuery("select * from mzw where name like  '%"+name +"%' order by _id desc",null);//查询
        if (cursor!=null){
            while (cursor.moveToNext()){
                Map<String ,Object> map=new HashMap<String, Object>();
                map.put("mc",cursor.getString(cursor.getColumnIndex("name")));
                map.put("say",cursor.getString(cursor.getColumnIndex("say")));
                alllist.add(map);
            }
            cursor.close();//释放游标
        }
        if (alllist.size()==0){
            textView.setVisibility(View.VISIBLE);
        }else {
            textView.setVisibility(View.GONE);
        }
        adapter.onDataChange(alllist);
        progressBar.setVisibility(View.GONE);
    }

    public void onLoad(String name) {
        final String Name=name;
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Name.compareTo("")==0)
                    return;
                findZwByName(Name);
            }
        },500);

    }

    @Override
    protected void onDestroy() {
        db.close();//关闭数据库
        super.onDestroy();
    }
}

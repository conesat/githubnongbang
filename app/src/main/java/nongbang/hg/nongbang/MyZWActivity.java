package nongbang.hg.nongbang;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ant.liao.GifView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nongbang.hg.nongbang.MSQLite.APDBOpenHelper;
import nongbang.hg.nongbang.MSQLite.ZwDb;
import nongbang.hg.nongbang.MyAdapter.ApListAdapter;
import nongbang.hg.nongbang.camera.PermissionsChecker;
import nongbang.hg.nongbang.camera.PhotoUtil;
import nongbang.hg.nongbang.tools.HttpThread;
import nongbang.hg.nongbang.util.ACache;
import nongbang.hg.nongbang.util.Checknetwork;
import nongbang.hg.nongbang.util.FindFloder;
import nongbang.hg.nongbang.util.SaveImage;
import nongbang.hg.nongbang.util.WeatherUtil;

import static android.os.Build.VERSION_CODES.M;


public class MyZWActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,View.OnClickListener,ApListAdapter.DeleteApListener {
    public SwipeRefreshLayout swipeRefresh;//下拉刷新
    private TextView name;//作物详细内的名字
    private TextView nianling;//作物年龄
    private String NAME;//作物名字
    private TextView wendud,wendug,weather,dcity,title;//最低温度 最高温度 天气（晴天 多云等）当前城市 标题作物名称
    private Button shanchu;//删除按钮
    private Button addanpai;//添加安排按钮
    private SQLiteDatabase db;//
    private String RIQI;//种植日期
    private String DQRIQI;//当前日期
    private ZwDb zwDb;//作物数据库
    private String xueming;//作物学名
    private String guang=null;//作物的光照数据
    private  int wd=0,wg=0;//作物的最高最低温度范围
    private LinearLayout mzwxiangxi,weahterxx;//作物详细布局和天气详细布局
    private Button buttonsq,buttonxx;//隐藏按钮
    private ImageButton cityedit,back;//切换城市图标与返回图标
    private boolean chancecity=false;//是否切换城市
    private GifView imageView1,imageView2;//两个天气图片
    private TextView textView1,textView2,
            textView3,textView4,textView5,
            textView6,textView7,textView8,
            textView9,textView10,textView11,
            textView12;//详细天气
    private String result;//缓存的天气
    private TextView tijian,photosm,mzwxx;//体检 当前没有照片说明 查看详细
    private ImageButton takephoto;//拍照
    private ImageView photozs;//作物图片框
    private PhotoUtil photoUtil;
    private Uri imageUri;//图片url
    private static final int REQUEST_PERMISSION = 4;  //权限请求
    static final String[] PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    private ACache mCache;
    private ArrayList<File> images;
    private APDBOpenHelper apdbOpenHelper;
    private SQLiteDatabase apdb;
    private ApListAdapter apListAdapter;
    public  List<Map<String,Object>> alllist=new ArrayList<Map<String,Object>>();
    private LinearLayout view;
    private View CustomView;
    public static MyZWActivity myZWActivity;
    private LinearLayout nointernet,haveinternet;
    private TextView yijian;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_myzw);
        myZWActivity=this;
        apdbOpenHelper =new APDBOpenHelper(MyZWActivity.this,"map.db");
        apdb= apdbOpenHelper.getWritableDatabase();

        NAME=this.getIntent().getExtras().getString("name");
        INIT();
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        //为SwipeRefreshLayout设置监听事件
        swipeRefresh.setOnRefreshListener(this);
        //为SwipeRefreshLayout设置刷新时的颜色变化，最多可以设置4种
        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        ChaNl();
        mCache = ACache.get(this);
        result = mCache.getAsString("weather");
        if(result==null){
            if (Checknetwork.isNetworkAvailable(MyZWActivity.this)) {
                sp = getSharedPreferences("config", MODE_PRIVATE);
                String city = sp.getString("city", "");
                if (city!=null)
                new WeatherThread(city).start();
            }else {
                yijian.setText("没有网络连接");
                yijian.setTextColor(Color.RED);
                nointernet.setVisibility(View.VISIBLE);
                haveinternet.setVisibility(View.GONE);
                buttonxx.setVisibility(View.GONE);
            }
        }else {
            List<String> list = WeatherUtil.parseXML(result);
            initView(list);
        }
        mPermissionsChecker = new PermissionsChecker(this);
        photoUtil = new PhotoUtil(this);
        if ((images= FindFloder.FindFromFloder(Environment.getExternalStorageDirectory()+"/.nongbang/images"+"/"+NAME)).size()>0){
            photosm.setVisibility(View.GONE);
            Picasso
                    .with(this)
                    .load("file://"+String.valueOf(images.get(0)))
                    .into(photozs);
        }else {
            photosm.setVisibility(View.VISIBLE);
        }
    }






    /*
    初始化控件
     */
    public void INIT(){
        wendug=(TextView)findViewById(R.id.mzw_wendug) ;
        wendud=(TextView)findViewById(R.id.mzw_wendud) ;
        weather=(TextView)findViewById(R.id.mzw_weather) ;
        nointernet=(LinearLayout)findViewById(R.id.mzw_nointernet);
        nointernet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Checknetwork.isNetworkAvailable(MyZWActivity.this)) {
                    nointernet.setVisibility(View.GONE);
                    haveinternet.setVisibility(View.VISIBLE);
                    buttonxx.setVisibility(View.VISIBLE);
                    sp = getSharedPreferences("config", MODE_PRIVATE);
                    String city = sp.getString("city", "");
                    new WeatherThread(city).start();
                }else {
                    nointernet.setVisibility(View.VISIBLE);
                    haveinternet.setVisibility(View.GONE);
                    buttonxx.setVisibility(View.GONE);
                    yijian.setText("没有网络连接");
                    yijian.setTextColor(Color.RED);
                    Toast.makeText(MyZWActivity.this,"无网络",Toast.LENGTH_SHORT).show();
                }
            }
        });
        haveinternet=(LinearLayout)findViewById(R.id.mzw_haveinternet);
        dcity=(TextView)findViewById(R.id.mzw_city) ;
        title=(TextView)findViewById(R.id.mzw_title);
        cityedit=(ImageButton) findViewById(R.id.mzw_cityedit);
        back=(ImageButton)findViewById(R.id.mzw_back);
        takephoto=(ImageButton)findViewById(R.id.takephoto);
        photosm=(TextView)findViewById(R.id.photosm);
        photozs=(ImageView)findViewById(R.id.photozs);
        mzwxiangxi=(LinearLayout)findViewById(R.id.mzw_xiangxi);
        buttonsq=(Button)findViewById(R.id.mzw_sq);
        shanchu=(Button)findViewById(R.id.mzwsc);
        nianling=(TextView)findViewById(R.id.mzwnl);
        name=(TextView)findViewById(R.id.mzwmc);
        weahterxx=(LinearLayout)findViewById(R.id.weatherxx) ;
        imageView1=(GifView)findViewById(R.id.mzw_gif1);
        imageView2=(GifView)findViewById(R.id.mzw_gif2);
        buttonxx=(Button)findViewById(R.id.weather_xxbut) ;
        textView1=(TextView)findViewById(R.id.xx_1);
        textView2=(TextView)findViewById(R.id.xx_2);
        textView3=(TextView)findViewById(R.id.xx_3);
        textView4=(TextView)findViewById(R.id.xx_4);
        textView5=(TextView)findViewById(R.id.xx_m1);
        textView6=(TextView)findViewById(R.id.xx_m2);
        textView7=(TextView)findViewById(R.id.xx_m3);
        textView8=(TextView)findViewById(R.id.xx_m4);
        textView9=(TextView)findViewById(R.id.xx_h1);
        textView10=(TextView)findViewById(R.id.xx_h2);
        textView11=(TextView)findViewById(R.id.xx_h3);
        textView12=(TextView)findViewById(R.id.xx_h4);
        tijian=(TextView)findViewById(R.id.mzwtj);
        takephoto=(ImageButton)findViewById(R.id.takephoto);
        photosm=(TextView)findViewById(R.id.photosm);
        photozs=(ImageView)findViewById(R.id.photozs);
        mzwxx=(TextView)findViewById(R.id.mzwxx);
        addanpai=(Button)findViewById(R.id.mzw_addanpai);
        yijian=(TextView)findViewById(R.id.mzw_yj);
        mzwxx.setOnClickListener(this);
        takephoto.setOnClickListener(this);
        tijian.setOnClickListener(this);
        back.setOnClickListener(this);
        cityedit.setOnClickListener(this);
        dcity.setOnClickListener(this);
        buttonxx.setOnClickListener(this);
        buttonsq.setOnClickListener(this);
        shanchu.setOnClickListener(this);
        photozs.setOnClickListener(this);
        addanpai.setOnClickListener(this);

        ListView listView=(ListView)findViewById(R.id.mzw_anpailistview);
        view=(LinearLayout)findViewById(R.id.jianyi_ap);
        Cursor cursor= apdb.rawQuery("select * from map where name = '"+NAME+"'",null);//查询

        if (cursor!=null){
            while (cursor.moveToNext()){
                Map<String ,Object> map=new HashMap<String, Object>();
                Log.i("name",cursor.getString(cursor.getColumnIndex("name")));
                map.put("title",cursor.getString(cursor.getColumnIndex("title")));
                map.put("time",cursor.getString(cursor.getColumnIndex("time")));
                map.put("repeat",cursor.getString(cursor.getColumnIndex("repeat")));
                map.put("date",cursor.getString(cursor.getColumnIndex("date")));
                map.put("describe",cursor.getString(cursor.getColumnIndex("describe")));
                alllist.add(map);
            }
            cursor.close();//释放游标
        }
        if (alllist.size()==0){
            view.setVisibility(View.GONE);
        }
        apdb.close();//关闭数据库

        apListAdapter=new ApListAdapter(alllist,this);
        apListAdapter.setDeleteApListener(this);
        listView.setAdapter(apListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder=myBuilder(MyZWActivity.this);
                final AlertDialog dialog=builder.show();
                TextView title=(TextView)CustomView.findViewById(R.id.arrange_title);
                title.setText(alllist.get(position).get("title").toString());
                TextView cont=(TextView)CustomView.findViewById(R.id.arrange_content);
                cont.setText(alllist.get(position).get("describe").toString());
                TextView ti=(TextView)CustomView.findViewById(R.id.arrange_time);
                String stringtime=alllist.get(position).get("time").toString();
                String[] stringtimes=stringtime.split(":");
                if(stringtimes[1].toCharArray().length==1)
                {
                    stringtimes[1]="0"+stringtimes[1];
                }
                if(stringtimes[0].toCharArray().length==1)
                {
                    stringtimes[0]="0"+stringtimes[0];
                }
                if (alllist.get(position).get("repeat").toString().compareTo("")!=0 && alllist.get(position).get("date").toString().compareTo("")==0) {
                    String[] strings=alllist.get(position).get("repeat").toString().split("/");
                    String s=strings[0];
                    for (int i=1;i<strings.length;i++)
                        s=s+" "+strings[i];
                    ti.setText("每周"+s+" "+stringtimes[0]+":"+stringtimes[1]+" 重复提醒");
                }else  if (alllist.get(position).get("repeat").toString().compareTo("")==0 && alllist.get(position).get("date").toString().compareTo("")!=0)
                    ti.setText(alllist.get(position).get("date").toString()+" "+stringtimes[0]+":"+stringtimes[1]+" 提醒");
            }
        });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 303){
            if ((images=FindFloder.FindFromFloder(Environment.getExternalStorageDirectory()+"/.nongbang/images"+"/"+NAME)).size()>0){
                Picasso
                        .with(this)
                        .load("file://"+String.valueOf(images.get(images.size()-1)))
                        .into(photozs);
                photosm.setVisibility(View.GONE);
            }else {
                photosm.setVisibility(View.VISIBLE);
            }
        }
        Bitmap bitmap;
        if (resultCode==RESULT_OK){
            if (requestCode == 0){
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    Matrix matrix = new Matrix();
                    matrix.setScale(0.3f, 0.3f);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                            bitmap.getHeight(), matrix, true);
                    photosm.setVisibility(View.INVISIBLE);
                    Picasso
                            .with(this)
                            .load(imageUri)
                            .into(photozs);
                    SaveImage saveImage=new SaveImage(Environment.getExternalStorageDirectory()+"/.nongbang/images"+"/"+NAME);
                    SimpleDateFormat    formatter    =   new    SimpleDateFormat    ("yyyy年MM月dd日HH:mm:ss");
                    Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间
                    String    str    =    formatter.format(curDate);
                    saveImage.saveBitmap(bitmap,str+".jpg",Bitmap.CompressFormat.JPEG);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }





    /*
    展示获取的天气
     */
    private void initView(List<String> list) {
        String xinxi;
        String[] strings=list.get(5).split("/");
        wendug.setText(strings[1].split("℃")[0]);
        wendud.setText("/"+strings[0]);
        xinxi=xueming+"/"+strings[1].split("℃")[0]+"/"+strings[0].split("℃")[0]+"/"+list.get(6).split(" ")[1];
        dcity.setText(list.get(1));
        weather.setText(list.get(6).split(" ")[1]);
        try {
            imageView1.setGifImage(getAssets().open("weather/"+list.get(8)));
            imageView2.setGifImage(getAssets().open("weather/"+list.get(9)));
            imageView1.setShowDimension(40, 40);
            imageView2.setShowDimension(40, 40);
        } catch (IOException e) {
            e.printStackTrace();
        }
        strings=list.get(10).split("；");
        textView1.setText("气温："+strings[0].split("：")[2]);
        textView2.setText(strings[1]);
        textView3.setText(strings[2]);
        textView4.setText(strings[3].split("。")[0]+"\n"+strings[3].split("。")[1]);
        textView5.setText(list.get(13).split(" ")[0]);
        textView6.setText(list.get(12));
        textView7.setText(list.get(13).split(" ")[1]);
        textView8.setText(list.get(14));
        textView9.setText(list.get(18).split(" ")[0]);
        textView10.setText(list.get(17));
        textView11.setText(list.get(18).split(" ")[1]);
        textView12.setText(list.get(19));
        xinxi=xinxi+"/"+list.get(12).split("/")[0].split("℃")[0]+"/"+list.get(12).split("/")[1].split("℃")[0]+"/"+list.get(13).split(" ")[1];
      //  xinxi=xinxi+"/"+list.get(17).split("/")[0].split("℃")[0]+"/"+list.get(17).split("/")[1].split("℃")[0]+"/"+list.get(18).split(" ")[1];
        HttpThread thread = new HttpThread("getyijian");
        thread.GetYiJian(xinxi);
    }
    //使用Handler来处理多线程之间的通信
    public Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    swipeRefresh.setRefreshing(false);
                    List<String> list = (List<String>)msg.obj;
                    Toast.makeText(MyZWActivity.this,"刷新完成",Toast.LENGTH_SHORT).show();
                    initView(list);
                    break;
                case 404:
                    swipeRefresh.setRefreshing(false);
                    Toast.makeText(MyZWActivity.this,"刷新失败",Toast.LENGTH_SHORT).show();
                    break;
                case 405:
                    swipeRefresh.setRefreshing(false);
                    Toast.makeText(MyZWActivity.this,"无新数据",Toast.LENGTH_SHORT).show();
                    break;
                case 406:
                    yijian.setText(msg.obj.toString());
                   // swipeRefresh.setRefreshing(false);
                    //Toast.makeText(MyZWActivity.this,"无新数据",Toast.LENGTH_SHORT).show();
                    break;
                case 407:
                    swipeRefresh.setRefreshing(false);
                    Toast.makeText(MyZWActivity.this,"服务器维护中！",Toast.LENGTH_SHORT).show();

                    break;
            }
        };
    };



    private SharedPreferences sp;

    @Override
    public void onRefresh() {
        //刷新
        mCache = ACache.get(this);
        result = mCache.getAsString("weather");
        if(result==null) {
            if (Checknetwork.isNetworkAvailable(MyZWActivity.this)) {
                nointernet.setVisibility(View.GONE);
                haveinternet.setVisibility(View.VISIBLE);
                buttonxx.setVisibility(View.VISIBLE);
                sp = getSharedPreferences("config", MODE_PRIVATE);
                String city = sp.getString("city", "");
                if (city.compareTo("") == 0) {
                    startActivity(new Intent(MyZWActivity.this, ChoiceCityActivity.class));
                } else {
                    new WeatherThread(city).start();
                }
            }else {
                swipeRefresh.setRefreshing(false);
                nointernet.setVisibility(View.VISIBLE);
                haveinternet.setVisibility(View.GONE);
                buttonxx.setVisibility(View.GONE);yijian.setText("没有网络连接");
                yijian.setTextColor(Color.RED);
                Toast.makeText(MyZWActivity.this,"无网络",Toast.LENGTH_SHORT).show();
            }
        }else {
            swipeRefresh.setRefreshing(false);
            Toast.makeText(MyZWActivity.this,"无新数据",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (chancecity) {
            super.onRestart();
            sp = getSharedPreferences("config", MODE_PRIVATE);
            String city = sp.getString("city", "");
            new WeatherThread(city).start();
            chancecity=false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mzw_cityedit:
                ChoiceCity();
                chancecity=true;
                break;
            case R.id.mzw_back:
                if(PhotoShopActivity.DLE) {
                    MainActivity.mainActivity.finish();
                    startActivity(new Intent(MyZWActivity.this, MainActivity.class));
                }
                MyZWActivity.this.finish();
                break;
            case R.id.mzw_city:
                ChoiceCity();
                chancecity=true;
                break;
            case R.id.mzw_sq:
                if (buttonsq.getText().toString().compareTo("更多")==0){
                    // 显示动画
                    TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f,
                            Animation.RELATIVE_TO_SELF, -0.0f, Animation.RELATIVE_TO_SELF,
                            0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    mShowAction.setRepeatMode(Animation.REVERSE);
                    mShowAction.setDuration(500);
                    mzwxiangxi.startAnimation(mShowAction);//开始动画
                    mzwxiangxi.setVisibility(View.VISIBLE);
                    buttonsq.setText("隐藏");
                }else {
                    mzwxiangxi.setVisibility(View.GONE);
                    buttonsq.setText("更多");
                }
                break;
            case R.id.weather_xxbut:
                if (buttonxx.getText().toString().compareTo("展开天气详情")==0){
                    // 显示动画
                    TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f,
                            Animation.RELATIVE_TO_SELF, -0.0f, Animation.RELATIVE_TO_SELF,
                            0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    mShowAction.setRepeatMode(Animation.REVERSE);
                    mShowAction.setDuration(500);
                    weahterxx.startAnimation(mShowAction);//开始动画
                    weahterxx.setVisibility(View.VISIBLE);
                    buttonxx.setText("隐藏天气详情");
                }else {
                    weahterxx.setVisibility(View.GONE);
                    buttonxx.setText("展开天气详情");
                }
                break;
            case R.id.mzwsc:
                showDialog();

                break;
            case R.id.mzwtj:
                startActivity(new Intent(MyZWActivity.this,TiJianActivity.class));
                break;

            case R.id.takephoto:
                //检查权限(6.0以上做权限判断)
                if (Build.VERSION.SDK_INT >= M) {
                    if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                        startPermissionsActivity();
                    } else {
                        //打开相机
                        imageUri = photoUtil.takePhoto(0);
                    }
                } else {
                    imageUri = photoUtil.takePhoto(0);
                }
                break;
            case R.id.mzwxx:
                Intent intent=new Intent(MyZWActivity.this,ZwJsActivity.class);
                intent.putExtra("name",NAME);
                intent.putExtra("ACT","MyZWActivity");
                intent.putExtra("xueming",xueming);
                startActivity(intent);
                break;
            case R.id.photozs:
                if (photosm.getVisibility()==View.VISIBLE){
                    Toast.makeText(MyZWActivity.this,"还没有照片哦！",Toast.LENGTH_SHORT).show();
                }else {
                    PhotoShopActivity.NAME = NAME;
                    PhotoShopActivity.images = FindFloder.FindFromFloder(Environment.getExternalStorageDirectory() + "/.nongbang/images" + "/" + NAME);
                    startActivity((new Intent(MyZWActivity.this, PhotoShopActivity.class)).putExtra("ACT","MyZWActivity"));
                }
                break;

            case R.id.mzw_addanpai:
                AddAnPaiActivity.NAME=NAME;
                startActivity(new Intent(MyZWActivity.this,AddAnPaiActivity.class));
                MyZWActivity.this.finish();
                break;
        }
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_PERMISSION,
                PERMISSIONS);
    }



    public void ChoiceCity(){
        if (Checknetwork.isNetworkAvailable(MyZWActivity.this)) {
            startActivity(new Intent(MyZWActivity.this,ChoiceCityActivity.class));
        }else {
            Toast.makeText(MyZWActivity.this,"无网络连接无法获取地名",Toast.LENGTH_SHORT).show();
        }
    }


    //删除安排回调方法
    @Override
    public void delete(final int p) {
        new AlertDialog.Builder(this)
                .setTitle("警告")
                .setMessage("删除后无法恢复！是否继续删除？")
                .setPositiveButton("确认删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("111", Integer.toString(p));
                        db = openOrCreateDatabase("map.db", MODE_PRIVATE, null);
                        db.execSQL("delete from map where _id = "+p);
                        Cursor cursor= db.rawQuery("select * from map ",null);//查询
                        alllist.clear();
                        if (cursor!=null){
                            while (cursor.moveToNext()){
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("title", cursor.getString(cursor.getColumnIndex("title")));
                                    map.put("describe", cursor.getString(cursor.getColumnIndex("describe")));
                                    map.put("time", cursor.getString(cursor.getColumnIndex("time")));
                                    map.put("repeat", cursor.getString(cursor.getColumnIndex("repeat")));
                                    map.put("date", cursor.getString(cursor.getColumnIndex("date")));
                                    alllist.add(map);
                            }
                            cursor.close();//释放游标
                        }
                        db.execSQL("DELETE FROM map");
                        ContentValues values = new ContentValues();
                        if (alllist.size()==0){
                            view.setVisibility(View.GONE);
                        }else {
                            for (int i = 0; i < alllist.size(); i++) {
                                values.clear();
                                values.put("_id", i);
                                values.put("title", alllist.get(i).get("title").toString());
                                values.put("describe", alllist.get(i).get("describe").toString());
                                values.put("time", alllist.get(i).get("time").toString());
                                values.put("date", alllist.get(i).get("date").toString());
                                values.put("repeat", alllist.get(i).get("repeat").toString());
                                db.insert("map", null, values);
                            }
                        }
                        db.close();
                        apListAdapter.onDataChange(alllist);
                        Toast.makeText(MyZWActivity.this,"已删除",Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setCancelable(false).show();
    }


    //访问网络的线程
    class WeatherThread extends Thread{
        String CITY;
        public WeatherThread(String city){
            CITY=city;
        }
        @Override
        public void run() {
            if (Checknetwork.isNetworkAvailable(MyZWActivity.this)) {
                String xml = WeatherUtil.getWeather(CITY);
                if(xml!=null){
                    List<String> list = WeatherUtil.parseXML(xml);
                    if (list.get(0).compareTo("访问被限制！")==0){
                        Message msg = handler.obtainMessage();
                        msg.what = 405;
                        handler.sendMessage(msg);
                    }else if (list.get(0).compareTo("系统维护中！")==0){
                        Message msg = handler.obtainMessage();
                        msg.what = 407;
                        handler.sendMessage(msg);
                    }else {
                        Message msg = handler.obtainMessage();
                        msg.obj = list;
                        msg.what = 1;
                        handler.sendMessage(msg);
                        ACache mCache = ACache.get(MyZWActivity.this);
                        mCache.put("weather", xml, 3 * ACache.TIME_HOUR);
                    }
                }else {
                    Message msg = handler.obtainMessage();
                    msg.what = 404;
                    handler.sendMessage(msg);
                }
            }else {
                Toast.makeText(MyZWActivity.this,"无网络连接",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            if(PhotoShopActivity.DLE) {
                MainActivity.mainActivity.finish();
                startActivity(new Intent(MyZWActivity.this, MainActivity.class));
            }
            MyZWActivity.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }



    /*
    查询数据库并计算年龄
     */
    public void ChaNl(){
        db=openOrCreateDatabase("mzw.db",MODE_PRIVATE,null);
        Cursor cursor= db.rawQuery("select * from mzw where name == '"+NAME+"'",null);//查询
        if (cursor!=null){
            while (cursor.moveToNext()){
                name.setText(NAME);
                title.setText(NAME);
                RIQI=cursor.getString(cursor.getColumnIndex("riqi"));
                xueming=cursor.getString(cursor.getColumnIndex("xueming"));
                SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd");
                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                DQRIQI = formatter.format(curDate);
                String str1 = RIQI;  //"yyyyMMdd"格式 如 20131022
                String str2 = DQRIQI;  //"yyyyMMdd"格式 如 20131022
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//输入日期的格式
                Date date1 = null;
                try {
                    date1 = simpleDateFormat.parse(str1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date date2 = null;
                try {
                    date2 = simpleDateFormat.parse(str2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                GregorianCalendar cal1 = new GregorianCalendar();
                GregorianCalendar cal2 = new GregorianCalendar();
                cal1.setTime(date1);
                cal2.setTime(date2);
                double dayCount = (cal2.getTimeInMillis()-cal1.getTimeInMillis())/(1000*3600*24);//从间隔毫秒变成间隔天数
                nianling.setText((int)dayCount+"天");
            }
            cursor.close();//释放游标
        }
        db.close();//关闭数据库


        zwDb=new ZwDb(this);
        db=zwDb.OpenZwdb();
        cursor= db.rawQuery("select * from zw where xueming == '"+xueming+"'",null);//查询
        if (cursor!=null){
            while (cursor.moveToNext()){
                guang=cursor.getString(cursor.getColumnIndex("guangzhao"));
                wd=cursor.getInt(cursor.getColumnIndex("wendud"));
                wg=cursor.getInt(cursor.getColumnIndex("wendug"));
            }
            cursor.close();//释放游标
        }
        db.close();//关闭数据库


    }

    // s删除提示弹出框
    private void showDialog() {
        new AlertDialog.Builder(this)
                .setTitle("警告")
                .setMessage("删除后无法恢复！是否继续删除？")
                .setPositiveButton("确认删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db = openOrCreateDatabase("mzw.db", MODE_PRIVATE, null);
                        db.execSQL("delete from mzw where name = '"+ NAME+"'");
                        db.close();
                        MainActivity.mainActivity.finish();
                        startActivity(new Intent(MyZWActivity.this, MainActivity.class));
                        MyZWActivity.this.finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setCancelable(false).show();

    }



    protected AlertDialog.Builder myBuilder(MyZWActivity dialogWindows) {
        final LayoutInflater inflater=this.getLayoutInflater();
        AlertDialog.Builder builder=new AlertDialog.Builder(dialogWindows);
        CustomView=inflater.inflate(R.layout.layout_arrangedescriebe, null);
        return builder.setView(CustomView);
    }



}

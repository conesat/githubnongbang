package nongbang.hg.nongbang;

import android.Manifest;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.squareup.picasso.Picasso;

import java.io.File;

import nongbang.hg.nongbang.MSQLite.ZwDb;
import nongbang.hg.nongbang.MyAdapter.ZyListAdapter;
import nongbang.hg.nongbang.StaticClass.TestValues;
import nongbang.hg.nongbang.tools.HttpThread;
import nongbang.hg.nongbang.util.Checknetwork;
import nongbang.hg.nongbang.util.UnZip;

/**
 * 登录界面
 */
public class LoadingActivity extends AppCompatActivity {
    private int time = 0;
    private CircleProgressBar mLineProgressBar;
    private ImageView imageView;
    private Button skip;
    private boolean SKIP = false;
    public static LoadingActivity loadingActivity;
    private SharedPreferences sp;
    private ZwDb zwDb;
    private SQLiteDatabase dbzw;
    private long webcunt, localcunt;
    private View CustomView;
    private LinearLayout pro, xinxi;
    private ProgressBar progressBar;
    private Button buttoncancle, buttonbreak, buttonsyn;
    private TextView newdata, jd;
    private Builder builder;
    private AlertDialog dialog;
    private android.support.v7.app.AlertDialog dialog1;
    private ZyListAdapter zyadapter;

    private String[] permissions = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_loading);
        loadingActivity = this;
        imageView = (ImageView) findViewById(R.id.loading_image);
        skip = (Button) findViewById(R.id.loading_but);

        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //   for (int x=0;x<permissions.length;x++) {
            // 检查该权限是否已经获取
            //int i = ContextCompat.checkSelfPermission(this, permissions[x]);
            int i = ContextCompat.checkSelfPermission(this, permissions[1]);
            int a = ContextCompat.checkSelfPermission(this, permissions[2]);//通讯录权限
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝.
            if (i != PackageManager.PERMISSION_GRANTED || a != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                startRequestPermission();
            }else {
                if (Checknetwork.isNetworkAvailable(this)) {
                    HttpThread httpThread = new HttpThread("getdatacunt");
                    httpThread.getDataCunt();
                } else {
                    new Thread(new MyThread()).start();
                }
            }
        }else {
            /*
            新修改部分
             */
            sp = getSharedPreferences("config", MODE_PRIVATE);
            String data = sp.getString("first", "");
            if (data.compareTo("") == 0) {
                File file = new File(Environment.getExternalStorageDirectory() + "/.nongbang");
                if (file.exists()) {
                    deleteDirWihtFile(file);
                }
                try {
                    file.mkdir();
                    file = new File(Environment.getExternalStorageDirectory() + "/.nongbang/images");
                    file.mkdir();
                    file = new File(Environment.getExternalStorageDirectory() + "/.nongbang/zwdata");
                    file.mkdir();
                    file = new File(Environment.getExternalStorageDirectory() + "/.nongbang/zwdata/images");
                    file.mkdir();
                    UnZip.unZip(this, "zwdata.zip", Environment.getExternalStorageDirectory() + "/.nongbang/zwdata/images", true);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("first", "no");
                    editor.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (Checknetwork.isNetworkAvailable(this)) {
                HttpThread httpThread = new HttpThread("getdatacunt");
                httpThread.getDataCunt();
            } else {
                new Thread(new MyThread()).start();
            }
        }
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SKIP = true;
            }
        });
        Picasso
                .with(LoadingActivity.this)
                .load("http://39.108.70.152/nongbang/ad/ad.png")
                .error(R.drawable.adhg)
                .placeholder(R.drawable.adhg)
                .into(imageView);
        mLineProgressBar = (CircleProgressBar) findViewById(R.id.line_progress);
        //testData();
        zwDb = new ZwDb(this);
        dbzw = zwDb.OpenZwdb();
        localcunt = allCaseNum();
        new TestValues();
    }


    //获取本地数据总数
    public long allCaseNum() {
        String sql = "select count(id) from zw";
        Cursor cursor = dbzw.rawQuery(sql, null);
        cursor.moveToFirst();
        long count = cursor.getLong(0);
        cursor.close();
        return count;
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

    public Handler handler = new Handler() { // handle
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mLineProgressBar.setProgress(time++);
                    break;
                case 101:
                    Log.i("2", msg.obj.toString());
                    if (!msg.obj.toString().equals("null")) {
                        String[] strings = msg.obj.toString().split("\\|");
                        ContentValues values = new ContentValues();
                        values.put("name", strings[1]);
                        values.put("xueming", strings[2]);
                        values.put("fenlei", strings[3]);
                        values.put("guangzhao", strings[4]);
                        values.put("wendud", strings[5]);
                        values.put("wendug", strings[6]);
                        values.put("turang", strings[7]);
                        values.put("feiliao", strings[8]);
                        values.put("r", strings[9]);
                        values.put("g", strings[10]);
                        values.put("b", strings[11]);
                        values.put("tijian", strings[12]);
                        values.put("jianjie", strings[13]);
                        values.put("leixing", strings[14]);
                        values.put("baike", strings[15]);
                        if (dbzw.insert("zw", null, values) != -1) {
                            progressBar.setProgress((int) (webcunt - localcunt));
                            jd.setText((int) (webcunt - localcunt) / progressBar.getMax() + "%");
                            if (webcunt > localcunt) {
                                HttpThread httpThread = new HttpThread("syndata");
                                httpThread.SynData(Long.toString(++localcunt));
                            } else {
                                dialog.dismiss();
                                new Thread(new MyThread()).start();
                            }
                        } else {
                            Toast.makeText(LoadingActivity.this, "同步失败1", Toast.LENGTH_SHORT).show();
                            new Thread(new MyThread()).start();
                        }
                    } else {
                        Toast.makeText(LoadingActivity.this, "同步失败2", Toast.LENGTH_SHORT).show();
                        new Thread(new MyThread()).start();
                    }
                    break;
                case 102:
                    webcunt = Long.parseLong(msg.obj.toString());
                    if (webcunt > localcunt) {
                        builder = myBuilder(LoadingActivity.this);
                        dialog = builder.show();
                        //点击屏幕外侧，dialog不消失
                        dialog.setCanceledOnTouchOutside(false);
                        xinxi = (LinearLayout) CustomView.findViewById(R.id.tb_msg);
                        newdata = (TextView) CustomView.findViewById(R.id.tb_xinxi);
                        buttoncancle = (Button) CustomView.findViewById(R.id.tb_xinxi_cancle);
                        buttonsyn = (Button) CustomView.findViewById(R.id.tb_xinxi_syn);
                        newdata.setText("服务器新增" + (webcunt - localcunt) + "条植物数据，是否同步到本地？");
                        buttoncancle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                new Thread(new MyThread()).start();
                            }
                        });
                        buttonsyn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pro = (LinearLayout) CustomView.findViewById(R.id.tb_pros);
                                xinxi.setVisibility(View.GONE);
                                pro.setVisibility(View.VISIBLE);
                                jd = (TextView) CustomView.findViewById(R.id.tb_jd);
                                progressBar = (ProgressBar) CustomView.findViewById(R.id.tb_progressBar);
                                progressBar.setMax((int) (webcunt - localcunt));
                                buttonbreak = (Button) CustomView.findViewById(R.id.tb_break);
                                buttonbreak.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        localcunt = webcunt;
                                    }
                                });
                                HttpThread httpThread = new HttpThread("syndata");
                                localcunt++;
                                httpThread.SynData(Long.toString(localcunt));
                            }
                        });

                    } else
                        new Thread(new MyThread()).start();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public class MyThread implements Runnable { // thread
        @Override
        public void run() {
            while (!SKIP) {
                if (time < 100) {
                    try {
                        Thread.sleep(40); // sleep 100ms
                        Message message = new Message();
                        message.what = 0;
                        handler.sendMessage(message);
                    } catch (Exception e) {
                    }
                } else
                    SKIP = true;
            }
            try {
                Thread.sleep(300); // sleep 100ms
                starApp();
            } catch (Exception e) {
            }
        }
    }

    public void starApp() {
        startActivity(new Intent(LoadingActivity.this, MainActivity.class));
        LoadingActivity.this.finish();
    }

    protected Builder myBuilder(LoadingActivity dialogWindows) {
        final LayoutInflater inflater = this.getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(dialogWindows);
        CustomView = inflater.inflate(R.layout.layout_getdata, null);
        return builder.setView(CustomView);
    }


    // 开始提交请求权限
    public void startRequestPermission() {
        ActivityCompat.requestPermissions(this, permissions, 321);
    }

    // 用户权限 申请 的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 321) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //第一次创建检测是否含有旧数据 将其重建
                // public void testData(){
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sp = getSharedPreferences("config", MODE_PRIVATE);
                    String data = sp.getString("first", "");
                    if (data.compareTo("") == 0) {
                        File file = new File(Environment.getExternalStorageDirectory() + "/.nongbang");
                        if (file.exists()) {
                            deleteDirWihtFile(file);
                        }
                        try {
                            file.mkdir();
                            file = new File(Environment.getExternalStorageDirectory() + "/.nongbang/images");
                            file.mkdir();
                            file = new File(Environment.getExternalStorageDirectory() + "/.nongbang/zwdata");
                            file.mkdir();
                            file = new File(Environment.getExternalStorageDirectory() + "/.nongbang/zwdata/images");
                            file.mkdir();
                            UnZip.unZip(this, "zwdata.zip", Environment.getExternalStorageDirectory() + "/.nongbang/zwdata/images", true);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("first", "no");
                            editor.commit();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    if (Checknetwork.isNetworkAvailable(this)) {
                        HttpThread httpThread = new HttpThread("getdatacunt");
                        httpThread.getDataCunt();
                    } else {
                        new Thread(new MyThread()).start();
                    }
                } else {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                        boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                        if (!b) {
                            // 用户还是想用我的 APP 的
                            // 提示用户去应用设置界面手动开启权限
                            showDialogTipUserGoToAppSettting();
                        } else
                            finish();
                        System.exit(0);
                    }
                }
            }
        }
    }
    // 提示用户去应用设置界面手动开启权限

    private void showDialogTipUserGoToAppSettting() {

        dialog1 = new android.support.v7.app.AlertDialog.Builder(this)
                .setTitle("权限不可用")
                .setMessage("请在-应用设置-权限-中，允许本软件使用存储权限来保存用户数据，以及访问网络")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 跳转到应用设置界面
                        goToAppSetting();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        System.exit(0);
                    }
                }).setCancelable(false).show();
    }

    // 跳转到当前应用的设置界面
    public void goToAppSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);

        startActivityForResult(intent, 123);
    }

    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 检查该权限是否已经获取
                int i = ContextCompat.checkSelfPermission(this, permissions[0]);
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                if (i != PackageManager.PERMISSION_GRANTED) {
                    // 提示用户应该去应用设置界面手动开启权限
                    showDialogTipUserGoToAppSettting();
                } else {
                    if (dialog1 != null && dialog1.isShowing()) {
                        dialog1.dismiss();
                    }
                }
            }
        }
    }
}

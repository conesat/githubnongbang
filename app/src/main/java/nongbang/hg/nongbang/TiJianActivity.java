package nongbang.hg.nongbang;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.FileNotFoundException;
import java.io.IOException;

import nongbang.hg.nongbang.camera.PermissionsChecker;
import nongbang.hg.nongbang.camera.PhotoUtil;

import static nongbang.hg.nongbang.camera.PhotoUtil.compressScale;

public class TiJianActivity extends AppCompatActivity implements View.OnClickListener {

    private PermissionsChecker mPermissionsChecker; // 权限检测器
    private static final int REQUEST_PERMISSION = 4;  //权限请求
    static final String[] PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    private PhotoUtil photoUtil;

    private Uri imageUri;
    private ImageView imageviewback;
    private Button paizhao, xuantu;
    private LinearLayout linearLayout;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tijian);
        imageviewback = (ImageView) findViewById(R.id.tjback);
        paizhao = (Button) findViewById(R.id.tjpaizaho);
        linearLayout = (LinearLayout) findViewById(R.id.tjyclayout);
        imageView = (ImageView) findViewById(R.id.tjiv);
        xuantu = (Button) findViewById(R.id.tjxuantu);
        xuantu.setOnClickListener(this);
        paizhao.setOnClickListener(this);
        imageviewback.setOnClickListener(this);
        mPermissionsChecker = new PermissionsChecker(this);
        photoUtil = new PhotoUtil(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tjback:
                TiJianActivity.this.finish();
                break;
            case R.id.tjpaizaho:
                //检查权限(6.0以上做权限判断)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
            case R.id.tjxuantu:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                        startPermissionsActivity();
                    } else {
                        //打开相册
                        photoUtil.callPhoto(1);
                    }
                } else {
                    photoUtil.callPhoto(1);
                }
                break;
        }
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_PERMISSION,
                PERMISSIONS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap;

// 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口  
        ContentResolver resolver = getContentResolver();
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {

                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    linearLayout.setVisibility(View.VISIBLE);
                    imageView.setImageBitmap(compressScale(bitmap)) ;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 1) {
                //从相册选择
                Uri originalUri = data.getData();
                if (originalUri !=null) {

                    try {
                        linearLayout.setVisibility(View.VISIBLE);
                        bitmap = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                        imageView.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }
        }

    }
}

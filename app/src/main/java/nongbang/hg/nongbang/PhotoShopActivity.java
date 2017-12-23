package nongbang.hg.nongbang;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import nongbang.hg.nongbang.view.ZoomImageView;

public class PhotoShopActivity extends AppCompatActivity implements View.OnClickListener{
    public static ArrayList<File> images;
    private ImageButton buttonbcak,delete;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    public static boolean DLE=false;
    public static String  NAME;
   // private int[] mImages={R.drawable.cs,R.drawable.cs,R.drawable.cs};
    private ImageView[] mImageViews=new ImageView[images.size()];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoshop);
        buttonbcak=(ImageButton)findViewById(R.id.psback);
        delete=(ImageButton)findViewById(R.id.ps_delete);
        delete.setOnClickListener(this);
        viewPager=(ViewPager)findViewById(R.id.psvp);
        pagerAdapter=new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ZoomImageView imageView=new ZoomImageView(getApplicationContext());
                Picasso
                        .with(PhotoShopActivity.this)
                        .load("file://"+String.valueOf(images.get(position)))
                        .into(imageView);
                container.addView(imageView);
                mImageViews[position]=imageView;
                return imageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(mImageViews[position]);
            }

            @Override
            public int getCount() {
                return images.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }
        };
        viewPager.setAdapter(pagerAdapter);
        buttonbcak.setOnClickListener(this);
        if ((this.getIntent().getExtras().getString("ACT")).compareTo("MyZWActivity")!=0){
            delete.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.psback:
                if (DLE) {
                    MyZWActivity.myZWActivity.finish();
                    Intent intent = new Intent(PhotoShopActivity.this, MyZWActivity.class);
                    intent.putExtra("name",NAME);
                    startActivityForResult(intent,303);
                    PhotoShopActivity.this.finish();
                }else {
                    PhotoShopActivity.this.finish();
                }
                break;
            case R.id.ps_delete:
                showDialog();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK) {
            if (DLE) {
                MyZWActivity.myZWActivity.finish();
                Intent intent = new Intent(PhotoShopActivity.this, MyZWActivity.class);
                intent.putExtra("name",NAME);
                startActivityForResult(intent,303);
                PhotoShopActivity.this.finish();
            }else {
                PhotoShopActivity.this.finish();
            }
        }
        return super.onKeyDown(keyCode, event);

    }

    // s删除提示弹出框
    private void showDialog() {
        new AlertDialog.Builder(this)
                .setTitle("警告")
                .setMessage("删除后无法恢复！是否继续删除？")
                .setPositiveButton("确认删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File file=images.get(viewPager.getCurrentItem());
                        if(file.exists()){
                            file.delete();
                            DLE=true;
                            Toast.makeText(PhotoShopActivity.this,"已删除",Toast.LENGTH_SHORT).show();
                            images.remove(viewPager.getCurrentItem());
                            pagerAdapter.notifyDataSetChanged();
                            viewPager.setAdapter(pagerAdapter);
                            if (images.size()==0){
                                MyZWActivity.myZWActivity.finish();
                                Intent intent = new Intent(PhotoShopActivity.this, MyZWActivity.class);
                                intent.putExtra("name",NAME);
                                startActivityForResult(intent,303);
                                PhotoShopActivity.this.finish();
                            }
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(PhotoShopActivity.this,"取消",Toast.LENGTH_SHORT).show();
                    }
                }).setCancelable(false).show();

    }
}

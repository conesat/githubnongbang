package nongbang.hg.nongbang;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import nongbang.hg.nongbang.view.NewDetailsTypeDialog;

public class NewDetailsActivity extends AppCompatActivity {

    private ImageView back;

    public TextView type;

    public static NewDetailsActivity newDetailsActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_new_details);

        back=(ImageView)findViewById(R.id.new_details_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewDetailsActivity.this.finish();
            }
        });

        type=(TextView)findViewById(R.id.new_details_type);
        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    NewDetailsTypeDialog selectDialog = new NewDetailsTypeDialog(NewDetailsActivity.this,R.style.dialog);//创建Dialog并设置样式主题
                    Window win = selectDialog.getWindow();
                    WindowManager.LayoutParams params = new WindowManager.LayoutParams();
                    params.y = -60;//设置y坐标
                    win.setAttributes(params);
                    selectDialog.setCanceledOnTouchOutside(true);//设置点击Dialog外部任意区域关闭Dialog
                    selectDialog.show();
            }
        });
        newDetailsActivity=this;
    }
}

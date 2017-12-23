package nongbang.hg.nongbang;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import nongbang.hg.nongbang.StaticClass.TestValues;

public class SquareDetailsActivity extends AppCompatActivity implements View.OnClickListener{
    TextView type;
    TextView start;
    TextView comment;
    TextView share;
    TextView title;
    TextView username;
    ImageView userico;
    ImageView img;
    ImageView back;
    Button commentbnt;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        position=this.getIntent().getExtras().getInt("postion");
        setContentView(R.layout.activity_square_details);
        InitView();

    }
    public void InitView(){
        userico=(ImageView)findViewById(R.id.detail_ico);
        start=(TextView)findViewById(R.id.detail_start);
        comment=(TextView)findViewById(R.id.detail_comment);
        share=(TextView)findViewById(R.id.detail_share);
        title=(TextView)findViewById(R.id.detail_title);
        type=(TextView)findViewById(R.id.detail_type);
        username=(TextView)findViewById(R.id.detail_name);
        img=(ImageView)findViewById(R.id.detail_img);
        back=(ImageView)findViewById(R.id.detail_back);
        commentbnt=(Button)findViewById(R.id.detail_commentbnt);
        username.setText(TestValues.SQUAREUSERNAME.get(position));
        type.setText(TestValues.SQUARETYPE.get(position));
        start.setText(TestValues.SQUARESTART.get(position));
        share.setText(TestValues.SQUARESHARE.get(position));

        back.setOnClickListener(this);
        commentbnt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.detail_back:
                SquareDetailsActivity.this.finish();
                break;
            case R.id.detail_commentbnt:
                Toast.makeText(SquareDetailsActivity.this,"请先登录",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}

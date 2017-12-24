package nongbang.hg.nongbang;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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
    ImageView startimg;
    EditText commentet;
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
        startimg=(ImageView)findViewById(R.id.detail_startimg);
        username.setText(TestValues.SQUAREUSERNAME.get(position));
        type.setText(TestValues.SQUARETYPE.get(position));
        start.setText(Integer.toString(TestValues.SQUARESTART.get(position)));
        share.setText(TestValues.SQUARESHARE.get(position));
        commentet=(EditText)findViewById(R.id.commentet);
        commentet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (commentet.getText().toString().compareTo("")==0){
                    commentbnt.setBackgroundResource(R.drawable.hui5);
                    commentbnt.setEnabled(false);
                }else {
                    commentbnt.setEnabled(true);
                    commentbnt.setBackgroundResource(R.drawable.bluebutton5);
                }
            }
        });
        back.setOnClickListener(this);
        commentbnt.setOnClickListener(this);
        commentbnt.setEnabled(false);


        if (TestValues.SQUAREISSTART.get(position)){;
            startimg.setImageResource(R.drawable.staron);
        }else {
            startimg.setImageResource(R.drawable.staroff);
        }
        startimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TestValues.SQUAREISSTART.get(position)){
                    TestValues.SQUARESTART.set(position,TestValues.SQUARESTART.get(position)-1);
                    start.setText(Integer.toString(TestValues.SQUARESTART.get(position)));
                    TestValues.SQUAREISSTART.set(position,false);
                    startimg.setImageResource(R.drawable.staroff);
                }else {
                    TestValues.SQUARESTART.set(position,TestValues.SQUARESTART.get(position)+1);
                    start.setText(Integer.toString(TestValues.SQUARESTART.get(position)));
                    TestValues.SQUAREISSTART.set(position,true);
                    startimg.setImageResource(R.drawable.staron);
                }
            }
        });

        Picasso
                .with(SquareDetailsActivity.this)
                .load(TestValues.SQUAREUSERICO.get(position))
                .into(userico);
        title.setText(TestValues.SQUARETITLE.get(position));

        if (TestValues.SQUAREIMG.get(position).compareTo("")!=0){
            img.setVisibility(View.VISIBLE);
            Picasso
                    .with(SquareDetailsActivity.this)
                    .load(TestValues.SQUAREIMG.get(position))
                    .into(img);
        }else {
            img.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.detail_back:
                SquareDetailsActivity.this.finish();
                break;
            case R.id.detail_commentbnt:
                Toast.makeText(SquareDetailsActivity.this,"已评论", Toast.LENGTH_SHORT).show();
                commentet.setText("");
                commentbnt.setBackgroundResource(R.drawable.hui5);
                commentbnt.setEnabled(false);
                break;
            case R.id.detail_img:

                break;
        }
    }
}

package nongbang.hg.nongbang.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import nongbang.hg.nongbang.NewDetailsActivity;
import nongbang.hg.nongbang.R;

/**
 * Created by Administrator on 2017-11-15.
 */

public class NewDetailsTypeDialog extends AlertDialog implements View.OnClickListener{

    Context context;
    private TextView type1,type2,type3,type4,type5,type6;

    public NewDetailsTypeDialog(Context context, int theme) {
        super(context, theme);
        this.context=context;
    }

    public NewDetailsTypeDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_choicetypedialog);
        type1=(TextView)findViewById(R.id.type1);
        type2=(TextView)findViewById(R.id.type2);
        type3=(TextView)findViewById(R.id.type3);
        type4=(TextView)findViewById(R.id.type4);
        type5=(TextView)findViewById(R.id.type5);
        type6=(TextView)findViewById(R.id.type6);
        type1.setOnClickListener(this);
        type2.setOnClickListener(this);
        type3.setOnClickListener(this);
        type4.setOnClickListener(this);
        type5.setOnClickListener(this);
        type6.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.type1:
                this.dismiss();
                NewDetailsActivity.newDetailsActivity.type.setText("#"+type1.getText()+"#");
                break;
            case R.id.type2:
                this.dismiss();
                NewDetailsActivity.newDetailsActivity.type.setText("#"+type2.getText()+"#");
                break;
            case R.id.type3:
                this.dismiss();
                NewDetailsActivity.newDetailsActivity.type.setText("#"+type3.getText()+"#");
                break;
            case R.id.type4:
                this.dismiss();
                NewDetailsActivity.newDetailsActivity.type.setText("#"+type4.getText()+"#");
                break;
            case R.id.type5:
                this.dismiss();
                NewDetailsActivity.newDetailsActivity.type.setText("#"+type5.getText()+"#");
                break;
            case R.id.type6:
                this.dismiss();
                NewDetailsActivity.newDetailsActivity.type.setText("#"+type6.getText()+"#");
                break;
        }
    }
}

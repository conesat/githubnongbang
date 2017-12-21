package nongbang.hg.nongbang.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import nongbang.hg.nongbang.ImageManipulationsActivity;
import nongbang.hg.nongbang.R;

/**
 * Created by Administrator on 2017-11-15.
 */

public class ScanDialog extends AlertDialog {

    Context context;

    public ScanDialog(Context context, int theme) {
        super(context, theme);
        this.context=context;
    }

    public ScanDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_scandialog);
        ImageView flower,leaf;
        flower=(ImageView)findViewById(R.id.scanflower);
        leaf=(ImageView)findViewById(R.id.scanleaf) ;
        leaf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                context.startActivity(new Intent(context,ImageManipulationsActivity.class));
            }
        });
        flower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                context.startActivity(new Intent(context,ImageManipulationsActivity.class));
               // Toast.makeText(context,"123",Toast.LENGTH_SHORT).show();
            }
        });
    }

}

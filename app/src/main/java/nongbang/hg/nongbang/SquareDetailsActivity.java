package nongbang.hg.nongbang;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

public class SquareDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_square_details);
    }
}

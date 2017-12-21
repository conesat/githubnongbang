package nongbang.hg.nongbang.tools;

import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;

import nongbang.hg.nongbang.R;

/**
 * Created by guiming on 2017/11/8.
 */

public class Vibration {

    Vibrator vibrator;
    public Vibration(Vibrator vibrator){
        this.vibrator=vibrator;
    }
    public void beginVibration() {
        vibrator.vibrate(new long[]{400, 300}, 0);
    }
    public void closeVibration(){
        vibrator.cancel();
    }
}

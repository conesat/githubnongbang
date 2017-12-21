package nongbang.hg.nongbang.MSQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import nongbang.hg.nongbang.R;

/**
 * Created by Administrator on 2017-07-27.
 */

public class ZwDb {
    Context context;
    public ZwDb(Context context){
        this.context=context;
    }
    public SQLiteDatabase OpenZwdb(){

        String dbPath = "/data"
                + Environment.getDataDirectory().getAbsolutePath()
                + "/nongbang.hg.nongbang/zwdata.db";// 要把你Raw文件的db保存到sdcard中
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            if (!(new File(dbPath).exists())) {
                is = context.getResources().openRawResource(R.raw.zwdata); // 你Raw的那个db索引
                fos = new FileOutputStream(dbPath);
                byte[] buffer = new byte[1024];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }

            }

        } catch (Exception e) {
            Log.e("DB_ERROR", "数据文件读写失败");
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    Log.e("DB_ERROR", "数据文件读写失败");
                }
            }

            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    Log.e("DB_ERROR", "数据文件读写失败");
                }
            }
        }

        return SQLiteDatabase.openOrCreateDatabase(dbPath, null);// 这里可以执行你的操作了
    }
}

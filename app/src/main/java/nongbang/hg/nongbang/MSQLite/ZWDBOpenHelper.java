package nongbang.hg.nongbang.MSQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 添加到作物列表的数据库
 * Created by Administrator on 2017-07-22.
 */

public class ZWDBOpenHelper extends SQLiteOpenHelper {


    public ZWDBOpenHelper(Context context, String name) {
        super(context, name, null, 1);
    }

    //首次创建
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists mzw(_id integer primary key autoincrement,name text UNIQUE,riqi text not null,say text not null,xueming text not null)");
    }

    //版本更变
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

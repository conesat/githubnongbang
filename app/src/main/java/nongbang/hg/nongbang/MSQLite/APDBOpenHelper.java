package nongbang.hg.nongbang.MSQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 添加到安排的数据库
 * Created by Administrator on 2017-07-22.
 */

public class APDBOpenHelper extends SQLiteOpenHelper {

    public APDBOpenHelper(Context context, String name) {
        super(context, name, null, 1);
    }

    //首次创建
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists map(_id integer primary key AUTOINCREMENT,name text not null,title text not null,describe text not null,date text not null,time text not null,repeat integer not null)");
    }

    //版本更变
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

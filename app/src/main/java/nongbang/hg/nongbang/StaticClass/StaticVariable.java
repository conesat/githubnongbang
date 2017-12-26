package nongbang.hg.nongbang.StaticClass;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-07-22.
 */

public  class StaticVariable {
    public static String MyUrl="http://39.108.70.152";
   // public static String MyUrl="http://192.168.1.101:8888";//测试地址
    public static Uri IMAGEURI;
    public static String APRTIME;
    public static String APRQREPEAT;
    public static String APRQDATE;
    public static List<Map<String,Object>> ALLLIST=new ArrayList<Map<String, Object>>();
    public static List<Map<String,Object>> HUAHUILIST=new ArrayList<Map<String, Object>>();
    public static List<Map<String,Object>> CAOLIST=new ArrayList<Map<String, Object>>();
    public static List<Map<String,Object>> SHULIST=new ArrayList<Map<String, Object>>();
    public static List<Map<String,Object>> ZHUZHILIST=new ArrayList<Map<String, Object>>();
}

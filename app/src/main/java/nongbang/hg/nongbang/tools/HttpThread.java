package nongbang.hg.nongbang.tools;

import android.os.Message;

import java.io.IOException;

import nongbang.hg.nongbang.LoadingActivity;
import nongbang.hg.nongbang.MyZWActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by Administrator on 2017-08-30.
 */
public class HttpThread extends Thread{
    public static  String HTTPURL;
    String type;
    /**
    * xinxi 组成（学名/今日低温/今日高温/今日天气/明日低温/明日高温/明日天气）
     **/
    public HttpThread(String type){
        this.type=type;
    }

    //同步数据
    public void getDataCunt(){
        HTTPURL="http://39.108.70.152/NongBangWeb/myservlet?type="+type;
        this.start();
    }

    //同步数据
    public void SynData(String id){
        HTTPURL="http://39.108.70.152/NongBangWeb/myservlet?type="+type+"&id="+id;
        this.start();
    }


    //获取意见
    public void GetYiJian(String xinxi){
        HTTPURL="http://39.108.70.152/NongBangWeb/myservlet?type="+type+"&xinxi="+xinxi;
        this.start();
    }

    public void DoGet(String url){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response.isSuccessful()) {
            Message message = new Message();
            try {
                message.obj=response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            switch (type) {
                case "getyijian":
                    message.what = 406;
                    MyZWActivity.myZWActivity.handler.sendMessage(message);
                    break;
                case "syndata":
                    message.what = 101;
                    LoadingActivity.loadingActivity.handler.sendMessage(message);
                    break;
                case "getdatacunt":
                    message.what = 102;
                    LoadingActivity.loadingActivity.handler.sendMessage(message);
                    break;

            }

        }
    }

    @Override
    public void run() {
        super.run();
        DoGet(HTTPURL);
//        switch (type){
//            case "getyijian":
//
//                break;
//        }
    }
}

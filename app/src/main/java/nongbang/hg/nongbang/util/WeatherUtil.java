package nongbang.hg.nongbang.util;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 该工具类 1、实现网络数据访问（天气数据） 2、实现XML文件的解析
 *
 * @author lampsss
 *
 */
public class WeatherUtil {

	private static String UU = "http://www.webxml.com.cn/WebServices/WeatherWebService.asmx/getWeatherbyCityName";

	/**
	 * 实现访问网络上的天气数据，并返回
	 *
	 * @param city
	 * @return
	 * @throws RuntimeException
	 */
	public static String getWeather(String city) throws RuntimeException {
		String xml = null;
		try {
			OkHttpClient okHttpClient = new OkHttpClient();

			RequestBody body = new FormBody.Builder()
					.add("theCityName", city)
    				.build();

			Request request = new Request.Builder()
					.url(UU)
					.post(body)
					.build();

			Call call = okHttpClient.newCall(request);
			try {
				Response response = call.execute();
				xml=response.body().string();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return xml;
	}

	/**
	 * 解析网络上返回XML文件数据
	 * @param xml
	 * @return
	 * @throws RuntimeException
	 */
	public static List<String> parseXML(String xml)throws RuntimeException{
		List<String> weatherDatas = new ArrayList<String>();

		//创建解析XML文件的解析器
		XmlPullParser pull = Xml.newPullParser();
		StringReader in = new StringReader(xml);
		try {
			pull.setInput(in);
			//获取事件类型
			int eventType = pull.getEventType();
			while(eventType!=XmlPullParser.END_DOCUMENT){
				switch (eventType) {
					case XmlPullParser.START_TAG:
						String tag = pull.getName();
						if("string".equalsIgnoreCase(tag)){
							weatherDatas.add(pull.nextText());
						}
						break;
					default:
						break;
				}
				eventType = pull.next();//获取下一个事件类型
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
		return weatherDatas;
	}
}

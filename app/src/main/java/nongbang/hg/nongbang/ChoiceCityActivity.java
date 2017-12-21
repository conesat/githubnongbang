package nongbang.hg.nongbang;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nongbang.hg.nongbang.util.ChoiceCityUtils;
import nongbang.hg.nongbang.util.ChoiceCityUtils.WebServiceCallBack;
import nongbang.hg.nongbang.util.ProgressDialogUtils;

public class ChoiceCityActivity extends AppCompatActivity {
	private String WEB_SERVICE_URL = "http://www.webxml.com.cn/WebServices/WeatherWebService.asmx";
	// 命名空间
	private String NAMESPACE = "http://WebXml.com.cn/";
	private List<String> provinceList = new ArrayList<String>(); // 中国省份名称列表
	private String methodNameProvince = "getSupportProvince"; // 调用的方法名
	private List<String> cityStringList; // 城市列表
	private String methodNameCiTy = "getSupportCity"; // 调用的方法名
	private ImageButton back;
	private TextView textView;
	private int D=0;
    String Province=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_choicecity);
		back= (ImageButton) findViewById(R.id.choicecity_back);
		textView=(TextView)findViewById(R.id.choicecity_tv) ;
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (D==0){
					ChoiceCityActivity.this.finish();
				}else {
					textView.setText("中国");
					province();
				}
			}
		});
		province();
	}

	private void province() {
		D=0;
		final ListView mProvinceList = (ListView) findViewById(R.id.province_list);
		// 显示进度条
		ProgressDialogUtils.showProgressDialog(this, "数据加载中...");
		ChoiceCityUtils.WEB_SERVICE_URL = this.WEB_SERVICE_URL;
		ChoiceCityUtils.NAMESPACE = this.NAMESPACE;
		// 通过工具类调用WebService接口
		ChoiceCityUtils.CallWebService(methodNameProvince, null, new WebServiceCallBack() {
			// WebService接口返回的数据回调到这个方法中
			@Override
			public void CallBack(SoapObject result) {
				// 关闭进度条
				ProgressDialogUtils.dismissProgressDialog();
				if (result != null) {
					provinceList = ParseSoapObject(result,methodNameProvince);
					mProvinceList.setAdapter(new ArrayAdapter<String>(
							ChoiceCityActivity.this,
							android.R.layout.simple_list_item_1, provinceList));
				} else {
					Toast.makeText(ChoiceCityActivity.this, "获取数据错误",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		// 点击事件
		mProvinceList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
                Province=provinceList.get(position);
				textView.setText(Province);
				city(Province);
			}
		});
	}

    private void city(String province) {
		D=1;
        final ListView mCityList = (ListView) findViewById(R.id.province_list);
        // 显示进度条
        ProgressDialogUtils.showProgressDialog(this, "数据加载中...");
        // 添加参数
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("byProvinceName", province);
        ChoiceCityUtils.CallWebService(methodNameCiTy, properties,
                new WebServiceCallBack() {
                    @Override
                    public void CallBack(SoapObject result) {
                        ProgressDialogUtils.dismissProgressDialog();
                        if (result != null) {
                            cityStringList = ParseSoapObject(result,methodNameCiTy);
                            mCityList.setAdapter(new ArrayAdapter<String>(
                                    ChoiceCityActivity.this,
                                    android.R.layout.simple_list_item_1,
                                    cityStringList));
                        } else {
                            Toast.makeText(ChoiceCityActivity.this,
                                    "获取数据错误", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });

        mCityList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
				SharedPreferences sp;
				sp = getSharedPreferences("config", MODE_PRIVATE);
				SharedPreferences.Editor editor = sp.edit();
				editor.putString("city", cityStringList.get(position));
				editor.commit();
				ChoiceCityActivity.this.finish();
            }
        });
    }


	/**
	 * 解析SoapObject对象 - 省份数据
	 *
	 * @param result
	 * @return
	 */
	private List<String> ParseSoapObject(SoapObject result, String methodName) {
		List<String> list = new ArrayList<String>();
		SoapObject provinceSoapObject = (SoapObject) result
				.getProperty(methodName+"Result");
		if (provinceSoapObject == null) {
			return null;
		}
		for (int i = 0; i < provinceSoapObject.getPropertyCount(); i++) {
			String cityString = provinceSoapObject.getProperty(i).toString();
			if(methodName.equals("getSupportCity"))
				list.add(cityString.substring(0, cityString.indexOf("(")).trim());
			else
				list.add(cityString);
		}

		return list;
	}



}

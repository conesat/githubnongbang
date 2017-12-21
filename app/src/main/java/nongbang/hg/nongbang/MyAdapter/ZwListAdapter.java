package nongbang.hg.nongbang.MyAdapter;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

import nongbang.hg.nongbang.R;

/**
 * Created by Administrator on 2017/7/17.
 */

public class ZwListAdapter extends BaseAdapter {

    private List<Map<String,Object>> allvalue;

    private Context cotx;

    public ZwListAdapter(List<Map<String,Object>> list,Context context){
        allvalue=list;
        cotx=context;
    }

    @Override
    public int getCount() {
        return allvalue.size();
    }

    @Override
    public Object getItem(int position) {
        return allvalue.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            convertView= LayoutInflater.from(cotx).inflate(R.layout.zwlist_layout,null);
            holder = new ViewHolder();
            holder.mc=(TextView)convertView.findViewById(R.id.mingcheng);
            holder.xm=(TextView)convertView.findViewById(R.id.xueming);
            holder.fl=(TextView)convertView.findViewById(R.id.fenlei);
            holder.imageView=(ImageView)convertView.findViewById(R.id.zwlist_iv);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        Map<String,Object> map=allvalue.get(position);
        holder.mc.setText(map.get("mc").toString());
        holder.xm.setText("学名:"+map.get("xm").toString());
        holder.fl.setText("分类:"+map.get("fl").toString());
        Picasso
                .with(cotx)
                .load("file://"+Environment.getExternalStorageDirectory() + "/.nongbang/zwdata/images/"+map.get("xm").toString()+"/j1.jpg")
                .resize(300,300)
                .error(R.drawable.zanwei)
                .placeholder(R.drawable.zanwei)
                .into( holder.imageView);
        return convertView;
    }

    public void onDataChange(List<Map<String, Object>> list) {
        this.allvalue=list;
        this.notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView mc;
        TextView fl;
        TextView xm;
        ImageView imageView;
    }

    public void updateView(List<Map<String,Object>> nowList)
    {
        this.allvalue = nowList;
        this.notifyDataSetChanged();//强制动态刷新数据进而调用getView方法
    }
}

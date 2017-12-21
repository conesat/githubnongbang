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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nongbang.hg.nongbang.R;
import nongbang.hg.nongbang.util.FindFloder;

import static nongbang.hg.nongbang.R.id.zyiv;

/**
 * Created by Administrator on 2017/7/17.
 */

public class FindZyListAdapter extends BaseAdapter {

    private List<Map<String,Object>> allvalue;

    private Context cotx;

    public FindZyListAdapter(List<Map<String,Object>> list, Context context){
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

    private ArrayList<File> images;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            convertView= LayoutInflater.from(cotx).inflate(R.layout.zylist_layout,null);
            holder = new ViewHolder();
            holder.mc=(TextView)convertView.findViewById(R.id.zymingzi);
            holder.say=(TextView)convertView.findViewById(R.id.zysay);
            holder.zyiv=(ImageView)convertView.findViewById(zyiv);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        Map<String,Object> map=allvalue.get(position);
        holder.mc.setText(map.get("mc").toString());
        holder.say.setText(map.get("say").toString());
        if ((images= FindFloder.FindFromFloder(Environment.getExternalStorageDirectory()+"/.nongbang/images"+"/"+map.get("mc").toString())).size()>0){
            Picasso
                    .with(cotx)
                    .load("file://"+String.valueOf(images.get(images.size()-1)))
                    .error(R.drawable.zanwei)
                    .placeholder(R.drawable.zanwei)
                    .into( holder.zyiv);
        }else {
            Picasso
                    .with(cotx)
                    .load("file://"+Environment.getExternalStorageDirectory()+"/.nongbang/images"+"/"+map.get("mc").toString()+"noImage/no.jpg")
                    .resize(300,300)
                    .error(R.drawable.zanwei)
                    .placeholder(R.drawable.zanwei)
                    .into( holder.zyiv);
        }
        return convertView;
    }
    static class ViewHolder {
        TextView mc;
        TextView say;
        ImageView zyiv;
    }


    public void onDataChange(List<Map<String, Object>> list) {
        this.allvalue=list;
        this.notifyDataSetChanged();
    }
}

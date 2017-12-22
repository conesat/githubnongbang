package nongbang.hg.nongbang.MyAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import nongbang.hg.nongbang.R;

/**
 * Created by Administrator on 2017-12-22.
 */

public class SquarelistAdapter extends BaseAdapter {

    private List<Map<String,Object>> alllist;
    private Context cotx;
    public SquarelistAdapter(List<Map<String,Object>> list, Context context){
        alllist=list;
        cotx=context;
    }

    @Override
    public int getCount() {
        return alllist.size();
    }

    @Override
    public Object getItem(int position) {
        return alllist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            convertView= LayoutInflater.from(cotx).inflate(R.layout.squarelist_layout,null);
            holder = new SquarelistAdapter.ViewHolder();

            convertView.setTag(holder);
        }else{
            holder = (SquarelistAdapter.ViewHolder)convertView.getTag();
        }
        return convertView;
    }

    static class ViewHolder {
        TextView title;
        TextView time;
        TextView repeat;
        ImageView delete;
    }
}

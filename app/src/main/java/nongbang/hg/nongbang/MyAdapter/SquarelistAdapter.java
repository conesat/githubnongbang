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
import nongbang.hg.nongbang.StaticClass.TestValues;

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
            holder.userico=(ImageView)convertView.findViewById(R.id.user_ico);
            holder.start=(TextView)convertView.findViewById(R.id.square_start);
            holder.comment=(TextView)convertView.findViewById(R.id.square_comment);
            holder.share=(TextView)convertView.findViewById(R.id.square_share);
            holder.title=(TextView)convertView.findViewById(R.id.square_title);
            holder.type=(TextView)convertView.findViewById(R.id.square_type);
            holder.username=(TextView)convertView.findViewById(R.id.user_name);
            holder.img=(ImageView)convertView.findViewById(R.id.square_img);
            convertView.setTag(holder);
        }else{
            holder = (SquarelistAdapter.ViewHolder)convertView.getTag();
        }
        holder.username.setText(TestValues.SQUAREUSERNAME.get(position));
        holder.type.setText(TestValues.SQUARETYPE.get(position));
        holder.start.setText(TestValues.SQUARESTART.get(position));
        holder.share.setText(TestValues.SQUARESHARE.get(position));
        return convertView;
    }

    static class ViewHolder {
        TextView type;
        TextView start;
        TextView comment;
        TextView share;
        TextView title;
        TextView username;
        ImageView userico;
        ImageView img;
    }
}

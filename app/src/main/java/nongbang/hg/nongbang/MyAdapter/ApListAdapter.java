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

import nongbang.hg.nongbang.MyZWActivity;
import nongbang.hg.nongbang.R;

/**
 * Created by Administrator on 2017/7/17.
 */

public class ApListAdapter extends BaseAdapter {

    private List<Map<String,Object>> allvalue;

    private DeleteApListener deleteAp;
    private Context cotx;

    public ApListAdapter(List<Map<String,Object>> list, Context context){
        allvalue=list;
        cotx=context;
    }

    public void setDeleteApListener(DeleteApListener deleteAp){
        this.deleteAp=deleteAp;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            convertView= LayoutInflater.from(cotx).inflate(R.layout.layout_anpaiitem,null);
            holder = new ViewHolder();
            holder.title=(TextView)convertView.findViewById(R.id.anpai_title);
            holder.time=(TextView)convertView.findViewById(R.id.anpai_time);
            holder.repeat=(TextView)convertView.findViewById(R.id.anpai_repeat);
            holder.delete=(ImageView) convertView.findViewById(R.id.anpai_delete);
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteAp.delete(position);
                }
            });
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        Map<String,Object> map=allvalue.get(position);
        holder.title.setText(map.get("title").toString());
        String stringtime=map.get("time").toString();
        String[] stringtimes=stringtime.split(":");
        if(stringtimes[1].toCharArray().length==1)
        {
            stringtimes[1]="0"+stringtimes[1];
        }
        if(stringtimes[0].toCharArray().length==1)
        {
            stringtimes[0]="0"+stringtimes[0];
        }
        holder.time.setText(stringtimes[0]+":"+stringtimes[1]);
        if (map.get("repeat").toString().compareTo("")==0 && map.get("date").toString().compareTo("")!=0)
              holder.repeat.setText(map.get("date").toString());
        if (map.get("repeat").toString().compareTo("")!=0 && map.get("date").toString().compareTo("")==0) {
            String[] strings=map.get("repeat").toString().split("/");
            String s=strings[0];
            for (int i=1;i<strings.length;i++)
                s=s+" "+strings[i];
                holder.repeat.setText("每周"+s+"重复提醒");
        }
        return convertView;
    }

    public void onDataChange(List<Map<String, Object>> list) {
        this.allvalue=list;
        this.notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView title;
        TextView time;
        TextView repeat;
        ImageView delete;
    }

    public void updateView(List<Map<String,Object>> nowList)
    {
        this.allvalue = nowList;
        this.notifyDataSetChanged();//强制动态刷新数据进而调用getView方法
    }


    //回调接口通知Activity删除安排
    public interface DeleteApListener{
        public void delete(int p);
    }
}

package com.foxconn.rfid.theowner.activity.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.foxconn.rfid.theowner.R;
import com.foxconn.rfid.theowner.model.EventBusMsgPush;
import com.yzh.rfidbike.app.response.InsuranceMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by appadmin on 2016/12/24.
 */


public class AdapterBikeInsurance extends BaseAdapter{


    private List<EventBusMsgPush> datas;
    private Context context;

    private OnDeleteClickListener onDeleteClickListener;
    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }


    //    private Map<Integer, String> cacheMap;//缓存当前checkbox的选中状态

    public AdapterBikeInsurance(Context context) {
        super();
        this.context = context;
        this.datas = new ArrayList();
//        this.cacheMap = new HashMap<Integer, String>();
    }

    public void setDatas(List<EventBusMsgPush> datas){
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    //	public void addDatas(List<ContactEntity> datas){
    //		datas.addAll(datas);
    //		this.notifyDataSetChanged();
    //	}

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if(convertView!=null){
            holder = (ViewHolder) convertView.getTag();
        }else {
            holder = new ViewHolder();
            convertView  = LayoutInflater.from(context).inflate(R.layout.item_sysmessage_lv, null);
            holder.tv_safety_name = (TextView) convertView.findViewById(R.id.tv_safety_name);
            holder.tv_safety_date_time = (TextView) convertView.findViewById(R.id.tv_safety_date_time);
            holder.tv_safety_addr = (TextView) convertView.findViewById(R.id.tv_safety_addr);
            holder.iv_safety_marks = (ImageView) convertView.findViewById(R.id.iv_safety_marks);
            holder.iv_safety_delete = (ImageView) convertView.findViewById(R.id.msg_safety_delete);
            convertView.setTag(holder);

            holder.iv_safety_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    datas.remove(position);
//                    AdapterSystemMessage.this.notifyDataSetChanged();

                    if (onDeleteClickListener != null) {
                        onDeleteClickListener.onClickListen(view,position);
                    }
                }
            });
            //设置checkbox的监听
            //            holder.box.setOnCheckedChangeListener(this);
        }
        //得到checkbox对象 -- 保证当前的checkbox里的tag是当前所在的item的下标
//        holder.box.setTag(position);


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final String time = simpleDateFormat.format(datas.get(position).getCreateDate());
        holder.tv_safety_date_time.setText(time);
        holder.tv_safety_name.setText(datas.get(position).getMsgTitle());
        holder.tv_safety_addr.setText(datas.get(position).getMsgContent());

        return convertView;
    }

    class ViewHolder{
        ImageView iv_safety_marks,iv_safety_delete;
        TextView tv_safety_name,tv_safety_date_time,tv_safety_addr;

    }

    	public interface OnDeleteClickListener{
    		void onClickListen(View view, int position);
    	}

//    @Override
//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        int position = (Integer) buttonView.getTag();
//        //当checkbox被选中时，把相应的下标和数据缓存进map中
//        if(isChecked){
//            cacheMap.put(position, datas.get(position));
//
//
//        } else {
//            //如果取消选中，则从map中移除该数据
//            cacheMap.remove(position);
//        }
//    }

    /**
     * 返回被选中的数据
     * @return
     */
//    public Collection<String> getCheckedDatas(){
//        Collection<String> collection = cacheMap.values();
//        return collection;
//    }
//
//
//    public void setAllCheckBox(Boolean bool){
//        if (bool){
//            for (int i = 0; i < datas.size(); i++) {
//                cacheMap.put(i,datas.get(i));
//            }
//            this.notifyDataSetChanged();
//        }else {
//            cacheMap.clear();
//            this.notifyDataSetChanged();
//        }
//
//    }






}

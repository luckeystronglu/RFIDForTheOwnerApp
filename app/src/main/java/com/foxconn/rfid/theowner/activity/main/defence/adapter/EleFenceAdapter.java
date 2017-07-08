/**
 *
 */
package com.foxconn.rfid.theowner.activity.main.defence.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.foxconn.rfid.theowner.R;
import com.foxconn.rfid.theowner.activity.main.defence.EleFenceEditActivity;
import com.foxconn.rfid.theowner.model.Bike;
import com.foxconn.rfid.theowner.util.string.DateUtils;
import com.yzh.rfidbike.app.response.GetBikeDefenceResponse;

import java.util.List;


/**
 * @author WT00111
 */
@SuppressLint("InflateParams")
public class EleFenceAdapter extends BaseAdapter {
    private Context context;
    private Bike bike;
    private int selectItem = -1;//选中项

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }

    //	private OnItemClickListener onItemClickListener;
    //
    //	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
    //		this.onItemClickListener = onItemClickListener;
    //	}

    private List<GetBikeDefenceResponse.DefenceMessage> listData;

    public EleFenceAdapter(Context context, List<GetBikeDefenceResponse.DefenceMessage> list, Bike bike) {
        this.context = context;
        this.bike = bike;
        listData = list;
    }


    @Override
    public int getCount() {
        return listData.size();
    }


    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_elefence_item, parent, false);
            holder = new ViewHolder();
            holder.tvTag = (TextView) convertView.findViewById(R.id.tv_tag);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tvEdit = (TextView) convertView.findViewById(R.id.tv_edit);
            holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvTag.setText(String.valueOf(position + 1));

        if (position == selectItem) {
            holder.tvTag.setBackgroundResource(R.drawable.shape_label_select_blue);
            holder.tvTitle.setTextColor(Color.parseColor("#1d72b1"));

        } else {
            holder.tvTag.setBackgroundResource(R.drawable.shape_label_orange);
            holder.tvTitle.setTextColor(Color.BLACK);
        }
        holder.tvDate.setText(DateUtils.getStringFromLong(listData.get(position).getCreateDate(), "yyyy-MM-dd"));
        holder.tvTitle.setText(listData.get(position).getName());

        holder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //				if (onItemClickListener != null) {
                //					onItemClickListener.onClickListen(view,position);
                //				}
                Intent intent = new Intent(context, EleFenceEditActivity.class);
                intent.putExtra("entity", listData.get(position));
                intent.putExtra("position", position);
                intent.putExtra("BIKE", bike);
                context.startActivity(intent);

            }
        });
        return convertView;
    }

    public final class ViewHolder {
        public TextView tvTitle;
        public TextView tvDate;
        public TextView tvEdit;
        public TextView tvTag;
    }

    //	public interface OnItemClickListener{
    //		void onClickListen(View view, int position);
    //	}

}

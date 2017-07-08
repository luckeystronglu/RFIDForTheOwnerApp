package com.foxconn.rfid.theowner.activity.main.adapter;

/**
 * Created by F1027734 on 2016-12-10.
 */

        import java.util.ArrayList;
        import java.util.List;

        import com.foxconn.rfid.theowner.R;
        import com.foxconn.rfid.theowner.model.Bike;
        import com.nostra13.universalimageloader.core.ImageLoader;
        import com.yzh.rfidbike.app.response.GetBikesResponse;

        import android.content.Context;
        import android.content.Intent;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.ImageView;
        import android.widget.ImageView.ScaleType;
        import android.widget.LinearLayout;
        import android.widget.LinearLayout.LayoutParams;
        import android.widget.TextView;
        import de.greenrobot.event.EventBus;

/**
 * ListAdapter
 *
 * @author Tom Xu
 */
public class BikeAdapter extends BaseAdapter {

    private Context mContext;
    private List<Bike> mDataList;
    private LayoutInflater mLayoutInflater;


    public BikeAdapter(Context context, List<Bike> data) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.mDataList = data;

    }



    public void setmDataList(List<Bike> data) {

        this.mDataList = data;
    }

    @Override
    public int getViewTypeCount() {
        // TODO Auto-generated method stub
        return 1;

    }

    @Override
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getCount() {
        return mDataList.size();// 返回数组的长度
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /* 书中详细解释该方法 */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //
        ViewHolderComment holder0 = new ViewHolderComment();
        if (convertView == null) {
				/* 得到各个控件的对象 */
            convertView = mLayoutInflater.inflate(
                    R.layout.list_bike_item, null);

            holder0.tvBikeName = (TextView) convertView
                    .findViewById(R.id.tv_title);
            convertView.setTag(holder0);
        } else {
            holder0 = (ViewHolderComment) convertView.getTag();// 取出ViewHolder对象
        }

        final Bike bikeMessage = mDataList.get(position);
        holder0.tvBikeName.setText(bikeMessage.getPlateNumber());
        return convertView;
    }

    public final class ViewHolderComment {
        public TextView tvBikeName;
    }


}

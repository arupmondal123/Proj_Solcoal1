package com.at.solcoal.adapter;

import java.util.List;

import com.at.solcoal.R;
import com.at.solcoal.activity.LoginActivity;
import com.at.solcoal.model.MyChatData;
import com.at.solcoal.utility.NI;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by anjan on 09-04-2016.
 */
@SuppressLint("ViewHolder")
public class MyChatAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<MyChatData> myChatDataList;
    private Activity activity = null;
    private Context context	= null;
    private String userId = null;

    public MyChatAdapter(Activity activity) {
        mInflater = activity.getLayoutInflater();
        this.activity = activity;
        this.context = activity.getApplicationContext();
    }

    public void addMyChatData(List<MyChatData> dataList) {
        myChatDataList = dataList;
    }

    @Override
    public int getCount() {
        return myChatDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return myChatDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        int res = R.layout.act_user_chat_entry;
        /* change */ int res = R.layout.a___act_user_chat_entry;
		
        convertView = mInflater.inflate(res, parent, false);

        final MyChatData myChatData = myChatDataList.get(position);
        TextView otherUserId = (TextView) convertView.findViewById(R.id.tv_chat_seller_id);
        otherUserId.setText(myChatData.getOtherUserId());
        TextView name_initial = (TextView) convertView.findViewById(R.id.name_initial);
        name_initial.setText(NI.getInitial(myChatData.getOtherUserId()));

        /*
        HCHAT h = new HCHAT();
        h.rl_layout = (RelativeLayout) convertView.findViewById(R.id.rl_my_chat);
        */

        View.OnClickListener onClickBrowse = new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startChat(myChatData);
            }
        };
        ((RelativeLayout) convertView.findViewById(R.id.rl_my_chat)).setOnClickListener(onClickBrowse);

        return convertView;
    }

    private void startChat(MyChatData myChatData) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("userId", myChatData.getUserId());
        intent.putExtra("sellerId", myChatData.getOtherUserId());
        activity.startActivity(intent);
    }
}

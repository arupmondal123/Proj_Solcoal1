package com.at.drgrep.activity;

import android.os.Bundle;
import android.app.Activity;
import android.widget.ListView;

import com.at.drgrep.R;
import com.at.drgrep.adapter.MyChatAdapter;
import com.at.drgrep.sqllite.DBChatHelper;

public class UserChatEntryHoldingPage2 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_user_chat_entry_holding_page);
        String userId = getIntent().getStringExtra("userId");

        MyChatAdapter myChatAdapter = new MyChatAdapter(this);
        DBChatHelper dbChatHelper = new DBChatHelper(this);
        myChatAdapter.addMyChatData(dbChatHelper.getMyChatDataList(userId));
        ListView messagesList = (ListView) findViewById(R.id.listUserChatEntries);
        messagesList.setAdapter(myChatAdapter);
    }

}

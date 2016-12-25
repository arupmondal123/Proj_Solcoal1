
package com.at.drgrep.activity;

import com.at.drgrep.R;
import com.at.drgrep.adapter.MyChatAdapter;
import com.at.drgrep.sqllite.DBChatHelper;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;


public class UserChatEntryHoldingPage extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// setContentView(R.layout.act_user_chat_entry_holding_page);
		/* change */ setContentView(R.layout.a___act_user_chat_entry_holding_page);
		String userId = getIntent().getStringExtra("userId");

		MyChatAdapter myChatAdapter = new MyChatAdapter(this);
		DBChatHelper dbChatHelper = new DBChatHelper(this);
		myChatAdapter.addMyChatData(dbChatHelper.getMyChatDataList(userId));
		ListView messagesList = (ListView) findViewById(R.id.listUserChatEntries);
		messagesList.setAdapter(myChatAdapter);

		/**/

		((LinearLayout) findViewById(R.id.handle)).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				onBackPressed();
			}
		});

		/**/
	}

}

package com.at.solcoal.activity;

import com.at.solcoal.R;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.at.solcoal.sqllite.DBChatHelper;
import com.at.solcoal.utility.SharedPreferenceUtility;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.messaging.Message;
import com.sinch.android.rtc.messaging.MessageClient;
import com.sinch.android.rtc.messaging.MessageClientListener;
import com.sinch.android.rtc.messaging.MessageDeliveryInfo;
import com.sinch.android.rtc.messaging.MessageFailureInfo;


import java.util.List;

public class MessagingActivity extends BaseActivity implements MessageClientListener {

    private static final String TAG = MessagingActivity.class.getSimpleName();

    private MessageAdapter mMessageAdapter;
    private EditText mTxtRecipient;
    private EditText mTxtTextBody;
    private Button mBtnSend;
    private DBChatHelper dbChatHelper;
    private String userId = null;
    private String otherUserId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messaging);
        userId = SharedPreferenceUtility.getUserInfo(this).getId();
        dbChatHelper = new DBChatHelper(this);

        Bundle bundle = getIntent().getExtras();
        mTxtRecipient = (EditText) findViewById(R.id.txtRecipient);
        /* Making the recipient edit text uneditable */
        mTxtRecipient.setEnabled(false);

        mTxtRecipient.setText(bundle.getString("sellerId"));
        otherUserId = bundle.getString("sellerId");
        mTxtTextBody = (EditText) findViewById(R.id.txtTextBody);
        mTxtTextBody.requestFocus();

        mMessageAdapter = new MessageAdapter(this);
        mMessageAdapter.addMessage(dbChatHelper, userId, otherUserId );
        ListView messagesList = (ListView) findViewById(R.id.lstMessages);
        messagesList.setAdapter(mMessageAdapter);

        mBtnSend = (Button) findViewById(R.id.btnSend);
        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }

    @Override
    public void onDestroy() {
        if (getSinchServiceInterface() != null) {
            getSinchServiceInterface().removeMessageClientListener(this);
        }
        super.onDestroy();
    }

    @Override
    public void onServiceConnected() {
        getSinchServiceInterface().addMessageClientListener(this);
        setButtonEnabled(true);
    }

    @Override
    public void onServiceDisconnected() {
        setButtonEnabled(false);
    }

    private void sendMessage() {
        String recipient = mTxtRecipient.getText().toString();
        String textBody = mTxtTextBody.getText().toString();
        if (recipient.isEmpty()) {
            Toast.makeText(this, "No recipient added", Toast.LENGTH_SHORT).show();
            return;
        }
        if (textBody.isEmpty()) {
            Toast.makeText(this, "No text message", Toast.LENGTH_SHORT).show();
            return;
        }
        getSinchServiceInterface().sendMessage(recipient, textBody);

        mTxtTextBody.setText("");
    }

    private void setButtonEnabled(boolean enabled) {
        mBtnSend.setEnabled(enabled);
    }

    @Override
    public void onIncomingMessage(MessageClient client, Message message) {
        dbChatHelper.insertChatMessage(userId, message.getSenderId(), "-1", message.getTextBody(), MessageAdapter.DIRECTION_INCOMING);
        mMessageAdapter.addMessage(dbChatHelper,userId,message.getSenderId());
    }

    @Override
    public void onMessageSent(MessageClient client, Message message, String recipientId) {
        dbChatHelper.insertChatMessage(userId, otherUserId, "-1", message.getTextBody(), MessageAdapter.DIRECTION_OUTGOING);
        mMessageAdapter.addMessage(dbChatHelper, userId, otherUserId);
    }

    @Override
    public void onShouldSendPushData(MessageClient client, Message message, List<PushPair> pushPairs) {
        // Left blank intentionally
    }

    @Override
    public void onMessageFailed(MessageClient client, Message message,
            MessageFailureInfo failureInfo) {
        StringBuilder sb = new StringBuilder();
        sb.append("Sending failed: ")
                .append(failureInfo.getSinchError().getMessage());

        Toast.makeText(this, sb.toString(), Toast.LENGTH_LONG).show();
        Log.d(TAG, sb.toString());
    }

    @Override
    public void onMessageDelivered(MessageClient client, MessageDeliveryInfo deliveryInfo) {
        Log.d(TAG, "onDelivered");
    }
}

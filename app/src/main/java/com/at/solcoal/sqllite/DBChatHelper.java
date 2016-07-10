package com.at.solcoal.sqllite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;

import com.at.solcoal.model.MyChatData;
import com.sinch.android.rtc.messaging.Message;
import com.sinch.android.rtc.messaging.WritableMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by anjan on 08-04-2016.
 */
public class DBChatHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "DrGrep.db";
    public static final String CHAT_TABLE_NAME = "chat";
    public static final String CHAT_ENTRY_TABLE_NAME = "chat_entries";
    public static final String CHAT_COLUMN_USER_ID = "user_id";
    public static final String CHAT_COLUMN_OTHER_USER_ID = "other_user_id";
    public static final String CHAT_COLUMN_PRODUCT_ID = "product_id";
    public static final String CHAT_COLUMN_CHAT_ID = "chat_id";
    public static final String CHAT_ENTRIES_COLUMN_CHAT_ID = "chat_id";
    public static final String CHAT_ENTRIES_COLUMN_CHAT_ENTRY_ID = "chat_entry_id";
    public static final String CHAT_ENTRIES_COLUMN_CHAT_ENTRY_DIRECTION = "direction";
    public static final String CHAT_ENTRIES_COLUMN_CHAT_ENTRY_MSG_TEXT = "msg_text";
    public static final String CHAT_ENTRIES_COLUMN_CHAT_ENTRY_TIME = "entry_time";
    private HashMap hp;

    public DBChatHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table if not exists chat " +
                        "(chat_id integer primary key AUTOINCREMENT, user_id text, other_user_id text, product_id text)"
        );

        db.execSQL(
                "create table if not exists chat_entries " +
                        "(chat_entry_id integer primary key AUTOINCREMENT, chat_id integer, msg_text text, direction integer, entry_time DATETIME DEFAULT CURRENT_TIMESTAMP)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertChatMessage (String userId, String otherUserId, String productId, String msgText, int direction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = null;
        long chatId = getChatId(userId, otherUserId);
        if (chatId <= 0) {
            contentValues = new ContentValues();
            contentValues.put("user_id", userId);
            contentValues.put("other_user_id", otherUserId);
            contentValues.put("product_id", productId);
            chatId = db.insert("chat", null, contentValues);
        }
        if (chatId > 0) {
            contentValues = new ContentValues();
            contentValues.put("chat_id", chatId);
            contentValues.put("msg_text", msgText);
            contentValues.put("direction", direction);
            db.insert("chat_entries", null, contentValues);
        }
        return true;
    }

    public long getChatId(String userId, String otherUserId){
        SQLiteDatabase db = this.getReadableDatabase();
        int chatId = -1;
        Cursor res =  db.rawQuery("select chat_id from chat where user_id=" + userId + " and other_user_id=" + otherUserId, null);
        res.moveToFirst();
        int index = res.getColumnIndex(CHAT_COLUMN_CHAT_ID);
        try {
            chatId = res.getInt(index);
        } catch (Exception e) {

        }
        return chatId;
    }

    public List<MyChatData> getMyChatDataList(String userId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select other_user_id from chat where user_id=" + userId + "", null);
        List<MyChatData> l = new ArrayList<MyChatData>();
        MyChatData myChatData;
        while(res.moveToNext()) {
            String otherUserId = res.getString(res.getColumnIndex(CHAT_COLUMN_OTHER_USER_ID));
            myChatData = new MyChatData();
            myChatData.setUserId(userId);
            myChatData.setOtherUserId(otherUserId);
            l.add(myChatData);
        }
        return l;
    }

    public List<Pair<Message, Integer>> getChatMessages(final String userId, final String otherUserId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select chat_id from chat where user_id=" + userId + " and other_user_id=" + otherUserId, null);
        res.moveToFirst();
        long chatId = res.getLong(res.getColumnIndex(CHAT_COLUMN_CHAT_ID));
        List<Pair<Message, Integer>> mMessages = new ArrayList<Pair<Message, Integer>>();
        Message message = null;
        res =  db.rawQuery("select * from chat_entries where chat_id=" + chatId + "", null);
        while(res.moveToNext()) {
            final int direction = res.getInt(res.getColumnIndex(CHAT_ENTRIES_COLUMN_CHAT_ENTRY_DIRECTION));
            final int msgId = res.getInt(res.getColumnIndex(CHAT_ENTRIES_COLUMN_CHAT_ENTRY_ID));
            final String bodyText = res.getString(res.getColumnIndex(CHAT_ENTRIES_COLUMN_CHAT_ENTRY_MSG_TEXT));
            message = new Message() {
                @Override
                public String getMessageId() {
                    return msgId+"";
                }

                @Override
                public Map<String, String> getHeaders() {
                    return null;
                }

                @Override
                public String getTextBody() {
                    return bodyText;
                }

                @Override
                public List<String> getRecipientIds() {
                    List l = new  ArrayList<String>(1);
                    l.add(otherUserId);
                    return l;
                }

                @Override
                public String getSenderId() {
                    return userId;
                }

                @Override
                public Date getTimestamp() {
                    return new Date(System.currentTimeMillis());
                }
            };
            mMessages.add(new Pair(message, direction));
        }
        return mMessages;
    }
}

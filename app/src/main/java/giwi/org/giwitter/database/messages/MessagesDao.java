package giwi.org.giwitter.database.messages;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.LinkedList;
import java.util.List;

import giwi.org.giwitter.database.DatabaseHelper;
import giwi.org.giwitter.model.Message;

import static giwi.org.giwitter.database.messages.MessagesDB.SQL_DELETE_ENTRIES;

/**
 * The type Messages dao.
 */
public class MessagesDao {
    DatabaseHelper databaseHelper;

    /**
     * Instantiates a new Messages dao.
     *
     * @param context the context
     */
    public MessagesDao(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    /**
     * Write a new message.
     *
     * @param db  the db
     * @param msg the msg
     */
    private void writeMessage(final SQLiteDatabase db, final Message msg) {

        // Create a new map of values, where column names are the keys
        final ContentValues values = new ContentValues();
        values.put(MessagesDB.MessageEntry.COLUMN_MESSAGE, msg.getMsg());
        values.put(MessagesDB.MessageEntry.COLUMN_USER, msg.getUsername());
        values.put(MessagesDB.MessageEntry.COLUMN_DATE, msg.getDate());

        db.insert(MessagesDB.MessageEntry.TABLE_NAME, null, values);
    }

    /**
     * Write messages.
     *
     * @param messages the messages
     */
    public void writeMessages(List<Message> messages) {
        // Gets the data repository in write mode
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.execSQL(SQL_DELETE_ENTRIES);
        for (Message msg : messages) {
            // insert
            writeMessage(db, msg);
        }
        db.close();

    }

    /**
     * retrieve all registered messages.
     *
     * @return the list
     */
    public List<Message> readMessages() {
        final SQLiteDatabase db = databaseHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        final String[] projection = {
                MessagesDB.MessageEntry.COLUMN_MESSAGE,
                MessagesDB.MessageEntry.COLUMN_USER,
                MessagesDB.MessageEntry.COLUMN_DATE,};


        final Cursor c = db.query(MessagesDB.MessageEntry.TABLE_NAME,       // The table to query
                projection,                               // The columns to return
                null,                                    // The columns for the WHERE clause
                null,                                   // The values for the WHERE clause
                null,                                  // don't group the rows
                null,                                 // don't filter by row groups
                null                            // The sort order
        );
        final List<Message> messages = cursorToMessages(c);
        c.close();
        db.close();
        return messages;
    }

    private List<Message> cursorToMessages(Cursor cursor) {
        final List<Message> msgs = new LinkedList<Message>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            msgs.add(cursorToMessage(cursor));
            cursor.moveToNext();
        }
        return msgs;
    }

    private Message cursorToMessage(Cursor cursor) {
        return new Message(
                cursor.getString(cursor.getColumnIndex(MessagesDB.MessageEntry.COLUMN_USER)),
                cursor.getString(cursor.getColumnIndex(MessagesDB.MessageEntry.COLUMN_MESSAGE)),
                cursor.getLong(cursor.getColumnIndex(MessagesDB.MessageEntry.COLUMN_DATE)));
    }
}

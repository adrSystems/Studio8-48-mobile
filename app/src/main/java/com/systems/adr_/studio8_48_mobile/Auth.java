package com.systems.adr_.studio8_48_mobile;

import android.content.ContentValues;
import android.content.Context;

/**
 * Created by luis_ on 29/04/2017.
 */

class Auth {
    private static final Auth ourInstance = new Auth();

    private Client client;

    static Auth getInstance() {
        return ourInstance;
    }

    private Auth() {
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client, Context context) {
        this.client = client;
        LocalDBHelper dbHelper = new LocalDBHelper(context, "studio848",null,1);
        ContentValues cv = new ContentValues();
        cv.put("id", client.getAccount().getId());
        dbHelper.getWritableDatabase().insert("auth", null, cv);
        dbHelper.close();
    }

    public void setClient(Client client, Context context, boolean insertToDB) {
        this.client = client;
        if(insertToDB)
        {
            LocalDBHelper dbHelper = new LocalDBHelper(context, "studio848",null,1);
            ContentValues cv = new ContentValues();
            cv.put("id", client.getAccount().getId());
            dbHelper.getWritableDatabase().insert("auth", null, cv);
            dbHelper.close();
        }
    }
}

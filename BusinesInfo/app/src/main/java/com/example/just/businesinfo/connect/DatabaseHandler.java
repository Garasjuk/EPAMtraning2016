package com.example.just.businesinfo.connect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.example.just.businesinfo.Entity.IngotDataSet;
import com.example.just.businesinfo.Entity.MetalDataSet;
import com.example.just.businesinfo.Entity.ParsedDataSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "currencyManager7";

    // CurrencyFragment table name
    private static final String TABLE_CURRENCY = "currencyTable1";
    private static final String TABLE_METAL = "metalTable1";

    // CurrencyFragment Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_currency = "currency";
    private static final String KEY_numCode = "numCode";
    private static final String KEY_charCode = "charCode";
    private static final String KEY_scale = "scale";
    private static final String KEY_name = "name";
    private static final String KEY_rate = "rate";
    private static final String KEY_status = "status";

    private static final String KEY_metalId = "metalId";
    private static final String KEY_nominal = "nominal";
    private static final String KEY_nameEng = "nameEng";
    private static final String KEY_certificateRuble = "certificateRuble";
    private static final String KEY_banksDollars = "banksDollars";

    private static final String SELECT_getAllCurrencySetting = "SELECT id, CharCode, Status FROM ";
    private static final String SELECT_getAllMetalSetting = "SELECT id, Name, NameEng, Nominal, Status FROM ";
    private static final String SELECT_getAllContactsHash = "SELECT CharCode, Scale, Name, Rate FROM ";
    private static final String SELECT_getAllMetal = "SELECT  metalId, nominal, name , nameEng, certificateRuble, banksDollars FROM ";
    private static final String DROP_table = "DROP TABLE IF EXISTS ";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CURRENCY_TABLE = "CREATE TABLE " + TABLE_CURRENCY + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_currency + " TEXT,"
                + KEY_numCode + " TEXT,"
                + KEY_charCode + " TEXT,"
                + KEY_scale + " TEXT,"
                + KEY_name + " TEXT,"
                + KEY_rate + " TEXT,"
                + KEY_status + " TEXT"
                + ")";
        db.execSQL(CREATE_CURRENCY_TABLE);

        String CREATE_METAL_TABLE = "CREATE TABLE " + TABLE_METAL + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_metalId + " TEXT,"
                + KEY_nominal + " TEXT,"
                + KEY_name + " TEXT,"
                + KEY_nameEng + " TEXT,"
                + KEY_certificateRuble + " TEXT,"
                + KEY_banksDollars + " TEXT,"
                + KEY_status + " TEXT"
                + ")";
        db.execSQL(CREATE_METAL_TABLE);
    }


    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL(DROP_table + TABLE_CURRENCY);

        // Create tables again
        onCreate(db);
    }

    public void onDrop() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Drop older table if existed
        db.execSQL(DROP_table + TABLE_CURRENCY);
        onCreate(db);
        db.execSQL(DROP_table + TABLE_METAL);
        onCreate(db);
    }


    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new ParsedDataSet
    @WorkerThread
    public void addContact(ParsedDataSet parsedDataSet) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_currency, parsedDataSet.getCurrency()); // CurrencyFragment
        values.put(KEY_numCode, parsedDataSet.getNumCode()); //  NumCode
        values.put(KEY_charCode, parsedDataSet.getCharCode()); // CharCode
        values.put(KEY_name, parsedDataSet.getName()); // Name
        values.put(KEY_scale, parsedDataSet.getScale()); // Scale
        values.put(KEY_rate, parsedDataSet.getRate()); // Rate
        values.put(KEY_status, parsedDataSet.getStatus()); // Status

        // Inserting Row
        db.insert(TABLE_CURRENCY, null, values);
        db.close(); // Closing database connection
    }

    // Adding new ParsedDataSet
    public void addMetal(MetalDataSet metalDataSet, IngotDataSet ingotDataSet) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_metalId, metalDataSet.getMetal());
        values.put(KEY_nominal, ingotDataSet.getNominal());
        values.put(KEY_name, metalDataSet.getName());
        values.put(KEY_nameEng, metalDataSet.getNameEng());
        values.put(KEY_certificateRuble, ingotDataSet.getCertificateRubles());
        values.put(KEY_banksDollars, ingotDataSet.getBanksDollars());
        values.put(KEY_status, metalDataSet.getStatus());

        // Inserting Row
        db.insert(TABLE_METAL, null, values);
        db.close();
    }
/*

    // Getting single contact
    Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[]{KEY_ID,
                        KEY_NAME, KEY_PH_NO}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Contact currency = new Contact(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return contact
        return contact;
    }
*/

    // Getting All Contacts
    public List<ParsedDataSet> getAllContacts() {
        List<ParsedDataSet> carsedDataSetList = new ArrayList<ParsedDataSet>();
        // Select All Query
        String selectQuery = "SELECT CharCode, Scale, Name, Rate FROM " + TABLE_CURRENCY;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ParsedDataSet parsedDataSet = new ParsedDataSet();
                parsedDataSet.setCharCode(cursor.getString(0));
                parsedDataSet.setScale(cursor.getString(1));
                parsedDataSet.setName(cursor.getString(2));
                parsedDataSet.setRate(cursor.getString(3));

                // Adding currency to list
                carsedDataSetList.add(parsedDataSet);
            } while (cursor.moveToNext());
        }

        // return currency list
        return carsedDataSetList;
    }

    // Get Create table
    public int getCreateTableCurrency() {
        int result = 0;
//        Stringing selectQuery = "SELECT count(name) FROM sqlite_master Where type = 'table' AND name = " + TABLE_CURRENCY;
        String selectQuery = "SELECT count(*) FROM sqlite_master Where type = 'table' AND name = " + "'" + TABLE_CURRENCY + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ParsedDataSet parsedDataSet = new ParsedDataSet();
                result = Integer.parseInt(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return result;
    }


    // Get Create table
    public int getCreateTableMetal() {
        int result = 0;
        String selectQuery = "SELECT count(*) FROM sqlite_master Where type = 'table' AND name = " + "'" + TABLE_METAL + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                MetalDataSet metalDataSet = new MetalDataSet();
                result = Integer.parseInt(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return result;
    }


    // Getting All CurrencyFragment
    public ArrayList<ParsedDataSet> getAllCurrencySetting() {
        ArrayList<ParsedDataSet> carsedDataSetList = new ArrayList<ParsedDataSet>();
        // Select All Query
        String selectQuery = SELECT_getAllCurrencySetting + TABLE_CURRENCY;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ParsedDataSet parsedDataSet = new ParsedDataSet();
                parsedDataSet.setId(Integer.parseInt(cursor.getString(0)));
                parsedDataSet.setCharCode(cursor.getString(1));
                parsedDataSet.setStatus(cursor.getString(2));

                // Adding currency to list
                carsedDataSetList.add(parsedDataSet);
            } while (cursor.moveToNext());
        }

        // return currency list
        return carsedDataSetList;
    }

    // Getting All MetalFragment
    public ArrayList<MetalDataSet> getAllMetalSetting() {
        ArrayList<MetalDataSet> metalDataSetList = new ArrayList<MetalDataSet>();
        // Select All Query
        String selectQuery = SELECT_getAllMetalSetting + TABLE_METAL;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MetalDataSet metalDataSet = new MetalDataSet();
                metalDataSet.setId(Integer.parseInt(cursor.getString(0)));
                metalDataSet.setName(cursor.getString(1));
                metalDataSet.setNameEng(cursor.getString(2));
                metalDataSet.setNominal(cursor.getString(3));
                metalDataSet.setStatus(cursor.getString(4));

                // Adding currency to list
                metalDataSetList.add(metalDataSet);
            } while (cursor.moveToNext());
        }

        // return currency list
        return metalDataSetList;
    }

    // Getting All currencysHash
    public List<HashMap<String, String>> getAllContactsHash() {
        List<HashMap<String, String>> carsedDataSetList = new ArrayList<>();
        // Select All Query
        String selectQuery = SELECT_getAllContactsHash + TABLE_CURRENCY + " WHERE status = 'true'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
//                ParsedDataSet parsedDataSet = new ParsedDataSet();
                HashMap<String, String> parsedDataSet = new HashMap<>();
                parsedDataSet.put("CharCode", cursor.getString(0));
                parsedDataSet.put("Scale", cursor.getString(1));
                parsedDataSet.put("Name", cursor.getString(2));
                parsedDataSet.put("Rate", cursor.getString(3));

                // Adding currency to list
                carsedDataSetList.add(parsedDataSet);
            } while (cursor.moveToNext());
        }

        cursor.close();
        // return currency list
        return carsedDataSetList;
    }


    // Getting ParsedDataSet
    public ParsedDataSet getParsedDataSetByName(String numCode) {
        SQLiteDatabase db = this.getReadableDatabase();
        ParsedDataSet parsedDataSet = null;
        Cursor cursor = db.query(TABLE_CURRENCY, new String[]{
                        KEY_currency, KEY_numCode, KEY_charCode, KEY_scale, KEY_name, KEY_rate, KEY_status}, KEY_numCode + "=?",
                new String[]{numCode}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {

            parsedDataSet = new ParsedDataSet(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6));
            cursor.close();
        }
        // return parsedDataSet
        return parsedDataSet;
    }


    // Getting All MetalFragment
    public ArrayList<MetalDataSet> getAllMetal() {
        ArrayList<MetalDataSet> metalDataSetArrayList = new ArrayList<MetalDataSet>();
        // Select All Query
        String selectQuery = SELECT_getAllMetal + TABLE_METAL + " WHERE status = 'true'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                MetalDataSet metalDataSet = new MetalDataSet();
                metalDataSet.setMetalId(cursor.getString(0));
                metalDataSet.setNominal(cursor.getString(1));
                metalDataSet.setName(cursor.getString(2));
                metalDataSet.setNameEng(cursor.getString(3));
                metalDataSet.setCertificateRubles(cursor.getString(4));
                metalDataSet.setBanksDollars(cursor.getString(5));


//                Log.v("getAllMetalSetting Nominal ", cursor.getString(1) );
                metalDataSetArrayList.add(metalDataSet);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return metalDataSetArrayList;
    }

    // Getting MetalDataSet
    public MetalDataSet getMetalDataSetByName(String id, String nominal) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] selectionArgs = new String[]{String.valueOf(id), String.valueOf(nominal)};
        MetalDataSet metalDataSet = null;
        Cursor cursor = db.query(TABLE_METAL, new String[]{
                        KEY_ID, KEY_metalId, KEY_nominal, KEY_name, KEY_nameEng, KEY_banksDollars, KEY_certificateRuble},
                KEY_metalId + " = ? AND " + KEY_nominal + " = ?",
                selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {

            metalDataSet = new MetalDataSet(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6));
            cursor.close();
        }
        return metalDataSet;
    }


    // Updating single dataSet
    public int updateDataSet(String rate, String numCode) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_rate, rate);

        // updating row
        return db.update(TABLE_CURRENCY, values, KEY_numCode + " = " + numCode, null);
//                new String[]{String.valueOf(parsedDataSet.getId())});
    }

    // Updating MetalDataSet
    public int updateMetalDataSet(String id, String nominal, String banksDollars, String certificateRubles) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_certificateRuble, certificateRubles);
        values.put(KEY_banksDollars, banksDollars);

        return db.update(TABLE_METAL, values, KEY_metalId + " = " + id + " AND " + KEY_nominal + " = " + nominal, null);
    }


    // Updating single dataSet
    public int updateMetalDataSet(String rate, String numCode) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_rate, rate);

        // updating row
        return db.update(TABLE_METAL, values, KEY_numCode + " = " + numCode, null);
    }


    // Updating status
    public int updateStatus(int id, boolean status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
//        values.put(KEY_status, status);

        // updating row
        if (status) {
            Log.v("Update ", id + " " + status);
            values.put(KEY_status, "true");
            return db.update(TABLE_CURRENCY, values, KEY_ID + " = " + id, null);
        } else {
            values.put(KEY_status, "false");
            Log.v("Update ", id + " " + status);
            return db.update(TABLE_CURRENCY, values, KEY_ID + " = " + id, null);
        }
    }

    // Updating status
    public int updateMetalStatus(int id, boolean status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        // updating row
        if (status) {
//            Log.v("Update ", id + " " + status);
            values.put(KEY_status, "true");
            return db.update(TABLE_METAL, values, KEY_ID + " = " + id, null);
        } else {
            values.put(KEY_status, "false");
//            Log.v("Update ", id + " " + status);
            return db.update(TABLE_METAL, values, KEY_ID + " = " + id, null);
        }
    }


    // Deleting single currency
/*
    public void deleteCurrency(ParsedDataSet parsedDataSet) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CURRENCY, KEY_ID + " = ?",
                new String[]{String.valueOf(currency.getID())});
        db.close();
    }
*/


    // Getting currency Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CURRENCY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}

package com.serc.projectspaceapp.sql;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context ) {
        super(context,"FixIT.db",null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        //region Create Users Table
        DB.execSQL("create Table Users(userID TEXT primary key," +
                "name TEXT not null," +
                "password PASSWORD not null," +
                "number NUMBER not null)");
        //endregion
        //region Create Categories Table
        DB.execSQL("create Table Categories(categoryID INTEGER primary key," +
                "categoryName TEXT not null)");
        //endregion
        //region Create Tickets Table
        DB.execSQL("create Table Tickets(ticketID INTEGER primary key autoincrement," +
                "userID TEXT not null," +
                "categoryID INTEGER not null," +
                "details TEXT not null," +
                "loggedDate LONG not null," +
                "requiredDate LONG not null," +
                "foreign key(userID) references Users(userID)," +
                "foreign key(categoryID) references Categories(categoryID))");
        //endregion
        //region Populate Categories Table
        DB.execSQL("insert into categories(categoryID,categoryName) values(1, 'Desktop PC'),(2, 'Laptop')," +
                "(3, 'Phone (Mobile)'),(4,'Phone (Desk)'),(5,'Printer'),(6,'Printer'),(7,'Software: Office - Word')," +
                "(8, 'Software: Office - PowerPoint'),(9,'Software: Office - Excel'),(10,'Software: Office - Publisher')," +
                "(11, 'Software: Teams'),(12,'Tablet'),(13,'Whiteboard'),(14,'Other: Not Listed')");
        //endregion
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("drop Table if exists UserDetails");
    }
    public Boolean insertUser(String name,String number,String email,String password){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userID",email);
        contentValues.put("name",name);
        contentValues.put("password",password);
        contentValues.put("number",number);
        long result= DB.insert("Users",null,contentValues);
        return result != -1;
    }

    public Boolean updateUser(String email, String name,String number,String password){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("password",password);
        contentValues.put("number",number);
        long result= DB.update("Users", contentValues, "userID = ?", new String[]{email});
        return result != -1;
    }

    public Cursor getData(){
        SQLiteDatabase DB = this.getWritableDatabase();
        return DB.rawQuery("Select * from Users ",null);
    }

    public Cursor getUser(String email) {
        SQLiteDatabase DB = this.getWritableDatabase();
        return DB.rawQuery("Select * from Users where userID='" + email + "'", null);
    }

    public int getLastTicketID() {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor result = DB.rawQuery("select * from sqlite_sequence where name='Tickets'",null);
        int lastID = 0;
        if(result != null && result.moveToNext()) {
            lastID = Integer.parseInt(result.getString(1));
        }
        if (result != null) {
            result.close();
        }
        return lastID;
    }

    public Cursor getTicket(String id, String user) {
        SQLiteDatabase DB = this.getWritableDatabase();
        //String query = "select * from Tickets where userID='" + user + "'" ;
        String query = "select ticketID, categoryName as category, details, loggedDate, requiredDate from Tickets inner join categories on categories.categoryID = tickets.categoryID WHERE userID = '" + user + "'";
        if (id.equals("-1")) {
            query += "order by ticketID DESC Limit 1";
        } else {
            query += " and ticketID=" + id;
        }
        return DB.rawQuery(query, null);
    }

    public Boolean insertTicket(String userID, int categoryID, String details,
                                long loggedDate, long requiredDate){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userID", userID);
        contentValues.put("categoryID",categoryID);
        contentValues.put("details",details);
        contentValues.put("loggedDate",loggedDate);
        contentValues.put("requiredDate", requiredDate);
        long result= DB.insert("Tickets",null,contentValues);
        return result != -1;
    }
}
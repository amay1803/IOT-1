package com.example.iot_1;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DbHandler extends SQLiteOpenHelper {
    SQLiteDatabase db;
    Context context;

    DbHandler(Context context){
        super(context,"studentdb",null,1);
        this.context=context;
        db=this.getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql ="create table student(number text primary key)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addStudent(Student s){
        ContentValues cv=new ContentValues();
        cv.put("number",s.getNumber());
        long id=db.insert("student",null,cv);
        if(id<0)
            Toast.makeText(context, "insert issue", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Whooa!!!Number Plate Inserted", Toast.LENGTH_SHORT).show();

    }



    public String viewStudent(){
        StringBuffer sb=new StringBuffer();
        Cursor cursor=db.query("student",null,null,null,null,null,null);
        cursor.moveToFirst();
if (cursor.getCount()>0) {
    cursor.moveToFirst();
    do {
        String number = cursor.getString(0);
        sb.append("Number Plate : "+number+"\n");
    } while (cursor.moveToNext());

}return sb.toString();
    }

    public Boolean checkNumber(String number){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("Select * from student where number=?",new String []{number});
        if (cursor.getCount()>0)return false;else return true;
    }






}

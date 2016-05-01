package ssm.countup;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DataAccess extends SQLiteOpenHelper {

    private SQLiteDatabase db = null;
    private StringBuffer SqlStr = null;
    private String[] SqlPram = null;

    public DataAccess(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {

        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        StringBuffer SqlSb = new StringBuffer();

        SqlSb.append("CREATE TABLE countTable");
        SqlSb.append("(");
        SqlSb.append("rowId INTEGER PRIMARY KEY AUTOINCREMENT,");
        SqlSb.append("type VARCHAR(1),");
        SqlSb.append("money VARCHAR(20),");
        SqlSb.append("date VARCHAR(20),");
        SqlSb.append("address VARCHAR(100),");
        SqlSb.append("text VARCHAR(300),");
        SqlSb.append("recordTime VARCHAR(20),");
        SqlSb.append("backup1 VARCHAR(100),");
        SqlSb.append("backup2 VARCHAR(100),");
        SqlSb.append("backup3 VARCHAR(100),");
        SqlSb.append("backup4 VARCHAR(100),");
        SqlSb.append("backup5 VARCHAR(100)");
        SqlSb.append(")");

        db.execSQL(SqlSb.toString());

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }

    public int insert(CountUpBeam insertBeam) {

        db = this.getWritableDatabase();
        SqlStr = new StringBuffer();
        SqlStr.append("INSERT INTO countTable");
        SqlStr.append("( type, money, date, address, text, recordTime )");
        SqlStr.append(" values ");
        SqlStr.append("( ?, ?, ?, ?, ?, ? )");

        SqlPram = new String[6];

        SqlPram[0] = insertBeam.getType();
        SqlPram[1] = insertBeam.getMoney();
        SqlPram[2] = insertBeam.getDate();
        SqlPram[3] = insertBeam.getAddress();
        SqlPram[4] = insertBeam.getText();
        SqlPram[5] = insertBeam.getRecordTime();

        db.execSQL(SqlStr.toString(), SqlPram);

        return 0;

    }

    public int update(CountUpBeam updateBeam) {

        db = this.getWritableDatabase();
        SqlStr = new StringBuffer();
        SqlStr.append("UPDATE countTable ");
        SqlStr.append(" SET type = ?");
        SqlStr.append(" , money = ?");
        SqlStr.append(" , date = ?");
        SqlStr.append(" , address = ?");
        SqlStr.append(" , text = ?");
        SqlStr.append(" , recordTime = ?");
        SqlStr.append(" WHERE rowId = ?");

        SqlPram = new String[7];

        SqlPram[0] = updateBeam.getType();
        SqlPram[1] = updateBeam.getMoney();
        SqlPram[2] = updateBeam.getDate();
        SqlPram[3] = updateBeam.getAddress();
        SqlPram[4] = updateBeam.getText();
        SqlPram[5] = updateBeam.getRecordTime();
        SqlPram[6] = updateBeam.getRowId()+"";

        db.execSQL(SqlStr.toString(), SqlPram);

        return 0;

    }

    public int delete(int rowId) {

        db = this.getWritableDatabase();
        SqlStr = new StringBuffer();

        SqlStr.append("DELETE FROM countTable ");
        SqlStr.append(" WHERE rowId = ?");

        db.execSQL(SqlStr.toString(), new String[]{rowId+""});

        return 0;
    }

    public void findRecentData(CountUpBeam resultBeam) {

        db = this.getReadableDatabase();

        SqlStr = new StringBuffer();
        SqlStr.append("SELECT * FROM countTable order by recordTime desc limit 0, 1");
        Cursor result = db.rawQuery(SqlStr.toString(), null);

        while (result.moveToNext()) {
            resultBeam.setRowId(result.getInt(result.getColumnIndex("rowId")));
            resultBeam.setType(result.getString(result.getColumnIndex("type")));
            resultBeam.setMoney(result.getString(result.getColumnIndex("money")));
            resultBeam.setDate(result.getString(result.getColumnIndex("date")));
            resultBeam.setAddress(result.getString(result.getColumnIndex("address")));
            resultBeam.setText(result.getString(result.getColumnIndex("text")));
            resultBeam.setRecordTime(result.getString(result.getColumnIndex("recordTime")));
        }

        result.close();
    }

    public void findDataById(int rowId, CountUpBeam resultBeam) {

        db = this.getReadableDatabase();

        SqlStr = new StringBuffer();
        SqlStr.append("SELECT * FROM countTable WHERE rowId = ?");
        Cursor result = db.rawQuery(SqlStr.toString(), new String[]{rowId+""});

        while (result.moveToNext()) {
            resultBeam.setRowId(result.getInt(result.getColumnIndex("rowId")));
            resultBeam.setType(result.getString(result.getColumnIndex("type")));
            resultBeam.setMoney(result.getString(result.getColumnIndex("money")));
            resultBeam.setDate(result.getString(result.getColumnIndex("date")));
            resultBeam.setAddress(result.getString(result.getColumnIndex("address")));
            resultBeam.setText(result.getString(result.getColumnIndex("text")));
            resultBeam.setRecordTime(result.getString(result.getColumnIndex("recordTime")));
        }

        result.close();
    }

    public String findInTodayTotalData (String today) {

        String resultStr = null;
        db = this.getReadableDatabase();

        SqlStr = new StringBuffer();
        SqlStr.append("SELECT SUM(money) FROM countTable WHERE date = ? AND type = '0'");
        Cursor result = db.rawQuery(SqlStr.toString(), new String[]{today});

        while (result.moveToNext()) {
            resultStr = result.getString(0);
        }

        if (resultStr == null) {
            resultStr = "0";
        }

        result.close();
        return resultStr;

    }

    public String findInMonthTotalData (String today) {

        String resultStr = null;

        db = this.getReadableDatabase();

        SqlStr = new StringBuffer();
        SqlStr.append("SELECT SUM(money) FROM countTable WHERE date like ?||'%' AND type = '0'");
        Cursor result = db.rawQuery(SqlStr.toString(), new String[]{today.substring(0, 7)});

        while (result.moveToNext()) {
            resultStr = result.getString(0);
        }

        if (resultStr == null) {
            resultStr = "0";
        }
        result.close();
        return resultStr;

    }

    public String findInYearTotalData (String today) {

        String resultStr = null;
        db = this.getReadableDatabase();

        SqlStr = new StringBuffer();
        SqlStr.append("SELECT SUM(money) FROM countTable WHERE date like ?||'%' AND type = '0'");
        Cursor result = db.rawQuery(SqlStr.toString(), new String[]{today.substring(0, 4)});

        while (result.moveToNext()) {
            resultStr = result.getString(0);
        }

        if (resultStr == null) {
            resultStr = "0";
        }
        result.close();
        return resultStr;

    }

    public String findOutTodayTotalData (String today) {

        String resultStr = null;
        db = this.getReadableDatabase();

        SqlStr = new StringBuffer();
        SqlStr.append("SELECT SUM(money) FROM countTable WHERE date = ? AND type = '1'");
        Cursor result = db.rawQuery(SqlStr.toString(), new String[]{today});

        while (result.moveToNext()) {
            resultStr = result.getString(0);
        }

        if (resultStr == null) {
            resultStr = "0";
        }
        result.close();
        return resultStr;

    }

    public String findOutMonthTotalData (String today) {

        String resultStr = null;
        db = this.getReadableDatabase();

        SqlStr = new StringBuffer();
        SqlStr.append("SELECT SUM(money) FROM countTable WHERE date like ?||'%' AND type = '1'");
        Cursor result = db.rawQuery(SqlStr.toString(), new String[]{today.substring(0, 7)});

        while (result.moveToNext()) {
            resultStr = result.getString(0);
        }

        if (resultStr == null) {
            resultStr = "0";
        }
        result.close();
        return resultStr;

    }

    public String findOutYearTotalData (String today) {

        String resultStr = null;
        db = this.getReadableDatabase();

        SqlStr = new StringBuffer();
        SqlStr.append("SELECT SUM(money) FROM countTable WHERE date like ?||'%' AND type = '1'");
        Cursor result = db.rawQuery(SqlStr.toString(), new String[]{today.substring(0, 4)});

        while (result.moveToNext()) {
            resultStr = result.getString(0);
        }

        if (resultStr == null) {
            resultStr = "0";
        }
        result.close();
        return resultStr;

    }

    public void findDataByMonth(String month, ArrayList<ListDayBeam> listResultBeam) {

        db = this.getReadableDatabase();

        ListDayBeam resultBeam = new ListDayBeam();
        SqlStr = new StringBuffer();
        SqlStr.append("SELECT * FROM countTable WHERE date like ?||'%'order by date desc, recordTime desc");
        Cursor result = db.rawQuery(SqlStr.toString(), new String[]{month});

        while (result.moveToNext()) {
            resultBeam = new ListDayBeam();
            resultBeam.setRowId(result.getInt(result.getColumnIndex("rowId")));
            resultBeam.setType(result.getString(result.getColumnIndex("type")));
            resultBeam.setMoney(result.getString(result.getColumnIndex("money")));
            resultBeam.setDay(result.getString(result.getColumnIndex("date")).substring(8) + "日");
            resultBeam.setCon(result.getString(result.getColumnIndex("text")));
            listResultBeam.add(resultBeam);
        }

        result.close();
    }
}

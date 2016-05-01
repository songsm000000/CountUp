package ssm.countup;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CountActivity extends AppCompatActivity implements View.OnClickListener,
        View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener,
        View.OnLongClickListener{

    private Button inBtn = null;
    private Button outBtn = null;

    private EditText moneyTxt = null;
    private TextView timeTxt = null;
    private EditText addressTxt = null;
    private EditText textTxt = null;
    private TextView recordTimeTxt = null;
    private ImageView deleteBtn = null;

    private Button saveBtn = null;
    private Button moreBtn = null;

    private String mode = null; //登录更新

    private int rowId = 0;
    private String type = null; //0:收入 1:支出

    private CountUpBeam beam = null;
    private DataAccess dataAccess = null;

    private Thread dbThread = null;
    private DbAccessThread dbAccessThread = null;
    private ThreadHandler threadHandler = null;
    private Message msg = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);
        init();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.inBtn:
                outBtn.setBackgroundColor(getResources().getColor(R.color.gray));
                outBtn.setTextColor(getResources().getColor(R.color.black));
                outBtn.setClickable(true);

                inBtn.setBackgroundColor(getResources().getColor(R.color.white));
                inBtn.setTextColor(getResources().getColor(R.color.red));
                inBtn.setClickable(false);

                moneyTxt.setTextColor(getResources().getColor(R.color.red));

                type = "0";

                break;
            case R.id.outBtn:

                inBtn.setBackgroundColor(getResources().getColor(R.color.gray));
                inBtn.setTextColor(getResources().getColor(R.color.black));
                inBtn.setClickable(true);

                outBtn.setBackgroundColor(getResources().getColor(R.color.white));
                outBtn.setTextColor(getResources().getColor(R.color.green));
                outBtn.setClickable(false);

                moneyTxt.setTextColor(getResources().getColor(R.color.green));

                type = "1";

                break;

            case R.id.saveBtn:

                if (checkData()) {

                    Toast.makeText(this, Conntext.SAVE_START, Toast.LENGTH_SHORT).show();
                    getData();
                    dbThread = new Thread(dbAccessThread, mode);
                    dbThread.start();

                }

                break;
            case R.id.moreBtn:

                mode = Conntext.INS_TXT;
                setData();
                outBtn.performClick();

                break;

            case R.id.deleteBtn:

                Toast.makeText(this, Conntext.DELETE_LONG, Toast.LENGTH_SHORT).show();
                break;

            case R.id.timeTxt:
                clearTxtBg();
                timeTxt.setBackgroundColor(getResources().getColor(R.color.lemonchiffon));
                new DatePickerDialog(this, this, Integer.parseInt(timeTxt.getText().toString().substring(0, 4)),
                        Integer.parseInt(timeTxt.getText().toString().substring(5, 7))-1,
                        Integer.parseInt(timeTxt.getText().toString().substring(8))).show();
                break;

        }

    }

    @Override
    public boolean onLongClick(View v) {

        switch (v.getId()) {
            case R.id.deleteBtn:
                mode = Conntext.DEL_TXT;
                Toast.makeText(this, Conntext.DELETE_START, Toast.LENGTH_SHORT).show();
                dbThread = new Thread(dbAccessThread, mode);
                dbThread.start();
                break;
        }
        return true;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        clearTxtBg();
        :
        switch (v.getId()) {

            case R.id.moneyTxt
                moneyTxt.setBackgroundColor(getResources().getColor(R.color.lemonchiffon));
                break;
            case R.id.addressTxt:
                addressTxt.setBackgroundColor(getResources().getColor(R.color.lemonchiffon));
                break;
            case R.id.textTxt:
                textTxt.setBackgroundColor(getResources().getColor(R.color.lemonchiffon));
                break;
        }

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        timeTxt.setText(year + "-" + new DecimalFormat("00").format(monthOfYear+1) + "-" + new DecimalFormat("00").format(dayOfMonth));
        clearTxtBg();
        addressTxt.setBackgroundColor(getResources().getColor(R.color.lemonchiffon));
        addressTxt.setFocusable(true);
        addressTxt.requestFocus();
    }

    @Override
    protected void onDestroy() {
        threadHandler.removeCallbacks(dbAccessThread);
        super.onDestroy();
    }

    private void init() {

        inBtn = (Button) findViewById(R.id.inBtn);
        outBtn = (Button) findViewById(R.id.outBtn);

        moneyTxt = (EditText) findViewById(R.id.moneyTxt);
        timeTxt = (TextView) findViewById(R.id.timeTxt);
        addressTxt = (EditText) findViewById(R.id.addressTxt);
        textTxt = (EditText) findViewById(R.id.textTxt);
        recordTimeTxt = (TextView) findViewById(R.id.recordTimeTxt);
        moneyTxt.setFilters(new InputFilter[]{new MoneyFilter()});

        deleteBtn = (ImageView) findViewById(R.id.deleteBtn);
        saveBtn = (Button) findViewById(R.id.saveBtn);
        moreBtn = (Button) findViewById(R.id.moreBtn);

        inBtn.setOnClickListener(this);
        outBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        moreBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        deleteBtn.setOnLongClickListener(this);
        timeTxt.setOnClickListener(this);

        moneyTxt.setOnFocusChangeListener(this);
        addressTxt.setOnFocusChangeListener(this);
        textTxt.setOnFocusChangeListener(this);

        Intent intent = getIntent();
        mode = intent.getStringExtra(Conntext.MODE_TXT);

        dataAccess = new DataAccess(this, Conntext.DB_NAME, null, 1);

        dbAccessThread = new DbAccessThread();
        threadHandler = new ThreadHandler();

        if (Conntext.UPD_TXT.equals(mode)) {
            rowId = intent.getIntExtra(Conntext.ROW_ID, 0);

            dbThread = new Thread(dbAccessThread, "find");
            dbThread.start();
        } else if (Conntext.INS_TXT.equals(mode)) {
            setData();
        }

    }

    private void setData() {

        if (Conntext.INS_TXT.equals(mode)) {

            rowId = 0;
            moneyTxt.setText("");
            timeTxt.setText(new SimpleDateFormat(Conntext.YYYY_MM_DD).format(Calendar.getInstance().getTime()));
            addressTxt.setText("");
            textTxt.setText("");
            recordTimeTxt.setText("");
            moneyTxt.setFocusable(true);
            moneyTxt.requestFocus();

            deleteBtn.setVisibility(View.GONE);
            outBtn.performClick();

        } else if (Conntext.UPD_TXT.equals(mode)) {

            rowId = beam.getRowId();
            moneyTxt.setText(beam.getMoney());
            timeTxt.setText(beam.getDate());
            addressTxt.setText(beam.getAddress());
            textTxt.setText(beam.getText());
            recordTimeTxt.setText(beam.getRecordTime());

            deleteBtn.setVisibility(View.VISIBLE);

            if ("0".equals(beam.getType())) {
                inBtn.performClick();
            } else if ("1".equals(beam.getType())) {
                outBtn.performClick();
            }

        }

    }

    private void getData() {

        beam = new CountUpBeam();

        beam.setRowId(rowId);
        beam.setType(type);
        beam.setMoney(moneyTxt.getText().toString());
        beam.setDate(timeTxt.getText().toString());
        beam.setAddress(addressTxt.getText().toString());
        beam.setText(textTxt.getText().toString());
        beam.setRecordTime(new SimpleDateFormat(Conntext.YYYYMMDD_HHMMSS).format(Calendar.getInstance().getTime()));

    }

    private void clearTxtBg () {

        moneyTxt.setBackgroundColor(getResources().getColor(R.color.white));
        timeTxt.setBackgroundColor(getResources().getColor(R.color.white));
        addressTxt.setBackgroundColor(getResources().getColor(R.color.white));
        textTxt.setBackgroundColor(getResources().getColor(R.color.white));

    }

    private boolean checkData () {

        if ("".equals(moneyTxt.getText().toString().trim())) {
            Toast.makeText(this, Conntext.MONEY_MSG, Toast.LENGTH_SHORT).show();
            moneyTxt.setBackground(getResources().getDrawable(R.drawable.money_stroke));
            return false;
        }

        return true;
    }

    public class ThreadHandler extends Handler {

        public ThreadHandler() {
            super();
        }

        @Override
        public void handleMessage(Message msg) {

            //登录更新删除完了
            if (msg.what == 1) {

                //删除完了
                if (Conntext.DEL_TXT.equals(mode)) {
                    Toast.makeText(CountActivity.this, Conntext.DELETE_COMPLETE, Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }

                mode = Conntext.UPD_TXT;
                Toast.makeText(CountActivity.this, Conntext.SAVE_COMPLETE, Toast.LENGTH_SHORT).show();

            }

            setData();

        }

    }

    public class DbAccessThread implements Runnable {

        public DbAccessThread() {
            super();
        }

        @Override
        public void run() {

            if ("find".equals(Thread.currentThread().getName())) {

                beam = new CountUpBeam();
                dataAccess.findDataById(rowId, beam);

                msg = new Message();
                msg.what = 2;
                threadHandler.sendEmptyMessage(msg.what);

            } else {

                if (Conntext.INS_TXT.equals(mode)) {
                    dataAccess.insert(beam);
                    beam = new CountUpBeam();
                    dataAccess.findRecentData(beam);
                } else if (Conntext.UPD_TXT.equals(mode)) {
                    dataAccess.update(beam);
                } else if (Conntext.DEL_TXT.equals(mode)) {
                    dataAccess.delete(rowId);
                }

                msg = new Message();
                msg.what = 1;
                threadHandler.sendEmptyMessage(msg.what);

            }

        }

    }

}



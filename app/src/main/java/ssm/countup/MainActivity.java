package ssm.countup;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private static final String[] week = new String[]{"", "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

    private ViewPager inoutViewPage = null;
    private RadioGroup viewpageGroup = null;
    private RadioButton viewpageSel1 = null;
    private RadioButton viewpageSel2 = null;

    private Button newCountBtn = null;
    private Button searchItemBtn = null;

    private TextView welcomeTitle = null;
    private TextView inoutTitle = null;
    private TextView moneyInput = null;
    private TextView timeInput = null;
    private TextView addressInput = null;
    private TextView textInput = null;
    private TextView recentInput = null;

    private ImageView startView = null;
    private TextView startTxt = null;

    private CountUpBeam beam = null;
    private DataAccess dataAccess = null;

    private Thread dbThread = null;
    private DbAccessThread dbAccessThread = null;
    private ThreadHandler threadHandler = null;
    private Message msg = null;

    private ArrayList<TotalFragment> totals = null;
    private TotalFragment inTotalFragment = null;
    private TotalFragment outTotalFragment = null;
    private TotalPageAdp totalPageAdp = null;

    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {

            case R.id.viewpageSel1:
                inoutViewPage.setCurrentItem(0);
                break;
            case R.id.viewpageSel2:
                inoutViewPage.setCurrentItem(1);
                break;
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.newCountBtn:

                Intent intent = new Intent(MainActivity.this, CountActivity.class);
                intent.putExtra(Conntext.MODE_TXT, Conntext.INS_TXT);
                startActivity(intent);
                break;

            case R.id.searchItemBtn:

                Intent intent2 = new Intent(MainActivity.this, ItemActivity.class);
                startActivity(intent2);
                break;

        }

    }

    @Override
    public void onResume() {

        super.onResume();

        dbThread = new Thread(dbAccessThread, "onResume");
        dbThread.start();

    }

    @Override
    public void onBackPressed() {

        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), Conntext.END_MESSAGE,
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {

        threadHandler.removeCallbacks(dbAccessThread);
        super.onDestroy();
    }

    private void init() {

        inoutViewPage = (ViewPager) findViewById(R.id.inoutViewPage);
        viewpageGroup = (RadioGroup) findViewById(R.id.viewpageGroup);
        viewpageSel1 = (RadioButton) findViewById(R.id.viewpageSel1);
        viewpageSel2 = (RadioButton) findViewById(R.id.viewpageSel2);

        newCountBtn = (Button) findViewById(R.id.newCountBtn);
        searchItemBtn = (Button) findViewById(R.id.searchItemBtn);

        welcomeTitle = (TextView) findViewById(R.id.welcomeTitle);
        inoutTitle = (TextView) findViewById(R.id.inoutTitle);
        moneyInput = (TextView) findViewById(R.id.moneyInput);
        timeInput = (TextView) findViewById(R.id.timeInput);
        addressInput = (TextView) findViewById(R.id.addressInput);
        textInput = (TextView) findViewById(R.id.textInput);
        recentInput = (TextView) findViewById(R.id.recentInput);

        startView = (ImageView)findViewById(R.id.startView);
        startTxt = (TextView) findViewById(R.id.startTxt);
        startView.animate().setDuration(1000).setStartDelay(2000).alpha(0).start();
        startTxt.animate().setDuration(1000).setStartDelay(2000).alpha(0).start();
        startView.postDelayed(new Runnable() {
            @Override
            public void run() {
                startView.setVisibility(View.GONE);
                startTxt.setVisibility(View.GONE);
            }
        }, 3000);

        welcomeTitle.setText(new SimpleDateFormat("yyyy年MM月dd日").format(Calendar.getInstance().getTime())
            + " " + week[Calendar.getInstance().get(Calendar.DAY_OF_WEEK)]);

        viewpageGroup.setOnCheckedChangeListener(this);
        newCountBtn.setOnClickListener(this);
        searchItemBtn.setOnClickListener(this);

        dataAccess = new DataAccess(this, Conntext.DB_NAME, null, 1);

        dbAccessThread = new DbAccessThread();
        threadHandler = new ThreadHandler();

        totals = new ArrayList<TotalFragment>();
        inTotalFragment = new TotalFragment();
        outTotalFragment = new TotalFragment();
        totals.add(outTotalFragment);
        totals.add(inTotalFragment);
        totalPageAdp = new TotalPageAdp(getSupportFragmentManager(), totals);
        inoutViewPage.setAdapter(totalPageAdp);
        inoutViewPage.setCurrentItem(0);
        inoutViewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

                //翻页完了
                if (state == 2) {

                    switch (inoutViewPage.getCurrentItem()) {

                        case 0:
                            viewpageSel1.setChecked(true);
                            break;
                        case 1:
                            viewpageSel2.setChecked(true);
                    }
                }

            }
        });

        viewpageSel1.setChecked(true);

    }

    private void setRecentData () {

        if ("0".equals(beam.getType())) {
            inoutTitle.setText(Conntext.IN_TXT);
            inoutTitle.setTextColor(getResources().getColor(R.color.red));
        } else if ("1".equals(beam.getType())) {
            inoutTitle.setText(Conntext.OUT_TXT);
            inoutTitle.setTextColor(getResources().getColor(R.color.green));
        }

        moneyInput.setText(beam.getMoney());
        timeInput.setText(beam.getDate());
        addressInput.setText(beam.getAddress());
        textInput.setText(beam.getText());
        recentInput.setText(beam.getRecordTime());
    }

    private void setTotalData(Bundle inData, Bundle outData) {

        if (inData != null) {
            inTotalFragment.setData(inData);
        }
        if (outData != null) {
            outTotalFragment.setData(outData);
        }

    }

    public class ThreadHandler extends Handler {

        public ThreadHandler() {
            super();
        }

        @Override
        public void handleMessage(Message msg) {

            //最近的记账
            if (msg.what == 1) {
                setRecentData();

                //总收入
            } else  if (msg.what == 2) {

                Bundle inData = msg.getData();
                setTotalData(inData, null);

            } else  if (msg.what == 3) {

                Bundle outData = msg.getData();
                setTotalData(null, outData);

            }

        }

    }

    public class DbAccessThread implements Runnable {

        public DbAccessThread() {
            super();
        }

        @Override
        public void run() {

            //最近账单
            beam = new CountUpBeam();
            dataAccess.findRecentData(beam);

            msg = new Message();
            msg.what = 1;
            threadHandler.sendEmptyMessage(msg.what);

            //总收入
            String today = new SimpleDateFormat(Conntext.YYYY_MM_DD).format(Calendar.getInstance().getTime());
            String todayTotal = dataAccess.findInTodayTotalData(today);
            String monthTotal = dataAccess.findInMonthTotalData(today);
            String yearTotal = dataAccess.findInYearTotalData(today);

            todayTotal = Conntext.IN_TODAY_TOTAL + todayTotal;
            monthTotal = Conntext.IN_MONTH_TOTAL + monthTotal;
            yearTotal = Conntext.IN_YEAY_TOTAL + yearTotal;

            Bundle total = new Bundle();
            total.putString("type", "0");
            total.putString("todayTotal", todayTotal);
            total.putString("monthTotal", monthTotal);
            total.putString("yearTotal", yearTotal);

            msg = new Message();
            msg.setData(total);
            msg.what = 2;
            threadHandler.sendMessage(msg);

            //总支出
            todayTotal = dataAccess.findOutTodayTotalData(today);
            monthTotal = dataAccess.findOutMonthTotalData(today);
            yearTotal = dataAccess.findOutYearTotalData(today);

            todayTotal = Conntext.OUT_TODAY_TOTAL + todayTotal;
            monthTotal = Conntext.OUT_MONTH_TOTAL + monthTotal;
            yearTotal = Conntext.OUT_YEAY_TOTAL + yearTotal;

            total = new Bundle();
            total.putString("type", "1");
            total.putString("todayTotal", todayTotal);
            total.putString("monthTotal", monthTotal);
            total.putString("yearTotal", yearTotal);

            msg = new Message();
            msg.setData(total);
            msg.what = 3;
            threadHandler.sendMessage(msg);

        }
    }
}

package ssm.countup;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ItemActivity extends AppCompatActivity implements View.OnClickListener,
        ExpandableListView.OnChildClickListener,
        AdapterView.OnItemSelectedListener {

    private Button lastYearBtn = null;
    private Button nextYearBtn = null;
    private TextView yearTitle = null;
    private Spinner selYearList = null;
    private ExpandableListView itemListView = null;

    private int year = 0;
    private int selectMonth = 0;

    private ArrayList<String> yearList = null;
    private ItemAdp  itemAdp = null;
    private ArrayList<ListMonthBeam> listMonthBeams = null;
    private ArrayList<ListDayBeam> listDayBeams = null;
    private ListMonthBeam listMonthBeam = null;

    private Thread searchThread = null;
    private SearchItemThread searchItemThread = null;
    private SearchHandler searchHandler = null;
    private Message msg = null;

    private DataAccess dataAccess = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itme);
        init();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.lastYearBtn:

                year = year - 1;
                yearTitle.setText(year + "年");
                selYearList.setSelection(yearList.indexOf(year + "年"));
                break;

            case R.id.nextYearBtn:

                year = year + 1;
                yearTitle.setText(year + "年");
                selYearList.setSelection(yearList.indexOf(year + "年"));
                break;

        }

    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

        selectMonth = groupPosition;
        Intent intent = new Intent(ItemActivity.this, CountActivity.class);
        intent.putExtra(Conntext.MODE_TXT, Conntext.UPD_TXT);
        intent.putExtra(Conntext.ROW_ID, listMonthBeams.get(groupPosition).getListDayBeams().get(childPosition).getRowId());
        startActivity(intent);
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        yearTitle.setText(yearList.get(position));
        year = Integer.parseInt(yearList.get(position).substring(0, 4));
        listMonthBeams = new ArrayList<ListMonthBeam>();
        listDayBeams = new ArrayList<ListDayBeam>();
        searchThread = new Thread(searchItemThread, "onItemSelected");
        searchThread.start();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override
    protected void onResume() {

        super.onResume();
        listMonthBeams = new ArrayList<ListMonthBeam>();
        listDayBeams = new ArrayList<ListDayBeam>();
        searchThread = new Thread(searchItemThread, "onResume");
        searchThread.start();
    }

    @Override
    protected void onDestroy() {

        searchHandler.removeCallbacks(searchItemThread);
        super.onDestroy();
    }

    public void init() {

        lastYearBtn = (Button) findViewById(R.id.lastYearBtn);
        nextYearBtn = (Button) findViewById(R.id.nextYearBtn);
        yearTitle = (TextView) findViewById(R.id.yearTitle);
        selYearList = (Spinner) findViewById(R.id.selYear);
        itemListView = (ExpandableListView) findViewById(R.id.itemListView);
        itemListView.setGroupIndicator(null);

        lastYearBtn.setOnClickListener(this);
        nextYearBtn.setOnClickListener(this);
        itemListView.setOnChildClickListener(this);
        selYearList.setOnItemSelectedListener(this);

        year = Integer.parseInt(new SimpleDateFormat(Conntext.YYYY).format(Calendar.getInstance().getTime()));
        yearTitle.setText(year + "年");
        selectMonth = Integer.parseInt(new SimpleDateFormat(Conntext.MM).format(Calendar.getInstance().getTime()))-1;

        yearList = new ArrayList<String>();
        for (int i=-5; i<5; i++) {
            yearList.add(year + i + "年");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_view, yearList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selYearList.setAdapter(adapter);
        selYearList.setSelection(yearList.indexOf(year + "年"));

        searchHandler = new SearchHandler();
        searchItemThread = new SearchItemThread();
        dataAccess = new DataAccess(this, Conntext.DB_NAME, null, 1);

    }

    public class SearchHandler extends Handler {

        public SearchHandler() {
            super();
        }

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);

            if (msg.what == 1) {

                itemAdp = new ItemAdp(ItemActivity.this, listMonthBeams);
                itemListView.setAdapter(itemAdp);
                for (;;) {
                    if (itemAdp.getGroupCount() > selectMonth) {
                        itemListView.expandGroup(selectMonth);
                        break;
                    }
                }
            }
        }
    }

    public class SearchItemThread implements Runnable {

        public SearchItemThread() {
            super();
        }

        @Override
        public void run() {

            String monthStr = "";
            String inTotal = "";
            String outTotal = "";

            for (int i=1; i<=12; i++) {

                monthStr = year + "-" + new DecimalFormat("00").format(i);

                inTotal = dataAccess.findInMonthTotalData(monthStr);
                outTotal = dataAccess.findOutMonthTotalData(monthStr);

                listDayBeams = new ArrayList<ListDayBeam>();
                dataAccess.findDataByMonth(monthStr, listDayBeams);

                listMonthBeam = new ListMonthBeam();
                listMonthBeam.setInMoney(inTotal);
                listMonthBeam.setOutMoney(outTotal);
                listMonthBeam.setMonth(i+"月");
                listMonthBeam.setListDayBeams(listDayBeams);
                listMonthBeams.add(listMonthBeam);
            }

            msg = new Message();
            msg.what = 1;
            searchHandler.sendEmptyMessage(msg.what);

        }
    }
}

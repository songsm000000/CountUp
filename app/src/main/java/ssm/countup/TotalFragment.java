package ssm.countup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TotalFragment extends Fragment {

    private TextView todayTotal = null;
    private TextView monthTotal = null;
    private TextView yearTotal = null;
    private Bundle data = null;

    public TotalFragment() {
        super();
    }

    public void setData(Bundle data) {

        this.data = data;

        for (;;) {

            if (todayTotal != null) {

                todayTotal.setText(data.getString("todayTotal"));
                monthTotal.setText(data.getString("monthTotal"));
                yearTotal.setText(data.getString("yearTotal"));

                if ("0".equals(data.getString("type"))) {
                    todayTotal.setTextColor(getResources().getColor(R.color.red));
                    monthTotal.setTextColor(getResources().getColor(R.color.red));
                    yearTotal.setTextColor(getResources().getColor(R.color.red));
                } else if ("1".equals(data.getString("type"))) {
                    todayTotal.setTextColor(getResources().getColor(R.color.green));
                    monthTotal.setTextColor(getResources().getColor(R.color.green));
                    yearTotal.setTextColor(getResources().getColor(R.color.green));
                }

                break;
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_total, container, false);

        todayTotal = (TextView) view.findViewById(R.id.todayTotal);
        monthTotal = (TextView) view.findViewById(R.id.monthTotal);
        yearTotal = (TextView) view.findViewById(R.id.yearTotal);

        todayTotal.setTextColor(getResources().getColor(R.color.grey));
        monthTotal.setTextColor(getResources().getColor(R.color.grey));
        yearTotal.setTextColor(getResources().getColor(R.color.grey));

        todayTotal.setBackgroundColor(getResources().getColor(R.color.white));
        monthTotal.setBackgroundColor(getResources().getColor(R.color.white));
        yearTotal.setBackgroundColor(getResources().getColor(R.color.white));

        return view;

    }

}

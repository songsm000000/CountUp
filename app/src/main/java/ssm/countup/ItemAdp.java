package ssm.countup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemAdp extends BaseExpandableListAdapter {

    private Context context = null;
    private ArrayList<ListMonthBeam> listMonthBeams = null;

    public ItemAdp(Context context, ArrayList<ListMonthBeam> listMonthBeams) {
        super();
        this.context = context;
        this.listMonthBeams = listMonthBeams;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_day, parent, false);
        }

        RelativeLayout listDayView = (RelativeLayout) convertView.findViewById(R.id.listDayView);
        TextView dayList = (TextView) convertView.findViewById(R.id.dayList);
        TextView typeTitle = (TextView) convertView.findViewById(R.id.typeTitle);
        TextView moneyTxt = (TextView) convertView.findViewById(R.id.moneyTxt);
        TextView conTxt = (TextView) convertView.findViewById(R.id.conTxt);

        dayList.setText(listMonthBeams.get(groupPosition).getListDayBeams().get(childPosition).getDay());
        if ("0".equals(listMonthBeams.get(groupPosition).getListDayBeams().get(childPosition).getType())) {
            typeTitle.setText(Conntext.IN_TXT);
            typeTitle.setTextColor(context.getResources().getColor(R.color.red));
        } else if ("1".equals(listMonthBeams.get(groupPosition).getListDayBeams().get(childPosition).getType())) {
            typeTitle.setText(Conntext.OUT_TXT);
            typeTitle.setTextColor(context.getResources().getColor(R.color.green));
        }
        moneyTxt.setText(listMonthBeams.get(groupPosition).getListDayBeams().get(childPosition).getMoney());
        conTxt.setText(listMonthBeams.get(groupPosition).getListDayBeams().get(childPosition).getCon());

        if (childPosition % 2 == 1) {
            listDayView.setBackground(context.getResources().getDrawable(R.drawable.list_press1));
        } else {
            listDayView.setBackground(context.getResources().getDrawable(R.drawable.list_press2));
        }

        return convertView;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_month, parent, false);
        }

        RelativeLayout listMonthView = (RelativeLayout) convertView.findViewById(R.id.listMonthView);
        TextView monthList = (TextView) convertView.findViewById(R.id.monthList);
        TextView inTxt = (TextView) convertView.findViewById(R.id.inTxt);
        TextView outTxt = (TextView) convertView.findViewById(R.id.outTxt);

        monthList.setText(listMonthBeams.get(groupPosition).getMonth());
        inTxt.setText(listMonthBeams.get(groupPosition).getInMoney());
        outTxt.setText(listMonthBeams.get(groupPosition).getOutMoney());

        listMonthView.setBackground(context.getResources().getDrawable(R.drawable.list_press3));

        return convertView;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listMonthBeams.get(groupPosition);
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listMonthBeams.get(groupPosition).getListDayBeams().size();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listMonthBeams.get(groupPosition).getListDayBeams().get(childPosition);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public int getGroupCount() {
        return listMonthBeams.size();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
}

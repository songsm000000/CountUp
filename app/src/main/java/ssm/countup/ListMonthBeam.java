package ssm.countup;

import java.util.ArrayList;

public class ListMonthBeam {

    private String month = "";
    private String inMoney = "";
    private String outMoney = "";
    private ArrayList<ListDayBeam> listDayBeams = new ArrayList<ListDayBeam>();

    public String getMonth() {
        return this.month;
    }

    public String getInMoney() {
        return this.inMoney;
    }

    public String getOutMoney() {
        return this.outMoney;
    }

    public ArrayList<ListDayBeam> getListDayBeams() {
        return this.listDayBeams;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setInMoney(String inMoney) {
        this.inMoney = inMoney;
    }

    public void setOutMoney(String outMoney) {
        this.outMoney = outMoney;
    }

    public void setListDayBeams(ArrayList<ListDayBeam> listDayBeams) {
        this.listDayBeams = listDayBeams;
    }
}

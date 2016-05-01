package ssm.countup;

public class ListDayBeam {

    private int rowId = 0;
    private String day = "";
    private String type = "";
    private String money = "";
    private String con = "";

    public int getRowId() {
        return this.rowId;
    }

    public String getDay() {
        return this.day;
    }

    public String getType() {
        return this.type;
    }

    public String getMoney() {
        return this.money;
    }

    public String getCon() {
        return this.con;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public void setCon(String con) {
        this.con = con;
    }
}

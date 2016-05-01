package ssm.countup;


public class CountUpBeam {

    int rowId = 0;
    String type = "";
    String money = "";
    String date = "";
    String address = "";
    String text = "";
    String recordTime = "";

    public CountUpBeam() {
    }

    public int getRowId() {
        return this.rowId;
    }

    public String getType() {
        return this.type;
    }

    public String getMoney() {
        return this.money;
    }

    public String getDate() {
        return this.date;
    }

    public String getAddress() {
        return this.address;
    }

    public String getText() {
        return this.text;
    }

    public String getRecordTime() {
        return this.recordTime;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

}

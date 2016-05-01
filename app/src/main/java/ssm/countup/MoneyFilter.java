package ssm.countup;

import android.text.InputFilter;
import android.text.Spanned;

public class MoneyFilter implements InputFilter {

    private static final double MAX_VALUE = 99999.99;
    private static final int PONTINT_LENGTH = 2;

    public MoneyFilter() {
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        if (dest.length() <= 0 && ".".equals(source.toString())) {
            return "";
        }

        if (dest.toString().contains(".")) {
            if (dest.toString().split("\\.").length > 1) {
                if (dest.toString().split("\\.")[1].length() >= PONTINT_LENGTH) {
                    return "";
                }
            }
        }

        if (dest.length() > 0) {
            if (Double.parseDouble(dest.toString() + source) > MAX_VALUE) {
                return "";
            }
        }

        return null;
    }
}

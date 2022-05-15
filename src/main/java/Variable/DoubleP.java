package Variable;

import item.Check;
import item.Setting;
import item.VariableWork;
import print.ScannerP;

public class DoubleP extends Setting implements Check, VariableWork {

    private static final String SPECIFIED = "ㅇㅆㅇ";

    @Override
    public boolean check(String line) {
        return line.strip().startsWith(SPECIFIED);
    }

    @Override
    public void start(String line) {
        int start = line.indexOf(SPECIFIED) + SPECIFIED.length();
        int end = line.indexOf(":");
        String key = line.substring(start, end).strip();
        String value = line.substring(end+1);
        value = scannerP.start(value);
        DM.put(key, Double.valueOf(value));
    }
}

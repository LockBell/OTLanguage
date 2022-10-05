package bin.apply.sys.run;

import bin.check.VariableCheck;
import bin.exception.MatchException;
import bin.token.Token;
import work.StartWork;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bin.token.cal.NumberToken.INT;

public class Sleep implements Token, StartWork {
    private final String type;
    private final Matcher matcher;

    public Sleep(String type) {
        this.type = type;
        String patternText = startEndMerge(type, BLANKS, INT);
        this.matcher = Pattern.compile(patternText).matcher("");
    }

    @Override
    public boolean check(String line) {
        return matcher.reset(line).find();
    }

    @Override
    public void start(String line, String origen,
                      Map<String, Map<String, Object>>[] repositoryArray) {
        String lines = line.strip().replaceFirst(START+type+BLANK, "");
        if (!VariableCheck.isLong(lines)) throw MatchException.grammarError();
        long time = Long.parseLong(lines);
        if (time < 0) throw MatchException.grammarError();
        try {
            Thread.sleep(time);
        } catch (InterruptedException ignored) {}
    }
}

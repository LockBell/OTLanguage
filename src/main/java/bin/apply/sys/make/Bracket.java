package bin.apply.sys.make;

import bin.apply.sys.run.LoopBracket;
import bin.exception.FileException;
import bin.exception.MatchException;
import bin.token.LoopToken;
import bin.token.Token;

import java.io.File;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Bracket implements LoopToken, Token {
    private final String endPattern = MR + "(" + BLANK + RETURN + ")?";
    private final String loopStartPattern =
            orMerge(START, "\\n") + LINE_NUMBER + BLANK + orMerge(LOOP_SET) + "[^\\n]*\\{?(?=\\s*(\\n|$))";
    private final String loopEndPattern =
            orMerge(START, "\\n") + LINE_NUMBER + BLANK + endPattern + "(?=\\s*(\\n|$))";
    private final Pattern pattern = Pattern.compile(orMerge(loopStartPattern, loopEndPattern));
    private final Stack<Integer> stack = new Stack<>();

    public String bracket(String total, File file) {
        return bracket(total, file.getName(), true);
    }

    public String bracket(String total, String fileName, boolean fileCheck) {
        total = total.replaceAll("(^|\\n)" + LINE_NUMBER + "\\s*(?=(\\n|$))", "");
        String copy = total;

        if (fileCheck) {
            fileName = fileName.split(DOT, 2)[0];
            if (LOOP_TOKEN.containsKey(fileName)) throw FileException.alreadyExistsFileName();
            LOOP_TOKEN.put(fileName, total);
        }

        if (!(copy.contains("{") && copy.contains("}"))) return copy;
        stack.clear();
        Matcher matcher = pattern.matcher(total);
        while (matcher.find()) {
            String group = matcher.group().strip();
            if (Pattern.compile(endPattern + BLANK + END).matcher(group).find()) {
                if (stack.isEmpty()) {
                    StartLine.setError(group, total);
                    throw MatchException.bracketMatchError();
                } else if (stack.size() == 1) {
                    int start = stack.pop();
                    int end = matcher.end();
                    String oldValue = total.substring(start, end);
                    String newValue = "(" + fileName + "," + (start+1) + "," + (end-1) + ")";
                    copy = copy.replace(oldValue, newValue);
                } else stack.pop();
            } else {
                if (!group.endsWith("{")) {
                    StartLine.setError(group, total);
                    throw MatchException.loopStyleError();
                } else stack.add(matcher.end()-1);
            }
        }
        if (!stack.isEmpty()) getErrorLine(total, stack.pop());
        return new LoopBracket().deleteEnter(copy);
    }

    private void getErrorLine(String total, int pos) {
        String[] lines = total.substring(0, pos).split("\\n");
        String line = lines[lines.length-1];
        StartLine.setError(line, total);
        throw MatchException.bracketMatchError();
    }
}
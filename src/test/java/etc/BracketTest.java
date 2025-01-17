package etc;

import bin.exception.FileException;
import bin.exception.MatchException;
import bin.token.MergeToken;

import java.io.File;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bin.apply.sys.item.Separator.SEPARATOR_LINE;
import static bin.apply.sys.make.StartLine.errorCount;
import static bin.apply.sys.make.StartLine.errorLine;
import static bin.token.LoopToken.*;

public class BracketTest implements MergeToken {
    private final String startPatternText = String.format("(?<=(^|%s)\\d+ )[^%s]*\\{",
            SEPARATOR_LINE, SEPARATOR_LINE);
    private final String endPatternText = String.format("(?<=%s\\d+ )}\\s*(%s|%s)?\\s*(?=%s|$)",
            SEPARATOR_LINE, PUTIN, RETURN, SEPARATOR_LINE);
    private final String patternText = String.format("(%s|%s)", startPatternText, endPatternText);
    private final Stack<Integer> stack = new Stack<>();
    private final Matcher matcher = Pattern.compile(patternText).matcher("");

    public String bracket(String total, File file) {
        return bracket(total, file.getName(), true);
    }

    public String bracket(String total, String fileName, boolean fileCheck) {
        if (fileCheck) {
            total = total.replaceAll("(^|\\n)" + LINE_NUMBER + "\\s*(?=(\\n|$))", "");
            int fileEx = fileName.lastIndexOf('.'); // 확장자 위치
            if (fileEx != -1) fileName = fileName.substring(0, fileEx);
            if (LOOP_TOKEN.containsKey(fileName)) throw new FileException().alreadyExistsFileName();
            LOOP_TOKEN.put(fileName, total);
        }

        if (!(total.contains("{") && total.contains("}"))) return total;
        stack.clear();
        matcher.reset(total);
        while (matcher.find()) {
            String group = matcher.group();
            if (startCheck(group)) stack.add(matcher.end());
            else if (group.startsWith("}")) {
                if (stack.isEmpty()) {
                    int a = cutLine(total, matcher.start());
                    int b = total.indexOf(SEPARATOR_LINE, a);
                    throwError(total.substring(a, b));
                } else if (stack.size() == 1) {
                    int start = stack.pop() - 1;
                    int end = matcher.start() + 1;
                    String newWord = String.format(" (%s,%s,%s) ", fileName, startLine(total, start), endLine(total, end));
                    total = total.replace(total.substring(start, end), newWord);
                    matcher.reset(total);
                } else stack.pop();
            }
        }
        if (!stack.isEmpty()) {
            throwError(total, stack.pop());
            throw new MatchException().bracketMatchError();
        }

        return total;
    }

    // (...) {\n14
    private String startLine(String total, int start) {
        int s = total.indexOf(SEPARATOR_LINE, start) + 1;
        int e = total.indexOf(' ', s);
        return total.substring(s, e);
    }

    private String endLine(String total, int end) {
        int cut = cutLine(total, end);
        int start = total.indexOf(' ', cut);
        return total.substring(cut, start);
    }

    // \n \r
    private final char lineToken = SEPARATOR_LINE.charAt(0);
    private int cutLine(String builder, int start) {
        while (true) {
            if (builder.charAt(--start) == lineToken) return start + 1;
            else if (start <= 0) return 0;
        }
    }

    // $ㅅ$ ㅇㅇ {
    private boolean startCheck(String line) {
        if (!line.strip().endsWith("{")) return false;
        for (String loopSet : LOOP_SET) {
            if (line.startsWith(loopSet)) return true;
        }
        return line.chars().filter(v -> v == '^').count() == 2;
    }

    private void throwError(String total, int s) {
        String line = total.substring(cutLine(total, s), s);
        throwError(line);
    }

    private void throwError(String line) {
        int start = line.indexOf(' ');
        errorCount.set(Long.parseLong(line.substring(0, start)));
        errorLine.set(line.substring(start + 1));
        throw new MatchException().bracketMatchError();
    }
}

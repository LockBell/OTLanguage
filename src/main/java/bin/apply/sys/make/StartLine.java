package bin.apply.sys.make;

import bin.apply.Setting;
import bin.exception.MatchException;
import bin.exception.VariableException;

import java.io.File;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bin.apply.Controller.bracket;
import static bin.apply.sys.item.SystemSetting.extensionCheck;
import static bin.token.Token.START;

public class StartLine {
    private static final String patternText = START + "[0-9]+( |$)";
    private static final Pattern pattern = Pattern.compile(patternText);

    @SafeVarargs
    public static void startLine(String total, String path,
                                 Map<String, Map<String, Object>>... repository) {
        boolean extensionCheck = extensionCheck(path);
        if (extensionCheck) errorPath.set(path);
        try {
            String finalTotal =
                    extensionCheck
                            ? bracket.bracket(total, new File(path))
                            : bracket.bracket(total, path, false);
            Pattern.compile("\\n")
                    .splitAsStream(finalTotal)
                    .map(line -> setError(line, total))
                    .forEach(line -> Setting.start(line, errorLine.get(), repository));
        } catch (VariableException e) {
            VariableException.variableErrorMessage(e, errorPath.get(), errorLine.get(), errorCount.get());
        } catch (MatchException e) {
            MatchException.matchErrorMessage(e, errorPath.get(), errorLine.get(), errorCount.get());
        }
    }
    /**
     * @see bin.apply.sys.run.FilePath
     */

    private static final AtomicLong errorCount = new AtomicLong(0);
    private static final AtomicReference<String> errorLine = new AtomicReference<>("");
    private static final AtomicReference<String> errorPath = new AtomicReference<>();
    public static String setError(String line, String total) {
        Matcher matcher = pattern.matcher(line);
        String lineNum = "0";
        if (matcher.find()) {
            lineNum = matcher.group().trim();
            errorCount.set(Long.parseLong(lineNum));
        }

        for (var lines : total.split("\\n")) {
            if (lines.startsWith(lineNum + " ")) {
                errorLine.set(lines.replaceFirst(patternText, ""));
                break;
            }
        }

        return line.replaceFirst(patternText, "");
    }
}
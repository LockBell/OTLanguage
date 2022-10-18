package bin.apply.sys.make;

import bin.apply.Repository;
import bin.apply.Setting;
import bin.apply.sys.item.RunType;
import bin.exception.ConsoleException;
import bin.exception.MatchException;
import bin.exception.ServerException;
import bin.exception.VariableException;
import bin.token.LoopToken;
import com.sun.tools.javac.Main;
import org.apache.hadoop.hdfs.web.JsonUtil;

import java.io.File;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bin.apply.Controller.bracket;
import static bin.apply.Controller.loopController;
import static bin.apply.sys.item.SystemSetting.extensionCheck;
import static cos.poison.Poison.variableHTML;

public class StartLine implements LoopToken {
    private static final String patternText = START + "[0-9]+( |$)";
    private static final Pattern pattern = Pattern.compile(patternText);

    @SafeVarargs
    public static void startLine(String total, String path,
                                 Map<String, Map<String, Object>>... repository) {
        boolean extensionCheck = extensionCheck(path);
        if (extensionCheck) errorPath.set(path);
        try {
            String finalTotal = getFinalTotal(extensionCheck, total, path);
            startStartLine(finalTotal, total, repository);
//            Pattern.compile("\\n")
//                    .splitAsStream(finalTotal)
//                    .filter(Predicate.not(String::isBlank))
//                    .map(line -> setError(line, total))
//                    .forEach(line -> Setting.start(line, errorLine.get(), repository));
        } catch (VariableException e) {
            VariableException.variableErrorMessage(e, errorPath.get(), errorLine.get(), errorCount.get());
            setLine();
        } catch (MatchException e) {
            MatchException.matchErrorMessage(e, errorPath.get(), errorLine.get(), errorCount.get());
            setLine();
        } catch (ServerException e) {
            ServerException.serverErrorMessage(e, errorPath.get(), errorLine.get(), errorCount.get());
            setLine();
        } catch (ConsoleException e) {
            ConsoleException.consoleErrorMessage(e, errorPath.get(), errorLine.get(), errorCount.get());
            setLine();
        }
    }

    private static void setLine() {
        if (Setting.runType.equals(RunType.Normal)) System.exit(0);
    }

    public static String getFinalTotal(boolean extensionCheck, String total, String path) {
        return extensionCheck
                ? bracket.bracket(total, new File(path))
                : bracket.bracket(total, path, false);
    }

    @SafeVarargs
    public static void startStartLine(String finalTotal, String total,
                                      Map<String, Map<String, Object>>... repository) {
        Pattern.compile("\\n")
                .splitAsStream(finalTotal)
                .filter(Predicate.not(String::isBlank))
                .map(line -> setError(line, total))
                .forEach(line -> Setting.start(line, errorLine.get(), repository));
    }

    @SafeVarargs
    public static String startLoop(String total, String fileName,
                                 Map<String, Map<String, Object>>... repository) {
        for (var line : bracket.bracket(total, fileName, false).split("\\n")) {
            line = loopController.check(setError(line, total).strip());
            if (line.equals(BREAK) || line.equals(CONTINUE)) return line;
            else Setting.start(line, errorLine.get(), repository);
        }
        return "FINISH";
    }

    @SafeVarargs
    public static void startPoison(String total, String fileName,
                                     Map<String, Map<String, Object>>... repository) {
        CONTINUE:
        for (var line : bracket.bracket(total, fileName, false).split("\\n")) {
            if (line.isBlank()) continue;
            line = setError(line, total);
            final String origen = line;
            for (var work : Repository.priorityWorks) {     // 강제 동작
                if (work.check(line)) {work.start(line, origen, repository);continue CONTINUE;}}

            line = Setting.lineStart(line, repository);
            // ㅁㄷㅁ 변수명:HTML 변수명 ( HTML 변수명 등록 )
            if (variableHTML.check(line)) {variableHTML.start(line); continue;}
            for (var work : Repository.startWorks) {        // 시작 동작
                if (work.check(line)) {work.start(line, origen, repository);continue CONTINUE;}}
            Setting.runMessage(origen);
        }
    }

    public static final AtomicLong errorCount = new AtomicLong(0);
    public static final AtomicReference<String> errorLine = new AtomicReference<>("");
    public static final AtomicReference<String> errorPath = new AtomicReference<>();
    public static String setError(String line, String total) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            String lineNum = matcher.group().trim();
            errorCount.set(Long.parseLong(lineNum));

            int start = total.indexOf("\n" + lineNum + " ");
            if (start == -1 && lineNum.equals("1")) start = 0;
            errorLine.set(total.substring(start).strip().split("\\n")[0].replaceFirst("^\\d+ ", ""));
        } else errorLine.set(line);

        return line
                .replaceFirst(patternText, "")
                .strip();
    }
}

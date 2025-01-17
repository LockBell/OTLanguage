package bin.apply.sys.make;

import bin.apply.Setting;
import bin.apply.sys.item.RunType;
import bin.apply.sys.item.SystemSetting;
import bin.calculator.tool.Calculator;
import bin.exception.*;
import bin.token.LoopToken;

import java.io.File;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

import static bin.apply.Setting.debugMode;
import static bin.apply.sys.item.Separator.EXT_REP;
import static bin.apply.sys.item.Separator.isWindow;
import static bin.apply.sys.item.SystemSetting.extension;
import static bin.apply.sys.item.SystemSetting.extensionCheck;
import static bin.calculator.tool.Calculator.*;

public class StartLine implements LoopToken, Calculator {
    public static boolean developmentMode = false;
    public static void startLine(String total, String path,
                                 LinkedList<Map<String, Map<String, Object>>> repositoryArray) {
        boolean extensionCheck = extensionCheck(path);
        try {
            String finalTotal = getFinalTotal(extensionCheck, total, path);
            startStartLine(finalTotal, total, repositoryArray);
        } catch (VariableException | MatchException | ServerException | ConsoleException | CosException e) {
            errorMessage(e, e);
        }
    }

    private static void setLine(RuntimeException e) {
        if (developmentMode) e.printStackTrace();
        if (Setting.runType.equals(RunType.Normal)) {
            // 운영체제가 윈도우가 아니거나 디버깅모드가 컴파일이 아닐때 종료 허용
            if (!isWindow || debugMode.isNoCompile()) System.exit(0);
        }
    }

    public static void errorMessage(RuntimeException r, ExceptionMessage e) {
        String p = errorPath.get() + "." + EXT_REP.getOrDefault(errorPath.get(), extension[0]);
        e.errorMessage(r, p, errorLine.get(), errorCount.get());
        setLine(r);
    }

    public static String getFinalTotal(boolean extensionCheck, String total, String path) {
        return extensionCheck
                ? Bracket.getInstance().bracket(total, new File(path))
                : Bracket.getInstance().bracket(total, path, false);
    }

    public static void startStartLine(String finalTotal, String total,
                                      LinkedList<Map<String, Map<String, Object>>> repositoryArray) {
        finalTotal.lines()
                .filter(Predicate.not(String::isBlank))
                .map(v -> setError(v, total))
                .forEach(line -> Setting.start(line, errorLine.get(), repositoryArray));
    }

    private final static String FINISH = "FINISH";
    public static String startLoop(String total, String fileName,
                                   LinkedList<Map<String, Map<String, Object>>> repositoryArray) {
        for (var line : Bracket.getInstance().bracket(total, fileName, false).lines().toList()) {
            line = setError(line, total).strip();
            line = Setting.lineStart(line, repositoryArray).strip();

            if (line.equals(BREAK)) return BREAK;
            else if (line.equals(CONTINUE)) return CONTINUE;
            else if (line.endsWith(BREAK)) {
                if (getBoolean(line, repositoryArray)) return BREAK;
            } else if (line.endsWith(CONTINUE)) {
                if (getBoolean(line, repositoryArray)) return CONTINUE;
            } else Setting.start(line, errorLine.get(), repositoryArray);
        }
        return FINISH;
    }

    // line = ㅇㅇ ㄸ ㄴㄴ
    private static boolean getBoolean(String line, LinkedList<Map<String, Map<String, Object>>> repositoryArray) {
        int position = line.lastIndexOf('?');
        if (position == -1) throw new MatchException().grammarError();
        return getBool(line.substring(0, position), repositoryArray);
    }

    public static void startPoison(String total, String fileName,
                                   LinkedList<Map<String, Map<String, Object>>> repositoryArray) {
        for (var line : Bracket.getInstance().bracket(total, fileName, false).lines().toList()) {
            if ((line = setError(line, total)).isBlank()) continue;
            Setting.start(line, errorLine.get(), repositoryArray);
        }
    }

    public static final AtomicLong errorCount = new AtomicLong(0);
    public static final AtomicReference<String> errorLine = new AtomicReference<>("");
    public static final AtomicReference<String> errorPath = new AtomicReference<>();
    public static String setError(String line, String total) {
        if (line.isBlank()) return "";
        else if (LOOP_TOKEN.containsKey(total)) total = LOOP_TOKEN.get(total);

        int i = line.indexOf(' ');
        String lineCount = i == -1 ? line : line.substring(0, i);
        errorCount.set(Integer.parseInt(lineCount));
        if (i != -1) {
            int start = total.indexOf("\n" + lineCount + " ") + 1;
            int end = total.indexOf("\n", start);
            if (end == -1) end = total.length();
            String lines = total.substring(start + lineCount.length() + 1, end);
            errorLine.set(lines);
            return line.substring(i + 1);
        } else return "";
    }
}

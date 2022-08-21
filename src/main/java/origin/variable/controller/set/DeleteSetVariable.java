package origin.variable.controller.set;

import event.Setting;
import origin.exception.MatchException;
import origin.exception.MatchMessage;
import origin.variable.define.generic.GenericDelete;
import origin.variable.define.generic.delete.DeleteSet;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeleteSetVariable implements DeleteSet, GenericDelete {
    private final String patternText;
    private final Pattern pattern;

    // [변수명]-[key]
    public DeleteSetVariable(String patternText) {
        this.patternText = patternText; // -
        // 변수명-숫자
        this.pattern = Pattern.compile(
                "^\\s*"+ Setting.variableStyle+"\\s*" + patternText + ""+Setting.variableStyle);
    }

    @Override
    public boolean check(String line) {
        return pattern.matcher(line).find();
    }

    @Override
    public void start(String line, Map<String, Map<String, Object>> repository) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            String group = matcher.group().strip();
            String[] groups = group.split(this.patternText);
            if (groups.length != 2) throw new MatchException(MatchMessage.grammarError);
            String variableName = groups[0].trim(); //변수명
            String key = groups[1].trim(); //삭제할 키값
            deleteSet(variableName, key, repository);
        }
    }
}
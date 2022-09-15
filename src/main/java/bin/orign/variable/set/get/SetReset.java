package bin.orign.variable.set.get;

import bin.exception.MatchException;
import bin.exception.VariableException;
import bin.token.LoopToken;
import work.StartWork;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

// 내용 변경
public class SetReset implements StartWork, LoopToken, GetSet {
    private final String type;
    private final Pattern pattern;

    public SetReset(String type) {
        this.type = type;
        String patternText = startMerge(VARIABLE_ACCESS, VARIABLE_PUT);
        this.pattern = Pattern.compile(patternText);
    }

    @Override
    public boolean check(String line) {
        return pattern.matcher(line).find();
    }

    @Override
    public void start(String line, String origen,
                      Map<String, Map<String, Object>>[] repositoryArray) {
        line = line.strip();
        int count = accessCount(line);
        String[] tokens = line
                .replaceFirst(START + ACCESS + "+", "")
                .replaceFirst(type + END, "")
                .split(VARIABLE_PUT, 2);
        if (tokens.length != 2) throw VariableException.noGrammar();
        String variableName = tokens[0];

        if (count > repositoryArray.length) throw VariableException.localNoVariable();
        var repository = repositoryArray[count];
        for (Map.Entry<String, Map<String, Object>> entry : repository.entrySet()) {
            Map<String, Object> values = entry.getValue();
            if (values.containsKey(variableName)) {
                if (!SET_LIST.contains(entry.getKey())) throw MatchException.grammarError();
                Set<Object> set = (Set<Object>) values.get(variableName);
                set.clear();
                set.addAll(getSet(type, tokens[1].strip()));
            }
        }
    }

    private Set<?> getSet(String type, String value) {
        return switch (type) {
            case SET_INTEGER -> getIntegerSet(value);
            case SET_LONG -> getLongSet(value);
            case SET_BOOLEAN -> getBoolSet(value);
            case SET_CHARACTER -> getCharacterSet(value);
            case SET_FLOAT -> getFlotSet(value);
            case SET_DOUBLE -> getDoubleSet(value);
            default -> getStringSet(value);
        };
    }
}
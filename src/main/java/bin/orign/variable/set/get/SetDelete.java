package bin.orign.variable.set.get;

import bin.exception.VariableException;
import bin.token.LoopToken;
import work.StartWork;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.regex.Pattern;

public class SetDelete implements StartWork, LoopToken {
    private final String type;
    private final Pattern pattern;

    public SetDelete(String type) {
        this.type = type;
        String patternText = startEndMerge(VARIABLE_ACCESS, type, "\\d+");
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
        if (repositoryArray.length < count) throw VariableException.localNoVariable();
        line = line.substring(count);   // ~변수-1 -> 변수-1
        String[] tokens = matchSplitError(line, Pattern.quote(type), 2);
        var repository = repositoryArray[count];
        getSet(repository, tokens[0], Integer.parseInt(tokens[1]));
    }

    private void getSet(Map<String, Map<String, Object>> repository,
                         String variableName, int position) {
        for (var token : SET_LIST) {
            var rep = repository.get(token);
            if (rep.containsKey(variableName)) {
                LinkedHashSet<Object> set = (LinkedHashSet<Object>) rep.get(variableName);
                set.remove(position);
                return;
            }
        }
    }
}

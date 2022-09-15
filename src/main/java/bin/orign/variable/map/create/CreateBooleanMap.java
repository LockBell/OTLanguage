package bin.orign.variable.map.create;

import bin.apply.Repository;
import bin.exception.VariableException;
import bin.orign.variable.map.get.GetMap;
import bin.token.VariableToken;
import work.StartWork;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateBooleanMap implements
        StartWork, VariableToken, GetMap {
    private final String patternText;
    private final Pattern pattern;
    private final String type;

    public CreateBooleanMap(String type, Map<String, Map<String, Object>> repository) {
        repository.put(type, new HashMap<>());
        this.patternText = startMerge(type, BLANKS, VARIABLE_NAME);
        this.pattern = Pattern.compile(patternText);
        this.type = type;
    }

    @Override
    public boolean check(String line) {
        return pattern.matcher(line).find();
    }

    @Override
    public void start(String line, String origen,
                      Map<String, Map<String, Object>>[] repositoryArray) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            // group : VARIABLE_NAME
            String group = matcher.group().replaceFirst("^\\s*" + type + "\\s*", "");
            if (Repository.noUse.contains(group)) throw VariableException.reservedWorks();
            else if (Repository.getSet(repositoryArray[0]).contains(group)) throw VariableException.sameVariable();
            // value : 값
            String value = line.replaceFirst(patternText, "").strip();
            Map<String, String> map;
            if (value.isBlank()) map = new LinkedHashMap<>();
            else {
                if (value.startsWith(MAP_ADD)) map = getBoolMap(value.substring(MAP_ADD.length()));
                else if (value.startsWith(VARIABLE_PUT)) map = getBoolMap(value.substring(VARIABLE_PUT.length()));
                else throw VariableException.noGrammar();
            }
            repositoryArray[0].get(type).put(group, map);
        }
    }
}
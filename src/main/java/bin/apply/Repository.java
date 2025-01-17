package bin.apply;

import bin.apply.sys.item.HpMap;
import bin.orign.variable.StartVariable;
import bin.orign.variable.Variable;
import work.v3.ReturnWorkV3;
import work.v3.StartWorkV3;

import java.util.*;

import static bin.token.ConsoleToken.SCANNER;
import static bin.token.LoopToken.*;
import static bin.token.cal.BoolToken.*;
import static bin.token.cal.NumberToken.MINUS_CALCULATOR_S;

public interface Repository {
    List<String> repositoryItems = new ArrayList<>(TOTAL_LIST) {{
        add(METHOD);
    }};
    LinkedList<Map<String, Map<String, Object>>> repository = new LinkedList<>() {{
        add(new HashMap<>() {{
            repositoryItems.forEach(v -> put(v, new HpMap(v)));
        }});
    }};

    Set<String> noUse = new HashSet<>() {{
        add(SCANNER); add(TRUE); add(FALSE);
        add(NOT); add(OR); add(AND);
        add(MINUS_CALCULATOR_S);
        addAll(MAP_LIST);
    }};

    static boolean containsVariable(String variable, Map<String, Map<String, Object>> repository) {
        return TOTAL_LIST.stream().anyMatch(v -> repository.get(v).containsKey(variable));
    }

    // Version 3
    Variable variable = new Variable(1);
    StartVariable startVariable = new StartVariable(2);

    HashMap<String, Map<String, StartWorkV3>> priorityStartWorksV3 = new HashMap<>();
    HashMap<String, Map<String, StartWorkV3>> startWorksV3 = new HashMap<>();
    HashMap<String, Map<String, ReturnWorkV3>> returnWorksV3 = new HashMap<>();
}

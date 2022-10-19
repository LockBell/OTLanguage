package bin.orign;

import bin.token.LoopToken;
import bin.token.MergeToken;
import work.StartWork;

import java.util.Map;

public class CreateOrigin implements StartWork, LoopToken {

    @Override
    public boolean check(String line) {
        return false;
    }

    @Override
    public void start(String line, String origen,
                      Map<String, Map<String, Object>>[] repositoryArray) {
        String[] values = matchSplitError(line, BLANKS, 2);
        // 변수명<<1234  =>
        String[] tokens = matchSplitError(values[1], orMerge(VARIABLE_PUT, LIST_ADD), 2);
        variableDefineError(tokens[0], repositoryArray[0]);
        repositoryArray[0].get(values[1]).put(tokens[0], tokens[1]);
    }
}
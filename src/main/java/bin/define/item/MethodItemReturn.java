package bin.define.item;

import bin.CreateReturnWorks;
import bin.apply.sys.item.HpMap;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static bin.apply.Repository.repositoryItems;
import static bin.apply.sys.make.StartLine.startStartLine;
import static bin.define.item.MethodItemTool.resetRepository;
import static bin.define.item.MethodItemTool.setParams;

public class MethodItemReturn {
    public final Map<String, Map<String, Object>> repository = new HashMap<>() {{
        repositoryItems.forEach(v -> put(v, new HpMap(v)));
    }};
    private final String[][] params;
    private final String finalTotal;
    private final String fileName;
    private final String returnValue;

    public MethodItemReturn(String[][] params, String finalTotal, String fileName, String returnValue) {
        this.params = params;
        this.finalTotal = finalTotal;
        this.fileName = fileName;
        this.returnValue = returnValue;
    }

    public String start(String[] params, LinkedList<Map<String, Map<String, Object>>> repositoryArray) {
        repositoryArray.addFirst(repository);
        setParams(this.params, params, repository);
        try {
            startStartLine(finalTotal, fileName, repositoryArray);
            return CreateReturnWorks.sub(returnValue, null, repositoryArray);
        } finally {
            resetRepository(repository);
            repositoryArray.removeFirst();
        }
    }

    public int getParams() {
        return this.params.length;
    }
}

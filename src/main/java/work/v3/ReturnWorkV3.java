package work.v3;

import bin.exception.MatchException;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;

public abstract class ReturnWorkV3 implements Serializable {
    private final int[] counts;

    public ReturnWorkV3(int...counts) {
        this.counts = counts.length == 0 ? null : counts;
    }

    public abstract String start(String line, String[] params, LinkedList<Map<String, Map<String, Object>>> repositoryArray);

    public ReturnWorkV3 paramsCheck(int size, String params) {
        if (!(counts == null
                || (params != null && counts.length == 1 && counts[0] == 0 && params.isEmpty())
                || Arrays.stream(counts).anyMatch(v -> v == size))) throw new MatchException().grammarError();
        return this;
    }
}

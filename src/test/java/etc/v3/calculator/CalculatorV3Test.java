package etc.v3.calculator;

import bin.exception.MatchException;
import org.apache.arrow.flatbuf.Int;
import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.BiFunction;

public class CalculatorV3Test {
    public static void main(String[] args) {
        System.out.println(getBig("100 ㅇ+ㅇ 232 ㅇ>ㅇ 32 ㅇ=ㅇ 393ㅇ<ㅇ23ㅇ>=ㅇ안녕12ㄸㅇㅇ"));
    }

    private static List<String> getBig(String line) {
        StringTokenizer tokenizer = new StringTokenizer(line, "ㅇ", true);
        List<String> list = new ArrayList<>();
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (token.equals("ㅇ")) {
                String a = tokenizer.nextToken();
                if (a.equals("ㅇ")) list.add("ㅇㅇ");
                else if (a.equals("ㄴ")) list.add("ㅇㄴ");
                else {
                    String b = tokenizer.nextToken();
                    if (b.equals("ㅇ")) list.add(token + a + b);
                    else throw new MatchException().grammarError();
                }
            } else list.add(token.strip());
        }
        return list;
    }

}

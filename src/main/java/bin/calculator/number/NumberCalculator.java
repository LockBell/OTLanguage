package bin.calculator.number;

import bin.calculator.tool.CalculatorTool;
import bin.exception.MatchException;

import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;

import static bin.exception.MatchException.*;
import static bin.token.Token.NO_TOKEN;
import static bin.token.Token.TOKEN;
import static bin.token.cal.BoolToken.*;

public class NumberCalculator implements CalculatorTool {
    private final Stack<String> stack = new Stack<>();
    public Stack<String> getStack(String line) {
        stack.clear();
        StringTokenizer tokenizer = new StringTokenizer(line, TOKEN + OR + NO_TOKEN + AND, true);
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (token.isBlank()) continue;
            if (token.equals(TOKEN)) {
                String a = tokenizer.nextToken();
                if (a.equals(TOKEN)) stack.add(TRUE);
                else if (a.equals(NO_TOKEN)) stack.add(NOT);
                else {
                    String tokens = tokenizer.nextToken();
                    if (tokens.equals(TOKEN)) stack.add(a);
                    else throw new MatchException().grammarTypeError(token + a + tokens, GrammarTypeClass.TOKEN);
                }
            } else if (token.equals(NO_TOKEN)) {
                String tokens = tokenizer.nextToken();
                if (tokens.equals(NO_TOKEN)) stack.add(FALSE);
                else throw new MatchException().grammarTypeError(token + tokens, GrammarTypeClass.TOKEN);
            } else stack.add(token.strip());
        }
        return stack;
    }


    public void start(Stack<String> stack, LinkedList<Map<String, Map<String, Object>>> ra) {
        if (stack.size() == 1) {
            String token = stack.pop();
            double d = getDouble(token, ra);
            if (token.contains(".")) stack.add(String.valueOf(d));
            else stack.add(checkInteger(d));
            return;
        }

        int start, i;
        double total = 0;
        while (containsFirst(stack)) {
            boolean doubleCheck = true;
            for (start=getFirst(stack), i = start; (i < stack.size() && checkFirst(stack.get(i))); i+=2) {
                if (start == i) {
                    String a = stack.get(i-1);
                    if (doubleCheck && a.contains(".")) doubleCheck = false;
                    total = getDouble(a, ra);
                }
                String a = stack.get(i+1);
                if (doubleCheck && a.contains(".")) doubleCheck = false;
                total = number.get(stack.get(i)).apply(total, getDouble(a, ra));
            }
            deleteStack(start, i, stack);
            stack.set(start-1, doubleCheck ? checkInteger(total) : String.valueOf(total));
        }

        while (containsSecond(stack)) {
            boolean doubleCheck = true;
            for (start=getSecond(stack), i = start; (i < stack.size() && checkSecond(stack.get(i))); i+=2) {
                if (start == i) {
                    String a = stack.get(i-1);
                    if (doubleCheck && a.contains(".")) doubleCheck = false;
                    total = getDouble(stack.get(i-1), ra);
                }
                String a = stack.get(i+1);
                if (doubleCheck && a.contains(".")) doubleCheck = false;
                total = number.get(stack.get(i)).apply(total, getDouble(a, ra));
            }
            deleteStack(start, i, stack);
            stack.set(start-1, doubleCheck ? checkInteger(total) : String.valueOf(total));
        }
    }

    private String checkInteger(double d) {
        return (d == Math.floor(d) && !Double.isInfinite(d)) ? Long.toString((long) d) : Double.toString(d);
    }

    private void deleteStack(int start, int end, Stack<String> stack) {
        for (int v = start; v < end; v+=2) {
            stack.remove(start);
            stack.remove(start-1);
        }
    }

    private int getFirst(Stack<String> stack) {
        int value = stack.indexOf("%");
        int a = stack.indexOf("*");
        if (a != -1) value = value == -1 ? a : Math.min(value, a);
        int b = stack.indexOf("/");
        if (b != -1) value = value == -1 ? b : Math.min(value, b);
        return value;
    }

    private int getSecond(Stack<String> stack) {
        int value = stack.indexOf("-");
        int a = stack.indexOf("+");
        if (a != -1) value = value == -1 ? a : Math.min(value, a);
        return value;
    }

    private boolean checkFirst(String sing) {
        return sing.equals("*") || sing.equals("%") || sing.equals("/");
    }

    private boolean checkSecond(String sing) {
        return sing.equals("+") || sing.equals("-");
    }

    private boolean containsFirst(Stack<String> stack) {
        return stack.contains("*") || stack.contains("/") || stack.contains("%");
    }

    private boolean containsSecond(Stack<String> stack) {
        return stack.contains("+") || stack.contains("-");
    }
}

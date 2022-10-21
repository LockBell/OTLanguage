package bin.token;

import bin.exception.VariableException;

import java.util.*;

import static bin.apply.Repository.getSet;
import static bin.apply.Repository.noUse;
import static bin.check.VariableCheck.*;
import static bin.token.cal.BoolToken.FALSE;
import static bin.token.cal.NumberToken.NUMBER;

public interface VariableToken extends Token {
    String VARIABLE_HTML = "[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z$-]+[0-9ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z$-]*";
    String VARIABLE_NAME = "(\\[\\d+\\])?" + VARIABLE_HTML;
    String VARIABLE_ACCESS = ACCESS + "{0,2}" + VARIABLE_NAME;
    String VARIABLE = ":" + VARIABLE_ACCESS + "_";
    String VARIABLE_SET = VARIABLE_ACCESS + ":";
    String VARIABLE_PUT = ":";
    String VARIABLE_GET_S = ":";
    String VARIABLE_GET_E = "_";

    String FILE_TYPE = "[^~\\s]+";
    String FILE_NAME = "[0-9ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z$_-]+";

    // SYSTEM
    String FORCE_QUIT = "ㄲㅌㄲ";
    String SLEEP = "=_=";
    String FILE = "ㅍㅅㅍ";

    String NUMBER_LIST = String.join(BLANK, BL, NUMBER, "(", ",", NUMBER + ")*", BR);

    // VARIABLE
    String INT_VARIABLE = "ㅇㅈㅇ";
    String LONG_VARIABLE = "ㅇㅉㅇ";
    String BOOL_VARIABLE = "ㅇㅂㅇ";
    String STRING_VARIABLE = "ㅇㅁㅇ";
    String CHARACTER_VARIABLE = "ㅇㄱㅇ";
    String FLOAT_VARIABLE = "ㅇㅅㅇ";
    String DOUBLE_VARIABLE = "ㅇㅆㅇ";

    // SET
    String SET_INTEGER = "ㄴㅈㄴ";
    String SET_LONG = "ㄴㅉㄴ";
    String SET_BOOLEAN = "ㄴㅂㄴ";
    String SET_STRING = "ㄴㅁㄴ";
    String SET_CHARACTER = "ㄴㄱㄴ";
    String SET_FLOAT = "ㄴㅅㄴ";
    String SET_DOUBLE = "ㄴㅆㄴ";

    String SET_SORT = AMPERSAND;             // &
    String SET_CLEAR = EXCLAMATION;          // !
    String SET_DELETE = HYPHEN;              // -
    String SET_ADD = LESS_SIGN;              // <
    String SET_GET = GREATER_SIGN;           // >
    String SET_ISEMPTY = QUESTION;           // ?
    String SET_SUM = PLUS;                   // +

    // LIST
    String LIST_INTEGER = "ㄹㅈㄹ";
    String LIST_LONG = "ㄹㅉㄹ";
    String LIST_BOOLEAN = "ㄹㅂㄹ";
    String LIST_STRING = "ㄹㅁㄹ";
    String LIST_CHARACTER = "ㄹㄱㄹ";
    String LIST_FLOAT = "ㄹㅅㄹ";
    String LIST_DOUBLE = "ㄹㅆㄹ";

    String LIST_SORT = AMPERSAND.repeat(2);             // &&
    String LIST_CLEAR = EXCLAMATION.repeat(2);          // !!
    String LIST_DELETE = HYPHEN.repeat(2);              // --
    String LIST_ADD = LESS_SIGN.repeat(2);              // <<
    String LIST_GET = GREATER_SIGN.repeat(2);           // >>
    String LIST_ISEMPTY = QUESTION.repeat(2);           // ??
    String LIST_SUM = PLUS.repeat(2);                   // ++

    String MAP_INTEGER = "ㅈㅈㅈ";
    String MAP_LONG = "ㅈㅉㅈ";
    String MAP_BOOLEAN = "ㅈㅂㅈ";
    String MAP_STRING = "ㅈㅁㅈ";
    String MAP_CHARACTER = "ㅈㄱㅈ";
    String MAP_FLOAT = "ㅈㅅㅈ";
    String MAP_DOUBLE = "ㅈㅆㅈ";

    String MAP_CLEAR = EXCLAMATION.repeat(3);          // !!!
    String MAP_DELETE = HYPHEN.repeat(3);              // ---
    String MAP_ADD = LESS_SIGN.repeat(3);              // <<<
    String MAP_GET = GREATER_SIGN.repeat(3);           // >>>
    String MAP_ISEMPTY = QUESTION.repeat(3);           // ???

    // ORIGIN
    List<String> ORIGIN_LIST = new ArrayList<>() {{
        add(INT_VARIABLE);
        add(LONG_VARIABLE);
        add(BOOL_VARIABLE);
        add(STRING_VARIABLE);
        add(CHARACTER_VARIABLE);
        add(FLOAT_VARIABLE);
        add(DOUBLE_VARIABLE);
    }};

    // SET
    List<String> SET_LIST = new ArrayList<>() {{
        add(SET_INTEGER);
        add(SET_LONG);
        add(SET_BOOLEAN);
        add(SET_STRING);
        add(SET_CHARACTER);
        add(SET_FLOAT);
        add(SET_DOUBLE);
    }};
    // LIST
    List<String> LIST_LIST = new ArrayList<>() {{
        add(LIST_INTEGER);
        add(LIST_LONG);
        add(LIST_BOOLEAN);
        add(LIST_STRING);
        add(LIST_CHARACTER);
        add(LIST_FLOAT);
        add(LIST_DOUBLE);
    }};
    // MAP
    List<String> MAP_LIST = new ArrayList<>() {{
        add(MAP_INTEGER);
        add(MAP_LONG);
        add(MAP_BOOLEAN);
        add(MAP_STRING);
        add(MAP_CHARACTER);
        add(MAP_FLOAT);
        add(MAP_DOUBLE);
    }};

    List<String> TOTAL_LIST = new ArrayList<>() {{
        addAll(ORIGIN_LIST);
        addAll(SET_LIST);
        addAll(LIST_LIST);
        addAll(MAP_LIST);
    }};

    Map<String, Object> DEFAULT = new HashMap<>() {{
        put(INT_VARIABLE, 0);
        put(LONG_VARIABLE, 0);
        put(BOOL_VARIABLE, FALSE);
        put(STRING_VARIABLE, "");
        put(CHARACTER_VARIABLE, ' ');
        put(FLOAT_VARIABLE, 0.0);
        put(DOUBLE_VARIABLE, 0.0);

        put(SET_INTEGER, new LinkedHashSet<Integer>());
        put(SET_LONG, new LinkedHashSet<Long>());
        put(SET_BOOLEAN, new LinkedHashSet<String>());
        put(SET_STRING, new LinkedHashSet<String>());
        put(SET_CHARACTER, new LinkedHashSet<Character>());
        put(SET_FLOAT, new LinkedHashSet<Float>());
        put(SET_DOUBLE, new LinkedHashSet<Double>());

        put(LIST_INTEGER, new LinkedList<Integer>());
        put(LIST_LONG, new LinkedList<Long>());
        put(LIST_BOOLEAN, new LinkedList<String>());
        put(LIST_STRING, new LinkedList<String>());
        put(LIST_CHARACTER, new LinkedList<Character>());
        put(LIST_FLOAT, new LinkedList<Float>());
        put(LIST_DOUBLE, new LinkedList<Double>());

        put(MAP_INTEGER, new LinkedHashMap<String, Integer>());
        put(MAP_LONG, new LinkedHashMap<String, Long>());
        put(MAP_BOOLEAN, new LinkedHashMap<String, String>());
        put(MAP_STRING, new LinkedHashMap<String, String>());
        put(MAP_CHARACTER, new LinkedHashMap<String, Character>());
        put(MAP_FLOAT, new LinkedHashMap<String, Float>());
        put(MAP_DOUBLE, new LinkedHashMap<String, Double>());
    }};
}

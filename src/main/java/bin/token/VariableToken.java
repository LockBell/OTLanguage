package bin.token;

import java.util.ArrayList;
import java.util.List;

public interface VariableToken extends Token {
    String VARIABLE_NAME = "[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z$_-]+[0-9ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z$_-]*";
    String VARIABLE_ACCESS = ACCESS + "{0,2}" + VARIABLE_NAME;
    String VARIABLE = ":" + VARIABLE_ACCESS + "_";
    String VARIABLE_SET = VARIABLE_ACCESS + ":";
    String VARIABLE_PUT = ":";
    String VARIABLE_GET_S = ":";
    String VARIABLE_GET_E = "_";

    String FILE_NAME = "[^~]+";

    // SYSTEM
    String FORCE_QUIT = "ㄲㅌㄲ";
    String SLEEP = "=_=";
    String FILE = "ㅍㅅㅍ";

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

    List<String> ORIGIN_LIST = new ArrayList<>() {{
        add(INT_VARIABLE);
        add(LONG_VARIABLE);
        add(BOOL_VARIABLE);
        add(STRING_VARIABLE);
        add(CHARACTER_VARIABLE);
        add(FLOAT_VARIABLE);
        add(DOUBLE_VARIABLE);
    }};

    List<String> SET_LIST = new ArrayList<>() {{
        add(SET_INTEGER);
        add(SET_LONG);
        add(SET_BOOLEAN);
        add(SET_STRING);
        add(SET_CHARACTER);
        add(SET_FLOAT);
        add(SET_DOUBLE);
    }};

    List<String> LIST_LIST = new ArrayList<>() {{
        add(LIST_INTEGER);
        add(LIST_LONG);
        add(LIST_BOOLEAN);
        add(LIST_STRING);
        add(LIST_CHARACTER);
        add(LIST_FLOAT);
        add(LIST_DOUBLE);
    }};

    List<String> MAP_LIST = new ArrayList<>() {{
        add(MAP_INTEGER);
        add(MAP_LONG);
        add(MAP_BOOLEAN);
        add(MAP_STRING);
        add(MAP_CHARACTER);
        add(MAP_FLOAT);
        add(MAP_DOUBLE);
    }};
}
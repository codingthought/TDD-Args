package parser;

public interface Parser<T> {
    T parse(String given);
//        T parse(String given, T defaultVale);
}

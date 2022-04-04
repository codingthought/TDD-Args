package parser;

public class IntParser implements Parser<Integer> {
    @Override
    public Integer parse(String given) {
        return given == null ? 0 : Integer.parseInt(given);
    }
}

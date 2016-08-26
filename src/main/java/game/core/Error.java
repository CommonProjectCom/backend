package game.core;

public enum  Error {
    NO_CITY("NO CITY"),
    FALSE_MOVE("FALSE MOVE"),
    GAME_NOT_FOUND("GAME NOT FOUND"),
    INVALID_GAME_ID("INVALID GAME ID");

    private String value;

    Error(String value) {
        this.value = value;
    }

    private String getValue() {
        return "ERROR - " + value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }
}

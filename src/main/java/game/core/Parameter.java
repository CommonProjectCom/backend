package game.core;

public class Parameter {

    static final String GAME_ID = "GameID";
    static final String STATE = "State";
    static final String NAME = "Name";
    static final String MOVE = "Move";

    private int id;
    private String state;
    private String move;
    private String name;

    public Parameter(int id, String state, String move, String name) {
        this.id = id;
        this.state = state;
        this.move = move;
        this.name = name;
    }

    @Override
    public String toString() {
        return "GameID:" + id +
                "@" +
                "State:" + state +
                "@" +
                "Move:"  + move +
                "@" +
                "Name:"  + name;
    }

    public Parameter(String input) {
        String[] strings = input.split("@");
        for (String value : strings) {
            setValue(value);
        }
    }

    private void setValue(String input) {
        String[] in = input.split(":");
        String value = in[1];
        switch (in[0]) {
            case GAME_ID:
                setId(value);
                break;

            case NAME:
                setName(value);
                break;

            case MOVE:
                setMove(value);
                break;

            case STATE:
                setState(value);
                break;
        }
    }

    public String getName() {
        return name;
    }

    public int getGameID() {
        return id;
    }

    public String getState() {
        return state;
    }

    public String getMove() {
        return move;
    }

    private void setName(String name) {
        this.name = name;
    }

    private void setId(String id) {
        this.id = Integer.valueOf(id);
    }

    private void setState(String state) {
        this.state = state;
    }

    public void setMove(String move) {
        this.move = move;
    }
}

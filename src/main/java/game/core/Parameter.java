package game.core;

public class Parameter {

    private static final String GAME_ID = "GameID";
    private static final String MOVE = "Move";
    private static final String STATE = "State";
    private static final String SEPARATOR = "@";

    private int gameID = 0;
    private String state = null;
    private String move = null;

    public Parameter(int gameID, String state, String move) {
        this.gameID = gameID;
        this.state = state;
        this.move = move.toUpperCase();
    }

    public Parameter(String input) {
        String[] strings = input.split(SEPARATOR);
        for (String value : strings) {
            setValue(value);
        }
    }

    @Override
    public String toString() {
        String param = "GameID:" + gameID + SEPARATOR + "Move:"  + move;

        if (state != null)
            param += SEPARATOR + "State:" + state;

        return param;
    }

    private void setValue(String input) {
        String[] in = input.split(":");
        String value = in[1];
        switch (in[0]) {
            case GAME_ID:
                setGameID(value);
                break;
            case MOVE:
                setMove(value.toUpperCase());
                break;
            case STATE:
                setState(value);
                break;
        }
    }

    public int getGameID() {
        return gameID;
    }

    public String getState() {
        return state;
    }

    public String getMove() {
        return move;
    }

    public void setGameID(String gameID) {
        this.gameID = Integer.valueOf(gameID);
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setMove(String move) {
        this.move = move;
    }
}

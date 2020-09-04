package matthias.tictactoe.game.events;

import matthias.tictactoe.game.model.Player;
import matthias.tictactoe.game.model.PlayerSymbol;
import matthias.tictactoe.game.model.StateType;
import matthias.tictactoe.game.model.Symbol;

public class GameEventFactory {

    public static GameEvent createPlayerJoinedEvent(Player player) {
        return GameEvent.builder()
                        .eventType(GameEventType.PLAYER_JOINED)
                        .addToPayload("player", player)
                        .build();
    }

    public static GameEvent createPlayerLeftEvent(Player player) {
        return GameEvent.builder()
                .eventType(GameEventType.PLAYER_LEFT)
                .addToPayload("player", player)
                .build();
    }

    public static GameEvent createStateChangedEvent(StateType state) {
        return GameEvent.builder()
                .eventType(GameEventType.GAME_STATE_CHANGED)
                .addToPayload("state", state)
                .build();
    }

    public static GameEvent createBoardChangedEvent(Symbol[][] board) {
        return GameEvent.builder()
                .eventType(GameEventType.BOARD_CHANGED)
                .addToPayload("board", board)
                .build();
    }

    public static GameEvent createActiveSymbolChangedEvent(PlayerSymbol activeSymbol) {
        return GameEvent.builder()
                .eventType(GameEventType.ACTIVE_SYMBOL_CHANGED)
                .addToPayload("activeSymbol", activeSymbol)
                .build();
    }
}
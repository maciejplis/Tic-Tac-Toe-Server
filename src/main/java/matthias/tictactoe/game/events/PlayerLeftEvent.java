package matthias.tictactoe.game.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import matthias.tictactoe.game.model.Symbol;

@Data
@AllArgsConstructor
public class PlayerLeftEvent {

    private final String name = "PLAYER_LEFT";
    private Symbol playerSymbol;
    private String playerName;
}

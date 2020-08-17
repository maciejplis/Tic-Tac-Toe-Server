package matthias.tictactoe.game.services;

import lombok.RequiredArgsConstructor;
import matthias.tictactoe.game.events.*;
import matthias.tictactoe.game.model.GameBoard;
import matthias.tictactoe.game.model.Status;
import matthias.tictactoe.game.model.Symbol;
import matthias.tictactoe.web.authentication.model.User;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GameEventPublisher {

    private final ApplicationEventPublisher publisher;

    public void publishPlayerJoinedEvent(Symbol symbol, User player) {
        String playerName = player.getUsername();
        publisher.publishEvent(new PlayerJoinedEvent(symbol, playerName) );
    }

    public void publishPlayerLeftEvent(Symbol symbol, User player) {
        String playerName = player.getUsername();
        publisher.publishEvent(new PlayerLeftEvent(symbol, playerName) );
    }

    public void publishGameStatusChangedEvent(Status status) {
        publisher.publishEvent(new GameStatusChangedEvent(status));
    }

    public void publishBoardChangedEvent(GameBoard board) {
        publisher.publishEvent(new BoardChangedEvent(board));
    }

    public void publishTurnChangedEvent(Symbol tour) {
        publisher.publishEvent(new TourChangedEvent(tour));
    }
}

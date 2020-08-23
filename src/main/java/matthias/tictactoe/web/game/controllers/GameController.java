package matthias.tictactoe.web.game.controllers;

import lombok.AllArgsConstructor;
import matthias.tictactoe.game.TicTacToeGame;
import matthias.tictactoe.web.authentication.utils.ResponseEntityBuilder;
import matthias.tictactoe.game.model.dto.GameData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.Point;
import java.security.Principal;

@AllArgsConstructor
@RestController
@CrossOrigin
public class GameController {

    private final TicTacToeGame game;

    @PostMapping("/mark")
    public ResponseEntity<?> makeMove(Principal principal, @RequestBody Point point) {

        game.markSquare(principal.getName(), point);

        return ResponseEntityBuilder
                .status(200)
                .addToPayload("success", true)
                .addToPayload("message", String.format("Field x%d y%d has been marked successfully", point.x, point.y))
                .build();
    }

    @GetMapping("/game-data")
    public GameData getGameData() {
        return game.getGameData();
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity handleException(Exception e) {
        return ResponseEntityBuilder
                .status(200)
                .addToPayload("success", false)
                .addToPayload("message", e.getMessage())
                .build();
    }
}

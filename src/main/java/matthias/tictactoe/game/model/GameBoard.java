package matthias.tictactoe.game.model;

import matthias.tictactoe.game.events.GameEvent;
import matthias.tictactoe.game.events.GameEventFactory;
import matthias.tictactoe.game.exceptions.SquareMarkingException;

import java.awt.*;
import java.util.Arrays;
import java.util.function.Consumer;

public class GameBoard {
    private final Consumer<GameEvent> eventCallback;
    private final int BOARD_SIZE = 3;
    private final Symbol[][] board = new Symbol[BOARD_SIZE][BOARD_SIZE];

    public GameBoard(Consumer<GameEvent> eventCallback) {
        this.eventCallback = eventCallback;
        clear();
    }

    public void clear() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = Symbol.EMPTY;
            }
        }
        eventCallback.accept(GameEventFactory.createBoardChangedEvent(board));
    }

    public void mark(Point point, Symbol symbol) {
        if(board[point.x][point.y] != Symbol.EMPTY) {
            throw new SquareMarkingException("This square is already marked");
        }
        if(point.x < 0 || point.x > BOARD_SIZE || point.y < 0 || point.y > BOARD_SIZE) {
            throw new SquareMarkingException(String.format("point (%d, %d) is not on the board", point.x, point.y));
        }

        board[point.x][point.y] = symbol;
        eventCallback.accept(GameEventFactory.createBoardChangedEvent(board));
    }

    public Symbol[][] as2DimArray() {
        return board;
    }

    public Symbol[] as1DimArray() {
        return Arrays.stream(board).flatMap(Arrays::stream).toArray(Symbol[]::new);
    }

    public boolean isFull() {
        for(int i=0; i<BOARD_SIZE; i++) {
            for(int j=0; j<BOARD_SIZE; j++) {
                if(board[i][j] == Symbol.EMPTY) {
                    return false;
                }
            }
        }

        return true;
    }

}

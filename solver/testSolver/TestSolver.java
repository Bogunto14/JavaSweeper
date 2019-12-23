package testSolver;

import org.junit.Test;
import solver.MoveType;
import solver.Solver;
import sweeper.*;

import static org.junit.Assert.assertEquals;

public class TestSolver {
    @Test
    public void logicalMove() {
        Ranges.setSize(new Coord(5, 5));
        /*
            00000
            01110
            01*10
            01110
            00000
         */
        Matrix bombMap = new Matrix(Box.ZERO);
        for (int x = 1; x < 4; x++) {
            for (int y = 1; y < 4; y++) {
                bombMap.set(new Coord(x, y), Box.NUM1);
            }
        }
        bombMap.set(new Coord(2, 2), Box.BOMB);
        Bomb bombs = new Bomb(2, bombMap);
        Game game = new Game(bombs);
        game.testStart();
        game.pressLeftButton(new Coord(0,0));
        Solver solver = new Solver(coord -> assertEquals(new Coord(2, 2), coord));
        solver.update(game);
        assertEquals(MoveType.Logical, solver.step());
    }

    @Test
    public void probabilityMove() {
        Ranges.setSize(new Coord(6, 7));
        Matrix bombMap = new Matrix(Box.ZERO);
        bombMap.set(new Coord(0, 0), Box.NUM2);
        bombMap.set(new Coord(1, 0), Box.BOMB);
        bombMap.set(new Coord(2, 0), Box.NUM3);
        bombMap.set(new Coord(3, 0), Box.BOMB);
        bombMap.set(new Coord(4, 0), Box.NUM1);
        bombMap.set(new Coord(5, 0), Box.ZERO);
        bombMap.set(new Coord(0, 1), Box.NUM2);
        bombMap.set(new Coord(1, 1), Box.BOMB);
        bombMap.set(new Coord(2, 1), Box.NUM3);
        bombMap.set(new Coord(3, 1), Box.NUM1);
        bombMap.set(new Coord(4, 1), Box.NUM1);
        bombMap.set(new Coord(5, 1), Box.ZERO);
        bombMap.set(new Coord(0, 2), Box.NUM1);
        bombMap.set(new Coord(1, 2), Box.NUM1);
        bombMap.set(new Coord(2, 2), Box.NUM2);
        bombMap.set(new Coord(3, 2), Box.NUM1);
        bombMap.set(new Coord(4, 2), Box.NUM1);
        bombMap.set(new Coord(5, 2), Box.ZERO);
        bombMap.set(new Coord(0, 3), Box.ZERO);
        bombMap.set(new Coord(1, 3), Box.ZERO);
        bombMap.set(new Coord(2, 3), Box.NUM1);
        bombMap.set(new Coord(3, 3), Box.BOMB);
        bombMap.set(new Coord(4, 3), Box.NUM1);
        bombMap.set(new Coord(5, 3), Box.ZERO);
        bombMap.set(new Coord(0, 4), Box.NUM1);
        bombMap.set(new Coord(1, 4), Box.NUM1);
        bombMap.set(new Coord(2, 4), Box.NUM2);
        bombMap.set(new Coord(3, 4), Box.NUM1);
        bombMap.set(new Coord(4, 4), Box.NUM1);
        bombMap.set(new Coord(5, 4), Box.ZERO);
        bombMap.set(new Coord(0, 5), Box.NUM1);
        bombMap.set(new Coord(1, 5), Box.BOMB);
        bombMap.set(new Coord(2, 5), Box.NUM1);
        bombMap.set(new Coord(3, 5), Box.ZERO);
        bombMap.set(new Coord(4, 5), Box.ZERO);
        bombMap.set(new Coord(5, 5), Box.ZERO);
        bombMap.set(new Coord(0, 6), Box.NUM1);
        bombMap.set(new Coord(1, 6), Box.NUM1);
        bombMap.set(new Coord(2, 6), Box.NUM1);
        bombMap.set(new Coord(3, 6), Box.ZERO);
        bombMap.set(new Coord(4, 6), Box.ZERO);
        bombMap.set(new Coord(5, 6), Box.ZERO);
        /*     x 0123456
             y    -------
             0   2*3*10
             1   2*3110
             2   112110
             3   001*10
             4   112110
             5   1*1000
             6   111000
         */
        Bomb bombs = new Bomb(2, bombMap);
        Game game = new Game(bombs);
        game.testStart();
        game.pressLeftButton(new Coord(0,1));
        game.pressLeftButton(new Coord(2,1));
        Solver solver = new Solver(coord -> assertEquals(new Coord(4, 0), coord));
        solver.update(game);
        assertEquals(MoveType.Probability, solver.step());
    }
}

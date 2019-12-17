package solver;
import sweeper.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Solver {
    private Game state;
    private Bomb bombs;
    private Flag flags;
    private CoordAction action;

    public Solver(CoordAction action){
        this.action = action;
    }

    public interface CoordAction {
        void performCoordAction(Coord coord);
    }

    public void update(Game newstate){
        assert newstate != null;
        state = newstate;
        bombs = newstate.getBomb();
        flags = newstate.getFlag();
    }

    private List<MinesGroup> genGroups() {
        List<MinesGroup> groups = new ArrayList<>();
        for (Coord coord : Ranges.getAllCoords()) {
            if (flags.get(coord) == Box.OPENED) {
                int number = bombs.bombsN(coord) - countCellsAround(coord);
                if (number == 0) continue;
                MinesGroup group = new MinesGroup(number);
                Set<Coord> coords = group.group;
                for (Coord around : Ranges.getCoordsAround(coord)) {
                    if (flags.get(around) == Box.CLOSED) {
                        coords.add(around);
                    }
                }
                groups.add(group);
            }
        }
        boolean changes;
        do {
            changes = false;
            for (int i = 0; i < groups.size(); i++) {                // Проходим по списку групп
                MinesGroup groupI = groups.get(i);
                for (int j = i + 1; j < groups.size(); j++) {        // Сравниваем ее с остальными группами
                    MinesGroup groupJ = groups.get(j);
                    if (groupI.equals(groupJ)) {                     // Если группы одинаковые, то вторую удаляем
                        groups.remove(j--);
                        continue;
                    }
                    MinesGroup parent;                               // Большая группа
                    MinesGroup child;                                // Меньшая группа
                    if (groupI.group.size() > groupJ.group.size()) {
                        parent = groupI;
                        child = groupJ;
                    } else {
                        parent = groupJ;
                        child = groupI;
                    }
                    if (parent.contains(child)) {                    // Если большая группа содержит меньшую, то вычитаем из большей меньшую
                        parent.minusAssign(child);
                        changes = true;                              // Фиксируем факт изменения групп
                    }
                    // Пересекающиеся группы будут устранены автоматически
                }
            }
        } while (changes);  // Повторяем до тех пор, пока не будет производиться никаких изменений
        return groups;
    }

    private boolean logicalMove(List<MinesGroup> groups) {
        for (MinesGroup group : groups) {
            if (group.minesAround == 0) {                            // Количество мин равно нулю
                openBox(group.group.iterator().next());
                return true;
            } else if (group.minesAround == group.group.size()) {    // Количество мин равно количеству ячеек в группе
                flagBox(group.group.iterator().next());
                return true;
            }
        }
        return false;
    }

    private boolean probabilityMove(List<MinesGroup> groups) {
        Coord size = Ranges.getSize();
        double[][] grid = new double[size.getX()][size.getY()];
        int countBombs = 0;
        for (MinesGroup group : groups) {                         // Нахождение вероятностей для каждой клетки с возможной миной
            for (Coord coord : group) {
                double prob = ((double) group.minesAround) / group.group.size();
                double old = grid[coord.getX()][coord.getY()];
                grid[coord.getX()][coord.getY()] = 1.0 - (1.0 - old) * (1.0 - prob);
            }
            countBombs += group.minesAround;
        }
        // Все остальные клетки также имеют вероятность содержания бомбы равную остаточному количеству бомб
        // поделённому на количество закрытых ячеек, не принадлежащих ни одной группе.
        int countClosedReminder = 0;
        for (Coord coord : Ranges.getAllCoords()) {
            // сначала сосчитаем такие ячейки
            if (flags.get(coord) == Box.CLOSED && grid[coord.getX()][coord.getY()] == 0.0)
                countClosedReminder++;
        }
        final int countBombsReminder = bombs.getTotalBombs() - countBombs;
        for (Coord coord : Ranges.getAllCoords()) {
            // теперь определим их вероятнности
            if (flags.get(coord) == Box.CLOSED && grid[coord.getX()][coord.getY()] == 0.0)
                grid[coord.getX()][coord.getY()] = ((double) countBombsReminder) / countClosedReminder;
        }
        // Выбор лучшего варианта
        double bestProb = 2.0;
        Coord bestCoord = null;
        for (Coord coord : Ranges.getAllCoords()) {
            double prob = grid[coord.getX()][coord.getY()];
            if (flags.get(coord) == Box.CLOSED && prob < bestProb) {
                bestProb = prob;
                bestCoord = coord;
            }
        }
        if (bestCoord == null)
            return false;
        openBox(bestCoord);
        return true;
    }

    public MoveType step() {
        if (state.getState() != GameState.PLAYED)
            return MoveType.NoStep;
        Coord size = Ranges.getSize();
        if (flags.getCountOfClosedBoxes() == size.getX() * size.getY()){
            openBox(Ranges.getRandomCoord());
            return MoveType.First;                          // Первый ход
        }
        List<MinesGroup> groups = genGroups();
        if (logicalMove(groups)) {
            return MoveType.Logical;                        // Логический ход
        } else if (probabilityMove(groups))
            return MoveType.Probability;                    // Вероятностный ход
        randomMove();
        return MoveType.Random;                             // Случайный ход
    }

    private void openBox(Coord coord){
        action.performCoordAction(coord);
        state.pressLeftButton(coord);
    }

    private void flagBox(Coord coord){
        action.performCoordAction(coord);
        state.pressRightButton(coord);
        for (Coord place : Ranges.getCoordsAround(coord)){
            if (flags.get(place) != Box.CLOSED && state.getState() == GameState.PLAYED)
                state.pressLeftButton(place);
        }
    }

    private int countCellsAround(Coord coord){
        int n = 0;
        for (Coord place : Ranges.getCoordsAround(coord)){
            if (flags.get(place) == Box.FLAGED)
                n++;
        }
        return n;
    }

    private void randomMove(){
        Optional<Coord> move = Ranges.getAllCoords()
                .stream()
                .filter(coord -> flags.get(coord) == Box.CLOSED)
                .findFirst();
        if (move.isPresent())
            openBox(move.get());
        else
            throw new IllegalStateException("There must be at least 1 closed cell");
    }
}

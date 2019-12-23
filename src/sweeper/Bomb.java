package sweeper;

public class Bomb {
    private Matrix bombMap;
    private int totalBombs;

    Bomb (int totalBombs){
        this.totalBombs = totalBombs;
        bombMap = new Matrix(Box.ZERO);
    }

    public Bomb(int totalBombs, Matrix bombs) {
        this.totalBombs = totalBombs;
        bombMap = bombs;
    }

    void start (){
        bombMap = new Matrix(Box.ZERO);
        for (int j = 0; j < totalBombs; j ++)
            placeBomb();
    }

    Box get (Coord coord){
        return bombMap.get(coord);
    }

    public int bombsN(Coord coord){
        return get(coord).getNumber();
    }

    private void placeBomb(){
        while (true) {
            Coord coord = Ranges.getRandomCoord();
            if (Box.BOMB == bombMap.get(coord))
                continue;
            placeBombAt(coord);
            break;
        }
    }

    private void placeBombAt(Coord coord) {
        bombMap.set(coord, Box.BOMB);
        incNumbersAroundBomb(coord);
    }

    private void incNumbersAroundBomb (Coord coord){
        for (Coord around : Ranges.getCoordsAround(coord))
            if (Box.BOMB != bombMap.get(around))
                bombMap.set(around, bombMap.get(around).getNextNumberBox());
    }

    public int getTotalBombs() {              //Получить общее количество бомб
        return totalBombs;
    }
}

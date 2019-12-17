package sweeper;

public class Coord {
   int x, y;

   public Coord (int x, int y) {
       this.x = x;
       this.y = y;
   }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public int hashCode() {
        return ~x ^ y;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (o instanceof Coord){
            Coord to = (Coord)o;
            return to.x == x && to.y == y;
        }
        return super.equals(o);
    }

    @Override
    public String toString() {
        return "(" + x + "|" + y + ")";
    }
}

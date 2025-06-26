public class Day8Point {

    private final int x;
    private final int y;

    public Day8Point(int x, int y) {
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
    public boolean equals(Object object) {
        if (object instanceof Day8Point point) {
            return this.x == point.getX() && this.y == point.getY();
        }
        return false;
    }

}

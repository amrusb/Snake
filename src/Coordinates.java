public class Coordinates {
    private int X = 0;
    private int Y = 0;

    Coordinates(int x, int y){
        X = x;
        Y = y;
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    public void setX(int x) {
        X = x;
    }

    public void setY(int y) {
        Y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(obj.getClass() != this.getClass()) return false;
        Coordinates temp = (Coordinates)obj;

        return X == temp.getX() && Y == temp.getY();
    }

    @Override
    public String toString() {
        return "(" + X + ", " + Y + ")";
    }
}

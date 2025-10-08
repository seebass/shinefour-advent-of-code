public class EmptyPosition extends Position {

    private Box box;

    private WideBox wideBox;

    public EmptyPosition(int x, int y, boolean withBox) {
        this.x = x;
        this.y = y;

        if (withBox) {
            this.box = new Box(this);
            setBox(box);
        }
    }

    public Box getBox() {
        return box;
    }

    public void setBox(Box box) {
        this.box = box;
    }

    public boolean hasBox() {
        return box != null;
    }

    public WideBox getWideBox() {
        return wideBox;
    }

    public void setWideBox(WideBox wideBox) {
        this.wideBox = wideBox;
    }

    public boolean hasWideBox() {
        return wideBox != null;
    }

}

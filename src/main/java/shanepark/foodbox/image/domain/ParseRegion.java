package shanepark.foodbox.image.domain;

import lombok.Getter;

@Getter
public class ParseRegion {
    private int x;
    private int y;
    private int width;
    private int height;

    public ParseRegion(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void addX(int i) {
        x += i;
    }
}

package shanepark.foodbox.image.domain;

import lombok.Getter;

public record ParseRegion(int x, int y, int width, int height) {

    public ParseRegion addX(int i) {
        return new ParseRegion(this.x + i, y, width, height);
    }
}

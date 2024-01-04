package dev.huskuraft.effortless.api.renderer;

import javax.annotation.Nullable;

public record ScreenRect(
        int x,
        int y,
        int width,
        int height
) {

    private static final ScreenRect EMPTY = new ScreenRect(0, 0, 0, 0);

    public static ScreenRect empty() {
        return EMPTY;
    }

    public int top() {
        return this.y;
    }

    public int bottom() {
        return this.y + this.height;
    }

    public int left() {
        return this.x;
    }

    public int right() {
        return this.x + this.width;
    }

    @Nullable
    public ScreenRect intersection(ScreenRect rect) {
        var left = Math.max(this.left(), rect.left());
        var top = Math.max(this.top(), rect.top());
        var right = Math.min(this.right(), rect.right());
        var bottom = Math.min(this.bottom(), rect.bottom());
        return left < right && top < bottom ? new ScreenRect(left, top, right - left, bottom - top) : null;
    }

}

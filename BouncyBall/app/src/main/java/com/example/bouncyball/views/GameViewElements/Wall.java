package com.example.bouncyball.views.GameViewElements;

public class Wall {
    private int x;
    private int y;
    private int width;
    private int height;

    public Wall(int X, int Y, int Width, int Height)
    {
        x=X;
        y=Y;
        width=Width;
        height=Height;
    }

    public Point getPosition()
    {
        return new Point(x,y);
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

}

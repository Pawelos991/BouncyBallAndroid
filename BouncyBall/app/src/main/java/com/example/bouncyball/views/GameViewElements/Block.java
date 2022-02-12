package com.example.bouncyball.views.GameViewElements;

import android.graphics.Color;

public class Block {

    private int x;
    private int y;
    private int width; //Always width>height
    private int height;
    private Wall wall;
    private int hitsLeft;

    public Block(int X, int Y, int Width, int Height, int HitsLeft)
    {
        x=X;
        y=Y;
        width=Width;
        height=Height;
        wall = new Wall(x,y,width,height);
        hitsLeft=HitsLeft;
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

    public int getColor()
    {
        if(hitsLeft>600)
            return Color.GRAY;
        else if(hitsLeft>500)
            return Color.MAGENTA;
        else if(hitsLeft>400)
            return Color.BLUE;
        else if(hitsLeft>300)
            return Color.CYAN;
        else if(hitsLeft>200)
            return Color.GREEN;
        else if(hitsLeft>100)
            return Color.YELLOW;
        else
            return Color.RED;
    }

    public Wall getWall()
    {
        return wall;
    }
    public int getHitsLeft()
    {
        return hitsLeft;
    }
    public void getHit()
    {
        hitsLeft--;
    }

}

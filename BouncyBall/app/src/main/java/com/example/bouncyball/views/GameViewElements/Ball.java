package com.example.bouncyball.views.GameViewElements;

import static java.lang.Math.abs;

public class Ball {
    private float x;
    private float y;
    private float xSpeed;
    private float ySpeed;
    private int d; //diameter;
    private boolean isShoot;
    private Point position;

    public Ball(int X, int Y, float XSpeed, float YSpeed, int D)
    {
        x=(float)X;
        y=(float)Y;
        xSpeed=XSpeed;
        ySpeed=YSpeed;
        d=D;
        isShoot=false;
        position = new Point((int)x,(int)y);
    }

    public Point getPosition()
    {
        return position;
    }

    public void setPosition(int X, int Y)
    {
        x=X;
        y=Y;
        position.x = X;
        position.y = Y;
    }

    public void updatePosition()
    {
        if(isShoot)
        {
            x=x+xSpeed;
            y=y+ySpeed;
            position.x = (int)x;
            position.y = (int)y;
        }

    }

    public void changeXSpeed()
    {
        xSpeed=xSpeed*(-1);
    }

    public void changeYSpeed()
    {
        ySpeed=ySpeed*(-1);
    }

    public int getD()
    {
        return d;
    }

    public void setXSpeed(float newSpeed)
    {
        xSpeed=newSpeed;
    }

    public void setYSpeed(float newSpeed)
    {
        ySpeed=newSpeed;
    }

    public void setShoot()
    {
        isShoot=true;
    }

    public boolean isShoot()
    {
        return isShoot;
    }

    private boolean checkCollisionWithWallFromLeft(Wall wall)
    {
        if( //Collision from left
                (y+d>wall.getPosition().y && y<wall.getPosition().y+wall.getHeight())
                        && (x+d>=wall.getPosition().x && x+d < wall.getPosition().x+abs(xSpeed))
        )
        {
            changeXSpeed();
            return true;
        }
        return false;
    }

    private boolean checkCollisionWithWallFromRight(Wall wall)
    {
        if( //Collision from right
                (y+d>wall.getPosition().y && y<wall.getPosition().y+wall.getHeight())
                        && (x<=wall.getPosition().x+wall.getWidth()
                        && x > wall.getPosition().x+wall.getWidth()-abs(xSpeed))
        )
        {
            changeXSpeed();
            return true;
        }
        return false;
    }

    private boolean checkCollisionWithWallFromBottom(Wall wall)
    {
        if( //Collision from bottom
                (x+d>=wall.getPosition().x && x<= wall.getPosition().x+wall.getWidth())
                        && (y<=wall.getPosition().y+wall.getHeight()
                        && y>wall.getPosition().y+wall.getHeight()-abs(ySpeed))
        )
        {
            changeYSpeed();
            return true;
        }
        return false;
    }

    private boolean checkCollisionWithWallFromTop(Wall wall)
    {
        if( //Collision from top
                (x+d>=wall.getPosition().x && x<= wall.getPosition().x+wall.getWidth())
                        && (y+d>=wall.getPosition().y && y+d<wall.getPosition().y+abs(ySpeed))
        )
        {
            changeYSpeed();
            return true;
        }
        return false;
    }

    public boolean checkCollisionWithWall(Wall wall)
    {
        boolean ret = false;
        if(checkCollisionWithWallFromLeft(wall))
            ret = true;
        else if(checkCollisionWithWallFromRight(wall))
            ret = true;
        if(checkCollisionWithWallFromBottom(wall))
            ret = true;
        else if(checkCollisionWithWallFromTop(wall))
            ret = true;
        return ret;
    }

}

package com.example.bouncyball.views;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

import android.app.Notification;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.LimitExceededException;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.bouncyball.views.GameViewElements.Ball;
import com.example.bouncyball.views.GameViewElements.Block;
import com.example.bouncyball.views.GameViewElements.Wall;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameView extends View {

    private int width;
    private int height;
    private float xScale;
    private float yScale;
    private List<Ball> balls;
    private List<Block> blocks;
    private List<Wall> borders;
    private boolean newShoot;
    private boolean shootClicked;
    private Ball aim;
    private boolean isAiming;
    private int ballD = 40;
    private float sumSpeed=20;
    private int framesPerBall;
    private int actualFrame;
    private int points;
    private Paint paint;
    private Rect borderRect;

    private int blockWidth;
    private int blockHeight;
    private int gapWidth;
    private int gapHeight;
    private Rect blockRect;

    private int fontSize;



    public GameView(Context context) {
        super(context);

        init(null);
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(attrs);
    }

    private void init(@Nullable AttributeSet set)
    {
        points = 0;
        newShoot=true;
        shootClicked=false;
        actualFrame=0;

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(30);
        paint.setTextAlign(Paint.Align.CENTER);

        borderRect = new Rect();
        blockRect = new Rect();

        width = 1080;
        height = 2033;

        xScale = (float)width/(float)1080;
        yScale = (float)height/(float)2033;

        sumSpeed = xScale * sumSpeed;
        ballD = (int)((float)ballD*xScale);
        framesPerBall = (int)(ballD*sqrt(2)/sumSpeed)+1;

        blockWidth = (int)((float)200 * xScale);
        gapWidth = (int)((float)15 * xScale);
        blockHeight = (int)((float)50 * yScale);
        gapHeight = (int)((float)10 * yScale);

        fontSize = (int)((float)30*xScale);

        aim = new Ball(0,0,0,0,(int)((float)60*xScale));
        isAiming=false;

        balls = new ArrayList<Ball>();
        initializeBalls();

        blocks = new ArrayList<Block>();
        initializeBlocks();

        borders = new ArrayList<Wall>();
        initializeBorders();

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(newShoot)
                     invalidate();
                else
                {
                    updateBalls();
                    invalidate();
                }

            }
        },17l,17l);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        canvas.drawColor(Color.BLACK);
        paintBorders(canvas);
        paintBlocks(canvas);
        paintBalls(canvas);
        paintAim(canvas);
    }

    private void initializeBalls()
    {
        for(int i=0;i<3;i++)
            balls.add(new Ball((width/2)-(ballD/2),height - ballD - 50,5,-5,ballD));
        for(int i = 0; i< points; i++)
            balls.add(new Ball((width/2)-(ballD/2),height - ballD - 50,5,-5,ballD));
    }

    private void initializeBlocks()
    {
        for(int i=0;i<5;i++)
        {
            for(int j=0;j<10;j++)
            {
                blocks.add(new Block(10+(blockWidth+gapWidth)*i, 10+(blockHeight+gapHeight)*j, blockWidth, blockHeight, 10));
            }
        }
    }

    private void initializeBorders()
    {
        borders.add(new Wall(0,height-10,width,10+(int)sumSpeed*2));
        borders.add(new Wall(0,-(int)sumSpeed*2,width,10+(int)sumSpeed*2));
        borders.add(new Wall((int)-sumSpeed*2,0,10+(int)sumSpeed*2,height));
        borders.add(new Wall(width-10,0,10+(int)sumSpeed*2,height));
    }

    private void paintBalls(Canvas canvas)
    {
        paint.setColor(Color.WHITE);
        try{
            for(Ball ball : balls)
            {
                canvas.drawCircle(ball.getPosition().x+(ball.getD()/2), ball.getPosition().y+(ball.getD()/2), ball.getD()/2, paint);
            }
        }
        catch(Exception e)
        {

        }
    }

    private void paintBlocks(Canvas canvas)
    {
        try {
            for(Block block : blocks)
            {
                    paint.setColor(Color.MAGENTA);
                    blockRect.left = block.getPosition().x;
                    blockRect.top = block.getPosition().y;
                    blockRect.right = block.getPosition().x+blockWidth;
                    blockRect.bottom = block.getPosition().y+blockHeight;
                    canvas.drawRect(blockRect,paint);

                    paint.setColor(Color.WHITE);
                    canvas.drawText(
                            String.valueOf(block.getHitsLeft()),
                            block.getPosition().x+(blockWidth/2),
                            block.getPosition().y+(blockHeight/2)+(fontSize/3),
                            paint
                    );
            }
        }
        catch (Exception e)
        {
        }

    }

    private void paintBorders(Canvas canvas)
    {
        paint.setColor(Color.GRAY);
        for(Wall wall : borders)
        {
            borderRect.left = wall.getPosition().x;
            borderRect.top = wall.getPosition().y;
            borderRect.right = wall.getPosition().x+wall.getWidth();
            borderRect.bottom = wall.getPosition().y+wall.getHeight();
            canvas.drawRect(borderRect,paint);
        }
    }

    private void paintAim(Canvas canvas)
    {
        if(newShoot && isAiming)
        {
            //Line
            paint.setColor(Color.GRAY);

            //Ball
            paint.setColor(Color.RED);
            canvas.drawCircle(aim.getPosition().x+(aim.getD()/2), aim.getPosition().y+(aim.getD()/2),aim.getD()/2,paint);
        }
    }

    private void updateBalls()
    {
        if(actualFrame==0)
        {
            actualFrame=framesPerBall;
            for(Ball ball : balls)
            {
                if(!ball.isShoot())
                {
                    ball.setShoot();
                    break;
                }
            }
        }
        actualFrame--;

        for(Ball ball : balls)
        {
            ball.updatePosition();
        }

        boolean ballRemoved=false;
        do{
            ballRemoved=false;
            Ball ballToRemove = null;
            for(Ball ball : balls)
            {
                if(ball.checkCollisionWithWall(borders.get(0)))
                    ballToRemove=ball;
            }
            if(ballToRemove!=null)
            {
                balls.remove(ballToRemove);
                ballRemoved=true;
            }
        }while(ballRemoved);

        if(balls.isEmpty())
        {
            newShoot=true;
            initializeBalls();
        }
        else
        {
            for(Ball ball : balls)
            {
                boolean collidedWithWall=false;
                for(Wall wall : borders)
                {
                    if(ball.checkCollisionWithWall(wall))
                        collidedWithWall=true;
                }

                if(!collidedWithWall)
                {
                    Block blockToRemove = null;
                    for(Block block : blocks)
                    {
                        if(ball.checkCollisionWithWall(block.getWall()))
                        {
                            block.getHit();
                            if(block.getHitsLeft()==0)
                            {
                                blockToRemove=block;
                                points++;
                                System.out.println("Jest tyle blokow " + blocks.size());
                            }
                            //break;
                        }
                    }
                    blocks.remove(blockToRemove);
                }
            }
        }
    }

    private void shoot()
    {
        newShoot=false;
        shootClicked=false;
        //numberOfShoots++;
        //System.out.println(Main.numberOfShoots);
        actualFrame=0;

        int aimX = aim.getPosition().x+(aim.getD()/2);
        int aimY = aim.getPosition().y+(aim.getD()/2);

        for(Ball ball : balls)
        {
            int ballX = ball.getPosition().x+(ball.getD()/2);
            int ballY = ball.getPosition().y+(ball.getD()/2);
            int xDist = aimX-ballX;
            int yDist = aimY-ballY;
            int sum = abs(xDist)+abs(yDist);
            float xToSum = (float)xDist/(float)sum;
            float yToSum = (float)yDist/(float)sum;
            float xSpeed;
            float ySpeed;
            if(xToSum>yToSum)
            {
                xSpeed = sumSpeed*xToSum;
                if(yDist>0)
                    ySpeed = sumSpeed-abs(xSpeed);
                else
                    ySpeed = -(sumSpeed-abs(xSpeed));
            }
            else
            {
                ySpeed = (int)(sumSpeed*yToSum);
                if(xDist>0)
                    xSpeed = sumSpeed-abs(ySpeed);
                else
                    xSpeed = -(sumSpeed-abs(ySpeed));
            }

            ball.setXSpeed(xSpeed);
            ball.setYSpeed(ySpeed);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        boolean value = super.onTouchEvent(event);
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:{
                int x = (int)event.getX();
                int y = (int)event.getY();
                aim.setPosition(x,y);
                if(!isAiming)
                    isAiming=true;
                return true;
            }
            case MotionEvent.ACTION_MOVE:{
                int x = (int)event.getX();
                int y = (int)event.getY();
                aim.setPosition(x,y);
                 return true;
            }
            case MotionEvent.ACTION_UP:
            {
                if(newShoot && !shootClicked)
                {
                    isAiming=false;
                    shoot();
                    return true;
                }
                return value;
            }
        }
        return value;
    }

}

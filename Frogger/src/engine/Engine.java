package engine;

import java.util.Random;

public class Engine
{

    private int height;
    private int width;

    private int level;
    private int lives;
    private int steps;

    private Field fields[][];

    private int frogX;
    private int frogY;

    private int goalX;
    private int goalY;

    private final int maxLevel = 4;

    private boolean spawnFlag = false;

    public Engine(int height, int width)
    {
        this.height = height;
        this.width = width;
        this.lives = 3;
        this.level = 1;
        this.steps = 0;

        this.initMap();
    }

    public void initMap()
    {
        int roadWaterRows = (this.height / 2) - 2;
        int waterI = 1+1;
        int roadI = waterI + 1 + roadWaterRows;

        fields = new Field[this.height][this.width];

        // init Grass
        for(int i = 0; i < this.width; i++)
        {
            this.fields[this.height - 1][i] = new Field(Fields.Grass);
            this.fields[this.height - 1][i].setCurrentState(Fields.Grass);

            this.fields[0][i] = new Field(Fields.Grass);
            this.fields[0][i].setCurrentState(Fields.Grass);

            this.fields[1][i] = new Field(Fields.Grass);
            this.fields[1][i].setCurrentState(Fields.Grass);

            this.fields[roadI - 1][i] = new Field(Fields.Grass);
            this.fields[roadI - 1][i].setCurrentState(Fields.Grass);
        }

        // init Frog
        this.initFrog();

        // init Water
        for(int i = 0; i < roadWaterRows; i++)
        {
            for(int j = 0; j < this.width; j++)
            {
                this.fields[i + waterI][j] = new Field(Fields.Water);
                this.fields[i + waterI][j].setCurrentState(Fields.Water);
            }
        }

        // init Road
        for(int i = 0; i < roadWaterRows; i++)
        {
            for(int j = 0; j < this.width; j++)
            {
                this.fields[i + roadI][j] = new Field(Fields.Road);
                this.fields[i + roadI][j].setCurrentState(Fields.Road);
            }
        }

        // init goal
        goalX = 1;
        Random r = new Random();
        goalY = r.nextInt(this.width - 1);
        this.fields[goalX][goalY] = new Field(Fields.Goal);
        this.fields[goalX][goalY].setCurrentState(Fields.Goal);
    }


    public void initFrog()
    {
        Random r = new Random();
        frogX = this.height - 1;
        frogY = r.nextInt(this.width - 1);

        this.fields[frogX][frogY].setCurrentState(Fields.Frog);
        this.fields[frogX][frogY].setFrogPosition(true);
    }

    public Fields getFieldValue(int i, int j)
    {
        return this.fields[i][j].getCurrentState();
    }

    public void move(Directions direction)
    {
        switch (direction)
        {
            case Up:
                if(this.frogX - 1 >= 0)
                {
                    this.fields[this.frogX][this.frogY].setCurrentState(this.fields[this.frogX][this.frogY].getFieldValue());
                    this.fields[this.frogX][this.frogY].setFrogPosition(false);

                    this.fields[this.frogX - 1][this.frogY].setCurrentState(Fields.Frog);
                    this.fields[this.frogX - 1][this.frogY].setFrogPosition(true);
                    this.frogX -= 1;
                }
                break;
            case Left:
                if(this.frogY - 1 >= 0)
                {
                    this.fields[this.frogX][this.frogY].setCurrentState(this.fields[this.frogX][this.frogY].getFieldValue());
                    this.fields[this.frogX][this.frogY].setFrogPosition(false);

                    this.fields[this.frogX][this.frogY - 1].setCurrentState(Fields.Frog);
                    this.fields[this.frogX][this.frogY - 1].setFrogPosition(true);
                    this.frogY -= 1;
                }
                break;
            case Down:
                if(this.frogX + 1 <= this.height - 1)
                {
                    this.fields[this.frogX][this.frogY].setCurrentState(this.fields[this.frogX][this.frogY].getFieldValue());
                    this.fields[this.frogX][this.frogY].setFrogPosition(false);

                    this.fields[this.frogX + 1][this.frogY].setCurrentState(Fields.Frog);
                    this.fields[this.frogX + 1][this.frogY].setFrogPosition(true);

                    this.frogX += 1;
                }
                break;
            case Right:
                if(this.frogY + 1 <= this.width - 1)
                {
                    this.fields[this.frogX][this.frogY].setCurrentState(this.fields[this.frogX][this.frogY].getFieldValue());
                    this.fields[this.frogX][this.frogY].setFrogPosition(false);

                    this.fields[this.frogX][this.frogY + 1].setCurrentState(Fields.Frog);
                    this.fields[this.frogX][this.frogY + 1].setFrogPosition(true);

                    this.frogY += 1;
                }
                break;
        }

        this.steps++;
    }

    public boolean isOnGoal()
    {
        if(this.fields[this.goalX][this.goalY].getCurrentState() == Fields.Frog)
        {
            this.fields[this.goalX][this.goalY].setCurrentState(this.fields[this.goalX][this.goalY].getFieldValue());
            this.level++;
            return true;
        }
        return false;
    }

    public boolean isCollided()
    {
        if(this.fields[frogX][frogY].getCurrentState() != Fields.Frog)
        {
            this.lives--;
            return true;
        }

        return false;
    }

    public boolean isDead()
    {
        if(this.lives == 0) return true;

        return false;
    }

    public boolean isEnd()
    {
        if(this.isDead() == true) return true;
        if(this.level == this.maxLevel) return true;

        return false;
    }

    public void spawnObject()
    {
        Random r = new Random();
        int spawnIR = r.nextInt(this.height -1);
        int spawnIW = r.nextInt(this.height -1);
        if(this.fields[spawnIW][0].getCurrentState() == Fields.Water)
        {
            this.fields[spawnIW][0].setCurrentState(Fields.Wood);
        }

        if(this.fields[spawnIR][0].getCurrentState() == Fields.Road)
        {
            Random r2 = new Random();
            int vehicle = r2.nextInt(2);
            if(vehicle == 0) this.fields[spawnIR][0].setCurrentState(Fields.Car);
            else if(vehicle == 1) this.fields[spawnIR][0].setCurrentState(Fields.Bus);
        }
    }

    public void moveObjects()
    {
        for(int i = 0; i < this.height - 1; i++)
        {
            for(int j = this.width - 1; j >= 0; j--)
            {
                if(this.fields[i][j].getCurrentState() == Fields.Wood || this.fields[i][j].getCurrentState() == Fields.Car || this.fields[i][j].getCurrentState() == Fields.Bus)
                {
                    if(j == this.width - 1)
                    {
                        this.fields[i][j].setCurrentState(this.fields[i][j].getFieldValue());
                    }
                    else
                    {
                        this.fields[i][j + 1].setCurrentState(this.fields[i][j].getCurrentState());
                        this.fields[i][j].setCurrentState(this.fields[i][j].getFieldValue());
                    }
                }
            }
        }

    }

    public int getLevel()
    {
        return level;
    }

    public int getLives()
    {
        return lives;
    }

    public int getSteps()
    {
        return steps;
    }
}

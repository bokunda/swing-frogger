package engine;

public class Field
{
    Fields currentState;
    Fields fieldValue;

    boolean moveable = false;
    boolean frogOnIt = false;

    public Field(Fields fieldValue)
    {
        this.fieldValue = fieldValue;
    }

    public Fields getCurrentState()
    {
        return currentState;
    }

    public void setCurrentState(Fields currentState)
    {
        this.currentState = currentState;

        if(currentState == Fields.Wood || currentState == Fields.Car || currentState == Fields.Bus)
        {
            this.moveable = true;
        }
    }

    public Fields getFieldValue()
    {
        return fieldValue;
    }

    public void setFrogPosition(boolean value)
    {
        this.frogOnIt = value;
    }
}

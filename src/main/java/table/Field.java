package table;

class Field{
    protected int x,y,pawn;

    public Field(int x, int y, int pawn){
        this.x = x;
        this.y = y;
        this.pawn = pawn;
    }

    public Field(){
        this.x = 0;
        this.y = 0;
        this.pawn = 5;
    }

    public void setPawn(int pawn){
        this.pawn = pawn;
    }

    public int getPawn(){
        return this.pawn;
    }

    public int getX(boolean whitePlayer){
        if (whitePlayer) return this.x;
        else return 9-this.x;
    }

    public int getY(boolean whitePlayer){
        if (whitePlayer) return this.y;
        else return 9-this.y;
    }
}

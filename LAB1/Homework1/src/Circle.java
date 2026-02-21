//autorul acestei clase este: Carabus Andrei-Sebastian(2A3)

public class Circle {
    private int radius;
    private int dimDraw;
    private char[][] draw;

    public Circle(int radius){

        this.radius=radius;
        int n = radius;
        this.dimDraw=2*n+3;
        this.draw = new char[this.dimDraw][this.dimDraw];
        //char whiteSpace = '\u2003';
        char whiteSpace = ' ';
        char blackSpace = '\u2588';

        int start = -n;
        int offset=n+1;
        for(int i=start-1;i<n+2;i++){
            for(int j=start-1;j<n+2;j++){

                int row=i+offset;
                int col=j+offset;

                if((int)Math.pow(i*2,2) + (int)Math.pow(j,2) <= (int)Math.pow(radius,2)){
                    this.draw[row][col]=whiteSpace;
                } else {
                    this.draw[row][col]=blackSpace;
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        int n=dimDraw;
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                sb.append(this.draw[i][j]);
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}

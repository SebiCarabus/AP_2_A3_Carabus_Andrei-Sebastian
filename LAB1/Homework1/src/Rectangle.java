//autorul acestei clase este: Carabus Andrei-Sebastian(2A3)

public class Rectangle {
    private int length;
    private int width;
    private char[][] draw;

    public Rectangle(int length,int width){
        this.length=length+2;
        this.width=width+2;
        this.draw=new char[this.length][this.width];
        //char whiteSpace = '\u2003';
        char whiteSpace = ' ';
        char blackSpace = '\u2588';
        int n=this.length;
        int m=this.width;
        for(int i=0;i<n;i++){
            for(int j=0;j<m;j++){
                if(i==0||i==n-1||j==0||j==m-1){
                    draw[i][j]=whiteSpace;
                } else {
                    draw[i][j]=blackSpace;
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        int n=this.length;
        int m=this.width;
        for(int i=0;i<n;i++){
            for(int j=0;j<m;j++){
                sb.append(this.draw[i][j]);
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}

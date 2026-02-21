import java.util.Scanner;//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
void main() {
    Scanner scan=new Scanner(System.in);
    int n=0;
    System.out.print("Give the dimension of the matrix: ");
    n=scan.nextInt();
    System.out.println("Give the matrix of the dimension "+n+" x "+n+": ");
    int[][] canva=new int[n+2][n+2];
    int farLeft=n+1,farRight=0,farUp=n+1,farDown=0;
    boolean exists=false;
    for(int i=1;i<=n;i++){
        for(int j=1;j<=n;j++){
            int pixel=scan.nextInt();
            if(pixel==1||pixel==0) {
                canva[i][j] = pixel;
                if(pixel==1){
                    exists=true;
                    if(i-1<farUp){
                        farUp=i-1;
                    }
                    if(i+1>farDown){
                        farDown=i+1;
                    }
                    if(j-1<farLeft){
                        farLeft=j-1;
                    }
                    if(j+1>farRight) {
                        farRight=j+1;
                    }
                }
            } else {
                System.out.println("The given pixel color \'"+pixel+"\' is invalid!");
                System.exit(1);
            }
        }
    }

    int nStart=1,nFinal=n,mStart=1,mFinal=n;
    if(exists==true){
        if(farLeft==0){
            mStart=0;
        }
        if(farRight==n+1){
            mFinal=n+1;
        }
        if(farUp==0){
            nStart=0;
        }
        if(farDown==n+1){
            nFinal=n+1;
        }

        for(int i=nStart;i<=nFinal;i++){
            for(int j=mStart;j<=mFinal;j++) {
                if(((i==farUp||i==farDown) && j>=farLeft && j<=farRight)){
                    canva[i][j]=2;
                }
                if(i>=farUp && i<=farDown && (j==farLeft || j==farRight)){
                    canva[i][j]=2;
                }
            }
        }
    }

    System.out.println("\nObiectul bordat:\n");
    for(int i=nStart;i<=nFinal;i++){
        for(int j=mStart;j<=mFinal;j++){
            System.out.print(canva[i][j]+" ");
        }
        System.out.println();
    }
}

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
void main(String[] args) {
    if(args.length!=2 && args.length!=3){
        System.out.println("Incorrect number of parameters!\nIt should have been 2 or 3 parameters!");
        System.exit(1);
    }
    int n=args.length;
    if(args[n-1].equals("circle")||args[n-1].equals("rectangle")){
        if(args[n-1].equals("circle")){
            if(n==2){
                int radius=Integer.parseInt(args[0]);

                long tStart=System.currentTimeMillis();
                Circle c = new Circle(radius);
                String cDraw= c.toString();
                long time=(System.currentTimeMillis()-tStart);

                if(radius>=1000){
                    System.out.println("The time needed to drawn the circle is "+time+" milliseconds.");
                } else {
                    System.out.println("The drawn circle looks like this:\n\n"+cDraw);
                }
            } else {
                System.out.println("The circle shape only needs the radius!");
                System.exit(1);
            }
        } else {
            if(n==3){

                int length=Integer.parseInt(args[0]);
                int width=Integer.parseInt(args[1]);

                long tStart=System.currentTimeMillis();
                Rectangle r=new Rectangle(length,width);
                String rDraw=r.toString();
                long time=(System.currentTimeMillis()-tStart);

                if(length*width>=1000){
                    System.out.println("The time needed to drawn the rectangle is "+time+" milliseconds.");
                } else {
                    System.out.println("The drawn rectangle looks like this:\n\n"+rDraw);
                }
            } else {
                System.out.println("The rectangle shape needs the both the length and width!");
                System.exit(1);
            }
        }
    }
    else{
        System.out.println("The given shape cannot be drawn!");
        System.exit(1);
    }
}

package compulsory.threads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class ExecutingController {
    private Object pauseLock=new Object();
    private volatile boolean isAlive=true;
    private List<Thread> threads=new ArrayList<>();

    public void addThread(Thread threa){
        this.threads.add(threa);
    }

    public void startThreads(){
        for(Thread thread:this.threads){
            thread.start();
        }
    }

    public void killThreads(){
        this.isAlive=false;
        for(Thread thread:this.threads){
            thread.interrupt();
        }
    }

    public void killThreadsExcluding(Thread threadExcept){
        this.isAlive=false;
        for(Thread thread:this.threads){
            if(!thread.equals(threadExcept)){
                thread.interrupt();
            }
        }
    }
}

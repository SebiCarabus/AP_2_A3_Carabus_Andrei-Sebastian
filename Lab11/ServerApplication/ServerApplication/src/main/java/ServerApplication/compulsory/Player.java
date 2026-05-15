package ServerApplication.compulsory;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Player {

    @NonNull
    @EqualsAndHashCode.Include
    private String name;

    @NonNull
    private ClientThread clientThread;

    private int score = 0;
    private long totalResponseTime = 0;

    public void incrementScore() {
        this.score++;
    }

    public void addResponseTime(long timeInMillis){
        this.totalResponseTime+=timeInMillis;
    }
}

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class BotDriver {
    static Timer timer = new Timer();
    static CSGOBot myBot = new CSGOBot();

    public static void main(String[] args) {

        class Task extends TimerTask {
            public void run() {
                try {
                    myBot.readPixels(myBot.takeCap());
                } catch(IOException e) {
                    System.out.println("Error: " + e.getMessage());
                }

                if(myBot.getScore() == 15 && !myBot.getHalftime()) {
                    myBot.halfTime();
                    myBot.swapTeams();
                }

                if(myBot.matchOver()) {
                    timer.cancel();
                    System.out.println("Game Over!");
                    System.out.println("CT: " + myBot.getCTScore()
                            + " T: " + myBot.getTScore());
                }
            }
        }
        Task task = new Task();

        timer.schedule(task, 100, 5000);
    }
}
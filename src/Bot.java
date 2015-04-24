import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URI;

public class Bot {
    public static void main(String[] args) throws InterruptedException {
        Pic pic = new Pic("TWin3.png");
        System.out.println("left: " + pic.getPixel(175, 1242));
        System.out.println("right: " + pic.getPixel(175, 1350));
        System.out.println("top: " + pic.getPixel(119, 1296));
        System.out.println("bottom: " + pic.getPixel(229, 1300));

        // try {
        //     URI uri = new URI("steam://rungameid/730");
        //     if (Desktop.isDesktopSupported()) {
        //         Desktop.getDesktop().browse(uri);
        //     }
        // } catch(Exception e) {
        //     System.out.println("Error: " + e.getMessage());
        // }

        Robot myBot = initialize();
        myBot.waitForIdle();
        myBot.delay(10000);
        // myBot.keyPress(KeyEvent.VK_BACK_QUOTE);
        // myBot.keyRelease(KeyEvent.VK_BACK_QUOTE);
        String text = "hell";
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            myBot.keyPress(Character.toUpperCase(c));
            myBot.keyRelease(Character.toUpperCase(c));
        }
    }

    public static Robot initialize() {
        Robot myBot;
        try {
            myBot = new Robot();
        } catch(AWTException e) {
            System.out.println("Error: " + e.getMessage());
            myBot = null;
        }
        return myBot;
    }
}
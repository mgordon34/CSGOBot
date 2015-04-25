import javax.imageio.ImageIO;
import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;

public class CSGOBot {
    private Robot myBot;
    FileProcessor htmlProcessor;
    FileProcessor cssProcessor;
    private String ip;

    private Pixel tMarker = new Pixel(55, 45, 29, 255);
    private Pixel ctMarker = new Pixel(81, 91, 98, 255);
    private Pixel roundMarker = new Pixel(195, 190, 195, 255);
    private Pixel whiteMarker = new Pixel(255, 255, 255, 255);

    private int count;
    private int tScore;
    private int ctScore;
    private boolean matchStarted;
    private boolean halfTime;

    public CSGOBot() {
        count = 0;
        tScore = 0;
        ctScore = 0;
        matchStarted = false;
        halfTime = false;
        htmlProcessor = new FileProcessor("Z:/public_html/csgobot/index.html");
        cssProcessor = new FileProcessor("Z:/public_html/csgobot/style.css");

        try {
            myBot = new Robot();
        } catch(AWTException e) {
            System.out.println("Error: " + e.getMessage());
            myBot = null;
        }
    }

    public void openGame() {
        try {
            URI uri = new URI("steam://rungameid/730");
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(uri);
            }
        } catch(Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void typeText(String text) {
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            myBot.keyPress(Character.toUpperCase(c));
            myBot.keyRelease(Character.toUpperCase(c));
        }
    }

    public Pic takeCap() throws IOException {
        BufferedImage cap = myBot.createScreenCapture(new Rectangle(553, 112, 813, 195));
        Pic pic = new Pic(cap);
//        p ic.show();
//        File outputfile = new File("res/TTest.png");
//        ImageIO.write(cap, "png", outputfile);
        return pic;
    }

    public void readPixels(Pic pic) {
//        pic.show();
        Pixel matchStartPix = pic.getPixel(170, 499);
        Pixel matchNotWhitePix = pic.getPixel(32, 54);
        Pixel roundPix = pic.getPixel(30, 308);
        Pixel centerPix = pic.getPixel(75, 760);

        System.out.println(roundPix);
        System.out.println(matchNotWhitePix);
        System.out.println(centerPix);
        if(roundPix.inRange(roundMarker, 40) && matchStarted && count > 2) {
            if(centerPix.inRange(tMarker, 20)) {
                System.out.println("Terrorists Win bitch!");
                tWin();
                tScore++;
                count = 0;
            } else if(centerPix.inRange(ctMarker, 20)) {
                System.out.println("Counter Terrorists Win bitch!");
                ctWin();
                ctScore++;
                count = 0;
            } else {
                System.out.println("White checked, no winner");
            }
            System.out.println("CT: " + ctScore + " T: " + tScore);
        } else if(matchStartPix.inRange(whiteMarker, 0) && !matchNotWhitePix.inRange(whiteMarker, 50) &&  count > 2) {
            System.out.println("Match Started!");
            count = 0;
            tScore = 0;
            ctScore = 0;
            if (!cssProcessor.tLeft()) {
                swapTeams();
            }
            matchStarted = true;
        }
        count++;
        System.out.println(count);
    }

    private void tWin() {
        String oldScore = String.valueOf(tScore);
        String newScore = String.valueOf(tScore + 1);
        if (halfTime) {
            htmlProcessor.replaceSelected("rscore\">" + oldScore, "rscore\">" + newScore);
        } else {
            htmlProcessor.replaceSelected("lscore\">" + oldScore, "lscore\">" + newScore);
        }
    }

    private void ctWin() {
        String oldScore = String.valueOf(ctScore);
        String newScore = String.valueOf(ctScore + 1);
        if (halfTime) {
            htmlProcessor.replaceSelected("lscore\">" + oldScore, "lscore\">" + newScore);
        } else {
            htmlProcessor.replaceSelected("rscore\">" + oldScore, "rscore\">" + newScore);
        }
    }

    public int getScore() {
        return tScore + ctScore;
    }

    public void swapTeams() {
        int temp = tScore;
        tScore = ctScore;
        ctScore = temp;

        if (cssProcessor.tLeft()) {
            cssProcessor.replaceSelected("#left.*\"Terrorist.png", "#left { background-image: url(\"CounterTerrorist.png");
            cssProcessor.replaceSelected("#right.*\"CounterTerrorist.png", "#right { background-image: url(\"Terrorist.png");
        } else {
            cssProcessor.replaceSelected("#right.*\"Terrorist.png", "#right { background-image: url(\"CounterTerrorist.png");
            cssProcessor.replaceSelected("#left.*\"CounterTerrorist.png", "#left { background-image: url(\"Terrorist.png");
        }
    }

    public boolean matchOver() {
        if(tScore == 16 || ctScore == 16) {
            return true;
        }
        return false;
    }

    public int getTScore() {
        return tScore;
    }

    public int getCTScore() {
        return ctScore;
    }

    public boolean getHalftime() {
        return halfTime;
    }

    public void halfTime() {
        halfTime = true;
    }

    public static void main(String[] args) throws IOException {
         CSGOBot bot = new CSGOBot();
         bot.swapTeams();
    }
}
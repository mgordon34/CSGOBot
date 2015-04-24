import java.io.*;
import java.io.FileWriter;

/**
 * Created by Matt on 9/11/2014.
 */
public class FileProcessor {

    private String fileName;
    private String line = "";
    private String input = "";
    private BufferedReader fw;
    private BufferedWriter bw;

    public FileProcessor(String fileName) {
        this.fileName = fileName;
    }

    public void loadFile() {
        try {
            fw = new BufferedReader(new FileReader(fileName));
            while ((line = fw.readLine()) != null) {
                input += line + "\n";
            }
            //System.out.println(input);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void writeFile() {
        try {
            bw = new BufferedWriter(new FileWriter(fileName));
            bw.write(input);
            bw.close();
            input = "";
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void replaceSelected(String text, String replaceWith) {
        loadFile();
        input = input.replaceAll(text, replaceWith);
        writeFile();
    }

    public boolean tLeft() {
        loadFile();
        return input.contains("#left { background-image: url(\"Terrorist");
    }

    public static void main(String[] args) {
        FileProcessor fp = new FileProcessor("Z:/public_html/csgobot/style.css");
        System.out.println(fp.tLeft());
    }
}

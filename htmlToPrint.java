import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class htmlToPrint {

    public htmlToPrint() {
        super();
    }

    public static void htmlConvert(String path) {
        try {
            File filein = new File(path);
            File fileout = new File("out.txt");
            Scanner out = new Scanner(filein);
            PrintWriter print = new PrintWriter(fileout);
            while(out.hasNextLine()){
                String read = out.nextLine();
                String readRepl = read.replaceAll("\"", "\'");
                print.println("out.println(\"" + readRepl + "\");");
            }
            out.close();
            print.close();
        } catch (Exception e) {
            System.out.println("Error al crear o modificar " + path + ": " + e);
        }

    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String path = scanner.nextLine();
        htmlConvert(path);
    }

}

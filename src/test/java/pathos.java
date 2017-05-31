import java.nio.file.FileSystem;
import java.nio.file.Path;

import static java.nio.file.FileSystems.getDefault;

/**
 * Created by symph on 31.05.2017.
 */
public class pathos {


    public static void main (String[] args){
        FileSystem fs = getDefault();
        Path path = fs.getPath("graphics//maps");
        String SP = "\\stars1.jpg";
        String abs = path.toAbsolutePath().toString().replace("\\", "\\");
        String texName = "\\default.png";

        System.out.println(abs);
        System.out.println(abs+texName);
    }
}

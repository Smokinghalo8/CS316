import java.io.File;

public class ServerErrorHandling {
    Boolean checkIfFileExists(String file){
        File checkFile = new File("ServerFiles/"+file);
        return checkFile.exists();
    }
}

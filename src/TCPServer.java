import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class TCPServer {
    static ServerErrorHandling errorHandling = new ServerErrorHandling();

    public static void main(String[] args) throws Exception {
        ServerSocketChannel listenChannel = ServerSocketChannel.open();
        listenChannel.bind(new InetSocketAddress(3002));
        while(true) {
            SocketChannel serveChannel = listenChannel.accept();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int bytesRead = serveChannel.read(buffer);
            buffer.flip();
            byte[] byteArray = new byte[bytesRead];
            buffer.get(byteArray);
            String ServerDirectory = "ServerFiles/";
            StringBuilder output= new StringBuilder();

            String clientMessage= new String(byteArray);
            switch(clientMessage){
                case "LIST":
                    output.append(getListOfFiles(ServerDirectory));
                    break;
                case "DELETE":
                    buffer.clear();
                    bytesRead = serveChannel.read(buffer);
                    buffer.flip();
                    byteArray = new byte[bytesRead];
                    buffer.get(byteArray);
                    output.append(deleteFile(byteArray,ServerDirectory));
                    break;
                case "RENAME":
                    buffer.clear();
                    bytesRead = serveChannel.read(buffer);
                    buffer.flip();
                    byteArray = new byte[bytesRead];
                    buffer.get(byteArray);
                    String filenames = new String(byteArray);
                    output.append(renameFile(filenames));
                    break;
                case "DOWNLOAD":
                    break;
                case "UPLOAD":
                    break;
                default:
                    break;
            }


            ByteBuffer replyBuffer = ByteBuffer.wrap(output.toString().getBytes());
            serveChannel.write(replyBuffer);
            serveChannel.close();
        }
    }

    static String getListOfFiles(String fileDirectory){
        StringBuilder output = new StringBuilder();
        File fileDirectoryObject = new File(fileDirectory);
        File[] serverFiles = fileDirectoryObject.listFiles();
        if(serverFiles!=null){
            for (File file : serverFiles){
                output.append(file.toString().substring(12)).append("\n");
            }
        }
        return output.toString();
    }

    static String deleteFile(byte[] byteArray, String ServerDirectory){
        String fileName = new String(byteArray);
        File myObj = new File(ServerDirectory+fileName);
        if (myObj.delete()) {
            return "Deleted the file: " + myObj.getName();
        } else {
            return "Failed to delete the file.";
        }
    }

    static String renameFile(String filenames){
        int indexOfNewLine = filenames.indexOf("\n");
        String oldFilename = filenames.substring(0,indexOfNewLine);
        String newFilename = filenames.substring(indexOfNewLine+1);

        if (errorHandling.checkIfFileExists(oldFilename)){
            File file = new File("ServerFiles/"+oldFilename);
            File rename = new File("ServerFiles/"+newFilename+oldFilename.substring(oldFilename.length()-4));

            if (file.renameTo(rename)) {
                return "File successfully renamed";
            }
            else {
                return "File could not be renamed";
            }
        }
        else {
            return "File does not exist";
        }
    }

}
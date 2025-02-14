import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class TCPServer {
    static ServerErrorHandling errorHandling = new ServerErrorHandling();

    public static void main(String[] args) throws Exception {
        ServerSocketChannel listenChannel = ServerSocketChannel.open();
        listenChannel.bind(new InetSocketAddress(3002));
        while(true) {
            SocketChannel serveChannel = listenChannel.accept();
            String ServerDirectory = "ServerFiles/";

            String clientMessage= getFileName(serveChannel);
            switch(clientMessage){
                case "LIST":
                    getListOfFiles(ServerDirectory,serveChannel);
                    break;
                case "DELETE":
                    deleteFile(getFileName(serveChannel),ServerDirectory,serveChannel);
                    break;
                case "RENAME":
                    renameFile(getFileName(serveChannel),serveChannel);
                    break;
                case "DOWNLOAD":
                    downloadFile(getFileName(serveChannel),serveChannel);
                    break;
                case "UPLOAD":
                    break;
                default:
                    break;
            }
        }
    }
    static void downloadFile(String filename,SocketChannel channel) throws IOException {
        if (errorHandling.checkIfFileExists(filename)){
            FileInputStream fs = new FileInputStream("ServerFiles/"+filename);
            FileChannel fc = fs.getChannel();
            ByteBuffer fileContent = ByteBuffer.allocate(1024);
            int byteRead;
            do {
                byteRead = fc.read(fileContent);
                fileContent.flip();
                channel.write(fileContent);
                fileContent.clear();
            }while(byteRead>=0);
            fs.close();
            channel.close();
        }
    }

    static void getListOfFiles(String fileDirectory, SocketChannel channel) throws IOException {
        StringBuilder output = new StringBuilder();
        File fileDirectoryObject = new File(fileDirectory);
        File[] serverFiles = fileDirectoryObject.listFiles();
        if(serverFiles!=null){
            for (File file : serverFiles){
                output.append(file.toString().substring(12)).append("\n");
            }
        }
        ByteBuffer replyBuffer = ByteBuffer.wrap(output.toString().getBytes());
        channel.write(replyBuffer);
        channel.close();
    }

    static void deleteFile(String filename, String ServerDirectory, SocketChannel channel) throws IOException {
        File myObj = new File(ServerDirectory+filename);
        ByteBuffer replyBuffer;

        if (myObj.delete()) {
            replyBuffer = ByteBuffer.wrap(("Deleted the file: " + myObj.getName()).getBytes());
        } else {
            replyBuffer = ByteBuffer.wrap(("File does not exist").getBytes());
        }
        channel.write(replyBuffer);
        channel.close();
    }

    static void renameFile(String filenames, SocketChannel channel) throws IOException {
        int indexOfNewLine = filenames.indexOf("\n");
        String oldFilename = filenames.substring(0,indexOfNewLine);
        String newFilename = filenames.substring(indexOfNewLine+1);
        ByteBuffer replyBuffer;

        if (errorHandling.checkIfFileExists(oldFilename)){
            File file = new File("ServerFiles/"+oldFilename);
            File rename = new File("ServerFiles/"+newFilename+oldFilename.substring(oldFilename.length()-4));

            if (file.renameTo(rename)) {
                replyBuffer = ByteBuffer.wrap("File successfully renamed".getBytes());
            }
            else {
                replyBuffer = ByteBuffer.wrap("File could not be renamed".getBytes());
            }
        }
        else {
            replyBuffer = ByteBuffer.wrap("File does not exist".getBytes());
        }
        channel.write(replyBuffer);
        channel.close();
    }

    static String getFileName(SocketChannel channel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int bytesRead = channel.read(buffer);
        buffer.flip();
        byte[] byteArray = new byte[bytesRead];
        buffer.get(byteArray);
        return new String(byteArray);
    }
}
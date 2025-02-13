import java.io.File;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class TCPServer {
    public static void main(String[] args) throws Exception {
        ServerSocketChannel listenChannel = ServerSocketChannel.open();
        listenChannel.bind(new InetSocketAddress(3002));
        while(true) {
            SocketChannel serveChannel = listenChannel.accept();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int bytesRead = serveChannel.read(buffer);
            buffer.flip();
            byte[] a = new byte[bytesRead];
            buffer.get(a);
            String ServerDirectory = "ServerFiles/";
            StringBuilder output= new StringBuilder();

            String clientMessage= new String(a);
            switch(clientMessage){
                case "LIST":
                    output.append(getListOfFiles(ServerDirectory));
                    break;
                case "DELETE":
                    buffer.clear();
                    bytesRead = serveChannel.read(buffer);
                    buffer.flip();
                    a = new byte[bytesRead];
                    buffer.get(a);
                    output.append(deleteFile(a,ServerDirectory));
                    break;
                case "RENAME":
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
    static String deleteFile(byte[] a, String ServerDirectory){
        String fileName = new String(a);
        File myObj = new File(ServerDirectory+fileName);
        if (myObj.delete()) {
            return "Deleted the file: " + myObj.getName();
        } else {
            return "Failed to delete the file.";
        }
    }
}
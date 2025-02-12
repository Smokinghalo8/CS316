import java.io.File;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class server {
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
            //String fileName = new String(a);
            String ServerDirectory = "ServerFiles/";
            String ResponseBack;
            StringBuilder output= new StringBuilder();

            //added from tcp server code
            String clientMessage= new String(a);
            System.out.println("Client Message: "+clientMessage);
            switch(clientMessage){
                case "LIST"://UNTESTED
                    File serverDirectoryObject = new File(ServerDirectory);
                    File[] serverFiles = serverDirectoryObject.listFiles();
                    if(serverFiles!=null){
                        for (File file : serverFiles){
                            output.append(file.toString()).append("\n");
                        }
                    }
                    break;
                case "DELETE":
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

            //reply back to client
            ByteBuffer replyBuffer = ByteBuffer.wrap(output.toString().getBytes());
            serveChannel.write(replyBuffer);
            serveChannel.close();
//          String clientMessage = new String(a);

            //System.out.println("Filename : " + fileName);

//          ByteBuffer replyBuffer = ByteBuffer.wrap(clientMessage.toUpperCase().getBytes());

            //File file = new File("ServerFiles/"+fileName);


            /*
            if(!file.exists()) {
                System.out.println("File doesn't exist");
            } else {
                FileInputStream fs = new FileInputStream("ServerFiles/" + fileName);
                FileChannel fc = fs.getChannel();
                ByteBuffer fileContent = ByteBuffer.allocate(1024);

                do{
                    bytesRead = fc.read(fileContent);
                    fileContent.flip();
                    serveChannel.write(fileContent);
                    fileContent.clear();
                }
                while(bytesRead >= 20);
                serveChannel.close();
            }//end else
            */


        }
    }
}
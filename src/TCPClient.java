import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;


public class TCPClient {
    public static void main(String[] args) throws Exception {
        ServerErrorHandling errorHandling = new ServerErrorHandling();

        int serverPort = Integer.parseInt(args[1]);
        Scanner keyboard = new Scanner(System.in);
        String commandName;

        errorHandling.CheckForCorrectAmountOfArguments(args);

        do {
            System.out.println("Please Enter A Command:");
            System.out.println("List. List of File \nDelete. Delete a File \nRename. Rename a File \nUpload. Upload a File \nDownload. Download a File");
            commandName = keyboard.nextLine();
            commandName = commandName.toUpperCase();
        }
        while (!errorHandling.CheckForProperCommand(commandName));

        SocketChannel channel = SocketChannel.open();
        channel.connect(new InetSocketAddress(args[0],serverPort));

        ByteBuffer buffer = ByteBuffer.wrap(commandName.getBytes());

        channel.write(buffer);
        channel.shutdownOutput();

        listOutput(channel);

//        FileOutputStream fs = new FileOutputStream("ClientFiles/"+filename,true);
//        FileChannel fc = fs.getChannel();
//
//        while(channel.read(fileContent)>=0){
//            replyBuffer.flip();
//            fc.write(fileContent);
//            replyBuffer.clear();
//        }
//        fs.close();
//        channel.close();
   }

   static void listOutput(SocketChannel channel) throws IOException {
       ByteBuffer replyBuffer = ByteBuffer.allocate(1024);
       int bytesRead = channel.read(replyBuffer);
       channel.close();
       replyBuffer.flip();
       byte[] a = new byte[bytesRead];
       replyBuffer.get(a);
       System.out.println(new String(a));
   }
}

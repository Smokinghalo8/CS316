import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class client {
    public static void main(String[] args) throws Exception{
        if (args.length != 2) {
            System.out.println("Please provide <serverIP> and <serverPort>");
            return;
        }
        int serverPort = Integer.parseInt(args[1]);
        Scanner keyboard = new Scanner(System.in);
        String filename = keyboard.nextLine(); //get message

        SocketChannel channel = SocketChannel.open();
        channel.connect(new InetSocketAddress(args[0], serverPort)); //3 way handshake (establish channel connection)

        ByteBuffer buffer = ByteBuffer.wrap(filename.getBytes()); //wrap message to bytes
        channel.write(buffer); //write bytes to channel

        channel.shutdownOutput(); //request closure of output channel

        FileOutputStream fs = new FileOutputStream("ClientFiles/"+filename, true);
        FileChannel fc = fs.getChannel();
        ByteBuffer fileContent = ByteBuffer.allocate(1024);
        while(channel.read(fileContent) >= 0) {
            fileContent.flip();
            fc.write(fileContent);
            fileContent.clear();
        }
        fs.close();
        channel.close();
    }
}
package client;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class PersonCl {
    String Name;
    String Ip;
    int Port;
    Socket socket;
    BufferedReader reader;
    BufferedWriter writer;

    public PersonCl(String name, String ip, String port) throws IOException {
        this.Ip = ip.trim();
        this.Port = Integer.valueOf(port.trim());
        this.Name = name.trim();
        socket = new Socket(Ip, Port);
        System.out.println("Client Socket is initialized");
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }


    public void sendMessage(String message) {
        try {
            if (socket.isClosed()) {
                System.out.println("connection closed!");
                return;
            }
            System.out.println("you say: " + message.split(":")[1]);
            writer.write(message + "\n");
            writer.flush();
            String line = reader.readLine();
            System.out.println(Name + " says: " + line);

            if(line.contains("bye")){
                reader.close();
                writer.close();
                socket.close();
                System.out.println("connection close");
            }
        } catch (SocketException se){
            se.printStackTrace();
            System.out.println("connection problem");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

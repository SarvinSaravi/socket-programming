package client;

import server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
    DatagramSocket clientSocket;
    String username = "Unknown";
    ArrayList<PersonCl> chatList;

    public Client(int portNumber) throws SocketException {
        clientSocket = new DatagramSocket(portNumber);
        chatList = new ArrayList<PersonCl>();
    }

    public void run() throws IOException {
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

        while (true){
            System.out.println("Please enter something in this format: receiver#command");
            String line = consoleReader.readLine();

            if (line.startsWith("server")){
                line = line.split("#")[1];

                if (line.startsWith("logout"))
                    line = line + " " + username;

//                send to server
                DatagramPacket packet = new DatagramPacket(line.getBytes(), line.getBytes().length, InetAddress.getByName("localhost"), 2020);
                clientSocket.send(packet);


//                response from server
                byte[] buffer = new byte[1024];
                DatagramPacket receivedPacket = new DatagramPacket(buffer, buffer.length);
                clientSocket.receive(receivedPacket);

                String serverRes = new String(receivedPacket.getData());
                System.out.println(serverRes);

//                checking server response
                if (serverRes.startsWith("Welcome")){
                    username = serverRes.split(" ")[1];
                    System.out.println("username is " + username);
                }else if (serverRes.startsWith("Address")){
                    if (serverRes.contains("user_is_not_online")){
                        System.out.println("not online");
                        continue;
                    }
                    String[] tokens = serverRes.split(" ");
                    line = line.substring(8);
                    PersonCl personClient = new PersonCl(line, tokens[1], tokens[2]);
                    chatList.add(personClient);
                }else if (line.startsWith("logout")) {
                    if (serverRes.contains("successful")){
                        System.out.println("goodbye " + username);
                        username = "Unknown";
                        break;
                    }else {
                        System.out.println(serverRes);
                    }
                }
            }else if (line.contains("#")){
                String[] tokens = line.split("#");
                Boolean flag = false;
                for (PersonCl pc: chatList){
                    if (pc.Name.equalsIgnoreCase(tokens[0].trim())){
                        flag = true;
                        tokens[0] = username.trim();
                        tokens[1] = tokens[1].trim();
                        String msg = tokens[0] + ":" + tokens[1];
                        pc.sendMessage(msg.trim());
                        break;
                    }
                }
                if (!flag){
                    System.out.println("no connection");
                }
            }else {
                System.out.println("Mismatch to format - back to role selecting");
                break;
            }
        }
    }


    public static void main(String[] args) {
        Client client = null;
        Server serverClient = null ;
        Scanner myObj = new Scanner(System.in);
        System.out.println("enter your port :");
        int portNumber = myObj.nextInt();
        try {
            serverClient = new Server(portNumber);
            client = new Client(portNumber);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        while (true) {
            try {
//                client/server
                System.out.println("please specify your role : (client/server/exit)");
                BufferedReader roleReader = new BufferedReader(new InputStreamReader(System.in));
                String bekhoon = roleReader.readLine();
                if (bekhoon.startsWith("server")) {
                    serverClient.run();
                } else if (bekhoon.startsWith("client")) {
                    client.run();
                }else if (bekhoon.startsWith("exit")){
                    System.out.println("goodbye");
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}

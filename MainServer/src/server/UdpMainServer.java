package server;

import java.lang.*;
import dataStructure.Person;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class UdpMainServer {
    DatagramSocket serverSocket;
    ArrayList<Person> usersList;

    public UdpMainServer() throws SocketException {
        serverSocket = new DatagramSocket(2020);
        usersList = new ArrayList<Person>();
    }

    public void run() throws IOException {
        while (true){
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            System.out.println("Server is listening on port " + serverSocket.getLocalPort());
            serverSocket.receive(packet);

            String line = new String(packet.getData());
            String response = null;
            System.out.println("server receive : " + line);

            if (line.startsWith("login")){
                String[] tokens = line.split(" ");
                int loginIndex = findPerson(tokens[1]);
                if (loginIndex < 0){
                    Person person = new Person(tokens[1]);
                    person.login(packet.getAddress().getHostAddress(), String.valueOf(packet.getPort()));
                    usersList.add(person);
                    response = "Welcome " + tokens[1];
                }else if (loginIndex >= 0){
                    response = "duplicate user";
//                    Person p = usersList.get(loginIndex);
//                    p.logout();
                }
            } else if (line.startsWith("connect")){
                String[] tokens = line.split(" ");
                int connectIndex = findPerson(tokens[1]);
                if (connectIndex < 0){
                    response = "no connecting ...";
                }else if (connectIndex >= 0 ){
                    Person p = usersList.get(connectIndex);
                    response = "Address: " + p.getIpAddress() + " " + p.getPortAddress();
                }
            } else if (line.startsWith("logout")){
                String[] tokens = line.split(" ");
                int logoutIndex = findPerson(tokens[1]);
                if (logoutIndex < 0){
                    response = "failed to log out";
                }else {
                    Person person = usersList.get(logoutIndex);
                    person.logout();
                    response = "successful log out";
                }
            }
            DatagramPacket sendPacket = new DatagramPacket(response.getBytes(), response.getBytes().length, packet.getAddress(), packet.getPort());
            serverSocket.send(sendPacket);
        }
    }

    public int findPerson(String name){
        int index = -1;
        Person person;
        for (int i=0 ; i<usersList.size(); i++){
            person = usersList.get(i);
            if (person.name.trim().equalsIgnoreCase(name.trim())){
                System.out.println("founded!");
                index = i;
                break;
            }
        }
        return index;
    }

    public static void main(String[] args) {
        try {
            UdpMainServer server = new UdpMainServer();
            server.run();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

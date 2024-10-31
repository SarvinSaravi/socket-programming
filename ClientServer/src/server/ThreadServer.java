package server;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ThreadServer  implements Runnable {
    Socket connectionSocket;
    BufferedReader reader;
    BufferedWriter writer;
    Scanner consolReader;

    public ThreadServer(Socket connectionSocket) throws IOException {
        this.connectionSocket = connectionSocket;
        reader = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(connectionSocket.getOutputStream()));
    }


    @Override
    public void run() {
        System.out.println("Local IP : " +
                connectionSocket.getLocalAddress().getHostAddress());
        System.out.println("Local Port : " +
                connectionSocket.getLocalPort());
        System.out.println("Remote IP : " +
                connectionSocket.getInetAddress().getHostAddress());
        System.out.println("Remote Port : " +
                connectionSocket.getPort());

        while (true) {
            try {
                String line = reader.readLine();
                String[] lineTokens = line.split(":");
                System.out.println("Server received: " + lineTokens[1]);
                System.out.println("from: " + lineTokens[0] + "@" + connectionSocket.getInetAddress().getHostAddress() + "@" + connectionSocket.getPort());

                System.out.println("your answer: ");

                consolReader = new Scanner(System.in);
                String answer;
                answer = consolReader.nextLine();

                answer = answer.trim();

                writer.write(answer + "\n");
                writer.flush();

                System.out.println("Message Sent!");
                if (line.contains("bye")){
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        try {
            reader.close();
            writer.close();
            connectionSocket.close();
            System.out.println("connection closed!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

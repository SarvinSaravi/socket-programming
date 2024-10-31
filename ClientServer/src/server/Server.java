package server;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    ServerSocket serverSocket;
    ExecutorService pool;

    public Server(int portNum) throws IOException {
        serverSocket = new ServerSocket(portNum);
        pool = Executors.newFixedThreadPool(5);
    }

    public void run() throws IOException {
        while (true){
            System.out.println("Are you really wan\'t to continue server ? (y/n) ");
            BufferedReader userSay = new BufferedReader(new InputStreamReader(System.in));
            String sayUser = userSay.readLine();
            if (sayUser.startsWith("n"))
                break;
            System.out.println("Server Socket starts listening on port " + serverSocket.getLocalPort());
            Socket connectionSocket = serverSocket.accept();
            System.out.println("Server Socket find a new connection");
            ThreadServer serverThread = new ThreadServer(connectionSocket);
            pool.execute(serverThread);

        }
    }
}

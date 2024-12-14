package application.chatapp_dialog;

import java.net.*;
import java.util.concurrent.*;
import java.io.*;

public class SocketServer {
    public static void main(String []args){
        int  serverPort = 1234;
        try (ServerSocket server = new ServerSocket(serverPort)) {
            ExecutorService pool = Executors.newFixedThreadPool(3);
            while(true){
                System.out.println("Waiting for client...");
                Socket client = server.accept();
                System.out.println("A client is connected");

                // RunnableActivation r = new RunnableActivation(client);
                // Thread clientThread = new Thread(r);
                // clientThread.start();

                RunnableActivation r = new RunnableActivation(client);
                pool.submit(r);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

class RunnableActivation implements Runnable{
    private Socket client;
    public RunnableActivation(Socket client){
        this.client = client;
    }

    @Override
    public void run() {
        try {
            InputStream in = client.getInputStream();
            OutputStream out = client.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));

            while (true) {
                String s = reader.readLine();
                System.out.println("Client sent: " + s);

                writer.write(s.toUpperCase() + "\n");
                writer.flush();

                if (s.equals("exit")) {
                    break;
                }
            }
            System.out.println("Client disconnected");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
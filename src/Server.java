import jdk.jfr.ContentType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

public class Server {
    public static void main(String[] args) throws IOException {

        String directoryRoot=args[0];
        String port=args[1];
        //String directoryRoot="/Users/akshatakadam/Documents/COEN317/";
        //String port="8001";
        ServerSocket server = new ServerSocket(Integer.parseInt(port));
        while (true) {
            Socket socket = server.accept();
            WorkerThread workerThread= new WorkerThread(socket,directoryRoot,server);
            workerThread.start();

        }
    }



}


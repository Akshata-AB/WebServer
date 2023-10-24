import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;

public class WorkerThread extends Thread{

    private Socket socket;
    private String directoryRoot;
    private ServerSocket server;
    public WorkerThread(Socket socket,String directoryRoot,ServerSocket server){
        this.socket=socket;
        this.directoryRoot=directoryRoot;
        this.server=server;
    }
    public void run(){

        try {
            handleClientAndProcessRequest(socket,directoryRoot,server);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    static void handleClientAndProcessRequest(Socket socket, String directoryRoot, ServerSocket server) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String input = bufferedReader.readLine();
        if (!(input.isBlank())) {

            String path = parseRequest(input,bufferedReader);
            if(path.equals("badRequest")){
                createResponse("400 Error",socket,"text/html",("<html><header>400 ERROR</header> <body> INCORRECT REQUEST SENT</body><html>").getBytes());
            }else{
                Path redirectedPath=retrieveFilePath(path,directoryRoot);
                if(Files.exists(redirectedPath)){
                    if(!(Files.isReadable(redirectedPath))){
                        createResponse("403 Error",socket,"text/html",("<html><div> 403 - ACCESS DENIED\n </div>" +
                                " <div>You don't have permission to access this page.\n").getBytes());
                    }else{
                        String contentType = Files.probeContentType(redirectedPath);
                        createResponse("200 OK",socket,contentType,Files.readAllBytes(redirectedPath));
                    }
                }else{
                   createResponse("404 Error",socket,"image/jpeg",Files.readAllBytes(Path.of("./404.jpg")));
                }
                //Close connection after request
                socket.close();
            }
        }
    }

    private static String parseRequest(String input,BufferedReader bufferedReader) throws IOException {

        String[] httpRequest = input.split("\r\n");
        System.out.println("Received http request is : " + httpRequest[0]);
        String[] firstLine = httpRequest[0].split(" ");
        String path = firstLine[1];
       if( path.contains("%") || path.contains("@") || path.contains(" ")){
             System.out.println("Bad Path"+ path);
             return "badRequest";
         }
        String method = firstLine[0];
        String version = firstLine[2];
        String headers= bufferedReader.readLine();
        return path;
    }

    private static void createResponse(String code, Socket socket, String contentType,byte[] fileContents) throws IOException {
        Calendar calendar=Calendar.getInstance();
        OutputStream responseOutput=socket.getOutputStream();
        responseOutput.write(("HTTP/1.1 " +code).getBytes() );
        responseOutput.write(("\r\n").getBytes());
        responseOutput.write(("content-type: " + contentType + "\r\n").getBytes());
        responseOutput.write(("date: " + new Date()+ "\r\n").getBytes());
        responseOutput.write(("content-length: " + fileContents.length).getBytes());
        responseOutput.write("\r\n".getBytes());
        responseOutput.write("\r\n".getBytes());
        responseOutput.write(fileContents);
        responseOutput.write("\r\n\r\n".getBytes());
        responseOutput.flush();
        responseOutput.close();
    }

    private static Path retrieveFilePath(String path,String directoryRoot){
        String finalPath;
        if(path.equals("/")){
            finalPath = "index.html";
        }else{
            finalPath= path;
        }
        return Paths.get(directoryRoot,finalPath);

    }
}

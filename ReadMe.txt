Akshata Bhonsle
Programming Assignment 1 COEN 317

Server.java class created to create a distributed web server. This class accepts parameters directory root path and the port to run.
The Server class spawns a new thread for every client connection made. The request is parsed and validated for any errors. 
If no resource is specified in the request the server will display the index.html file stored at the root directory path. 
If the resource is not found, a 404 error is returned along with an 404 image file.
If file is found and can be read, its contents are sent in the response. 
Response supports all important headers like Content-Type, Content-Length, and Date headers.Web server also supports image file content in case of 404 error.


Files-
Server.java
WorkerThread.java
404.jpg
ReadMe.txt
ProgramResults.docx

Instruction to run
a. Go to src folder of the project
b. Compile program using below command from terminal - javac Server.java
c. Run program using below command- java Server /Users/Documents/COEN317/ 8001

Here /Users/akshatakadam/Documents/COEN317/ is the directory_root and 8001 is the port
directory_root= where you will store the index.html and other files to access



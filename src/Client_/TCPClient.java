package Client_;
import java.io.*;
import java.net.*;
public class TCPClient{

        public void test() throws Exception{
                String sentence;
                String modifiedSentence;
                BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
                Socket clientSocket = new Socket("172.20.10.11", 6789);
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                BufferedReader inFromServer = new BufferedReader(new
                InputStreamReader(clientSocket.getInputStream()));
                sentence = inFromUser.readLine();
                outToServer.writeBytes(sentence + '\n');
                modifiedSentence = inFromServer.readLine();
                System.out.println("FROM SERVER: " + modifiedSentence);
                
                clientSocket.close();
        }
}
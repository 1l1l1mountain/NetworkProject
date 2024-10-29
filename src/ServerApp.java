import Server2.*;

public class ServerApp {

    
    public static void main(String argv[]){

            //Chat serverChat = new Chat();
            //serverChat.print();
     
            
        try {
            TCPServer tcpServer = new TCPServer();
            tcpServer.test();  // 예외가 발생할 수 있는 메서드 호출
        } 
        catch (Exception e) {
            e.printStackTrace();  // 예외 처리
        }

    }

}

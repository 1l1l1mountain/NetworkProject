import Client_.*;

public class ClientApp {

    
    public static void main(String argv[]){

            //Chat clientChat = new Chat();
            //clientChat.print();
    
            try {
                TCPClient tcpClient = new TCPClient();
                tcpClient.test();  // 예외가 발생할 수 있는 메서드 호출
            } catch (Exception e) {
                e.printStackTrace();  // 예외 처리
            }
    }
}

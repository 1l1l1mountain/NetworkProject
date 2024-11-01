import java.io.IOException;
import java.util.Scanner;

public class Login {

    public boolean DoLogin(Scanner sc) throws IOException{
            //NetStream stream = new NetStream();
            //NetStream.print(NetStream.Print.InputUser);
            //String user = sc.nextLine();
            
            //NetStream.print(NetStream.Print.InputPassword);
            //String pass = sc.nextLine();

            //sendCommand 처리후 NETSTREAM으로 이동 

            // 로그인 요청 보내기 (id)
            NetStream.SendCommand("USER " + user);
            // 로그인 응답 받기
            String userResponse = NetStream.reader.readLine();
            // 로그인 응답 출력
            NetStream.print(NetStream.Print.ServerReq);
            System.out.println(userResponse);

            if (!userResponse.startsWith("3") && !userResponse.startsWith("2")) {
                System.out.println("User ID 가 없습니다.");
                return false;
            }
    
            // 로그인 요청 보내기 (비번)
            NetStream.SendCommand("PASS " + pass);
            // 로그인 응답 받기
            String passResponse = NetStream.reader.readLine();
            NetStream.print(NetStream.Print.ServerReq);
            System.out.println(passResponse);
    
            if (!userResponse.startsWith("3") && !userResponse.startsWith("2")) {
                System.out.println("User 비번이 맞지 않습니다.");
                return false;
            }

            //로그인 성공
            return true;
            

    }
}

import java.io.*;

public class Main {
    

    public static void main(String[] args) {
        try {
            mainProc();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void mainProc() throws IOException {
        
        // 소켓 연결
        // 서버 주소 입력 리팩토링
        NetStream stream = new NetStream();
        stream.Init();

        // Read initial 커넥션 응답 받기
        String connectResponse = NetStream.reader.readLine();
        // 응답 출력
        NetStream.print(NetStream.Print.ServerReq);
        System.out.println(connectResponse);

        Login login = new Login();

        // 로그인 시도
        if (!login.DoLogin(NetStream.sc)) {
            System.out.println("--------로그인 실패--------");
            return;
        }


        // 커멘드 실행
        boolean isLoop = true;
        while(isLoop) {
            NetStream.print(NetStream.Print.ShowMenu);
            isLoop = Command.LoopCommand(NetStream.sc.nextLine());
        }

        
        // 종료 응답
        doQuit();
        //연결 닫기
        NetStream.sc.close();
        NetStream.controlSocket.close();
        System.out.println("Disconnected.");
    
    }
  
    public static void doQuit() throws IOException {
        // QUIT 요청
        NetStream.SendCommand("QUIT");
        // 서버 응답 및 출력
        System.out.println(NetStream.ReceiveResponse());
    }

    
}
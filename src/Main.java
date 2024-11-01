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
        
        //loginUI
        LoginUI loginUI = new LoginUI();
        loginUI.Show();

        /* 
        // Read initial 커넥션 응답 받기
        String connectResponse = NetStream.reader.readLine();
        // 응답 출력
        NetStream.print(NetStream.Print.ServerReq);
        System.out.println(connectResponse);*/

        /*Login login = new Login();

        // 로그인 시도
        if (!login.DoLogin(NetStream.sc)) {
            System.out.println("--------로그인 실패--------");
            return;
        }*/


        // 커멘드 실행
        boolean isLoop = true;
        while(isLoop) {
            NetStream.print(NetStream.Print.ShowMenu);
            isLoop = Command.LoopCommand(NetStream.sc.nextLine());
        }

        // 종료 응답
        doQuit();
    }
  
    public static void doQuit() throws IOException {
        NetStream.SendCommand("QUIT");
        System.out.println(NetStream.ReceiveResponse());
        NetStream.controlSocket.close();
        System.out.println("Disconnected.");
    }

    
}
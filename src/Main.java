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
            isLoop = Command.LoopCommand(NetStream.sc.next());
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

    public static String[] enterPassiveMode() throws IOException {
        NetStream.SendCommand("PASV");
        String response = NetStream.reader.readLine();
        System.out.println("Entering Passive Mode: " + response);
    
        // PASV 응답이 올바른지 확인
        if (!response.startsWith("227")) {
            throw new IOException("PASV 명령 실패: " + response);
        }
    
        try {
            // 괄호 안의 숫자들을 추출
            int startIndex = response.indexOf('(');
            int endIndex = response.indexOf(')');
            if (startIndex == -1 || endIndex == -1) {
                throw new IOException("PASV 응답 형식 오류: " + response);
            }
    
            String[] numbers = response.substring(startIndex + 1, endIndex).split(",");
            if (numbers.length != 6) {
                throw new IOException("PASV 응답의 숫자 개수가 잘못됨: " + response);
            }
    
            // IP 주소와 포트 계산
            String ip = numbers[0].trim() + "." + 
                       numbers[1].trim() + "." + 
                       numbers[2].trim() + "." + 
                       numbers[3].trim();
            int port = (Integer.parseInt(numbers[4].trim()) * 256) + 
                       Integer.parseInt(numbers[5].trim());
    
            return new String[]{ip, String.valueOf(port)};
        } catch (Exception e) {
            System.out.println("PASV 모드 진입 중 오류: " + e.getMessage());
            System.out.println("서버 응답: " + response);
            throw new IOException("PASV 모드 설정 실패", e);
        }
    }
}
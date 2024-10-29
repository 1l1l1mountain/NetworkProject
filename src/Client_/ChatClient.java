package Client_;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {
    public static void main(String[] args) {
        /*if (args.length != 1) {
            System.out.println("사용법 : java ChatClient 닉네임 : " + args[0] );
            return;
        }*/
        String name ="";
        try{
            //InetAddress localHost = InetAddress.getLocalHost();
            //name = localHost.getHostAddress(); // IP 주소를 문자열로 추출
            
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
            String line = keyboard.readLine();
            name = line;
        }
        catch(Exception e){
            System.out.println("ip 추출 오류");
        }
        
        try (Socket socket = new Socket("localhost", 8888);
             BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()))) {

            // 닉네임 전송
            pw.println(name + "이 접속하였습니다.");
            pw.flush();

            // 메시지 수신 스레드 시작
            Thread receiveThread = new Thread(new ReceiveTask(br));
            receiveThread.start();

            // 키보드 입력을 통해 서버로 메시지 전송
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
            String line;
            while ((line = keyboard.readLine()) != null) {
                if ("/quit".equals(line)) {
                    pw.println("/quit");
                    pw.flush();
                    break;
                }
                pw.println( name + " : "+ line);
                pw.flush();
            }

            // 스레드가 종료될 때까지 잠시 대기
            receiveThread.join();
            System.out.println("클라이언트가 종료되었습니다.");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("서버가 꺼져있는지 확인해보세요.");
        }
    }
}

// 서버로부터 메시지를 수신하는 스레드
class ReceiveTask implements Runnable {
    private final BufferedReader br;

    public ReceiveTask(BufferedReader br) {
        this.br = br;
    }

    @Override
    public void run() {
        try {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println("서버: " + line);
            }
        } catch (Exception e) {
            System.out.println("메시지 수신 중 오류가 발생했습니다.");
        } finally {
            System.out.println("수신 스레드가 종료되었습니다.");
        }
    }
}

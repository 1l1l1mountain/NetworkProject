
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class SimpleFtpClient {

    private static final String SERVER_ADDRESS = "192.168.19.154"; // FTP 서버 주소
    private static final int CONTROL_PORT = 2221; // FTP 기본 포트

    public static void main(String[] args) {
        try (Socket controlSocket = new Socket(SERVER_ADDRESS, CONTROL_PORT); PrintWriter out = new PrintWriter(controlSocket.getOutputStream(), true); BufferedReader in = new BufferedReader(new InputStreamReader(controlSocket.getInputStream())); Scanner scanner = new Scanner(System.in)) {

            System.out.println(in.readLine()); // 서버 환영 메시지

            // 익명 사용자 인증
            out.println("USER anonymous"); // 익명 사용자 이름
            System.out.println(in.readLine()); // 사용자 명령 응답

            out.println("PASS"); // 비밀번호는 빈 값으로 두기
            System.out.println(in.readLine()); // 비밀번호 명령 응답

            String command;
            while (true) {
                System.out.print("명령 입력 (RETR, STOR, QUIT): ");
                command = scanner.nextLine();

                if (command.equalsIgnoreCase("QUIT")) {
                    out.println("QUIT");
                    System.out.println(in.readLine());
                    break;
                } else if (command.startsWith("RETR") || command.startsWith("STOR")) {
                    String[] parts = command.split(" ");
                    if (parts.length < 2) {
                        System.out.println("파일 이름을 입력해야 합니다.");
                        continue;
                    }
                    String fileName = parts[1];

                    // 데이터 연결 설정
                    try (Socket dataSocket = new Socket(SERVER_ADDRESS, CONTROL_PORT + 1)) { // 데이터 포트 조정
                        if (command.startsWith("RETR")) {
                            out.println("RETR " + fileName);
                            downloadFile(fileName, dataSocket);
                        } else if (command.startsWith("STOR")) {
                            out.println("STOR " + fileName);
                            uploadFile(fileName, dataSocket);
                        }
                    }
                } else {
                    System.out.println("지원하지 않는 명령입니다.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void downloadFile(String fileName, Socket dataSocket) {
        try (BufferedReader dataIn = new BufferedReader(new InputStreamReader(dataSocket.getInputStream())); BufferedWriter fileWriter = new BufferedWriter(new FileWriter("downloaded_" + fileName))) {
            String line;
            while ((line = dataIn.readLine()) != null) {
                fileWriter.write(line);
                fileWriter.newLine();
            }
            System.out.println("파일 다운로드 완료: " + fileName);
        } catch (IOException e) {
            System.out.println("파일 다운로드 실패: " + e.getMessage());
        }
    }

    private static void uploadFile(String fileName, Socket dataSocket) {
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            try (BufferedReader fileReader = new BufferedReader(new FileReader(file)); PrintWriter dataOut = new PrintWriter(dataSocket.getOutputStream(), true)) {
                String line;
                while ((line = fileReader.readLine()) != null) {
                    dataOut.println(line);
                }
                System.out.println("파일 업로드 완료: " + fileName);
            } catch (IOException e) {
                System.out.println("파일 업로드 실패: " + e.getMessage());
            }
        } else {
            System.out.println("파일이 존재하지 않습니다: " + fileName);
        }
    }
}

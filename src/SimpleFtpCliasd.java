import java.io.*;
import java.net.*;
import java.util.Scanner;

public class SimpleFtpClient {
    private static final String SERVER_ADDRESS = "ftp.dlptest.com"; // FTP 서버 주소
    private static final int PORT = 21; // FTP 기본 포트

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println(in.readLine()); // 서버 환영 메시지

            // 익명 사용자 인증
            out.println("USER dlpuser");
            System.out.println(in.readLine()); // 사용자 명령 응답

            out.println("PASS rNrKYTX9g7z3RgJRmxWuGHbeu"); // 익명 사용자 비밀번호
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

                    if (command.startsWith("RETR")) {
                        out.println("RETR " + fileName);
                        downloadFile(fileName, in);
                    } else if (command.startsWith("STOR")) {
                        out.println("STOR " + fileName);
                        uploadFile(fileName, out, scanner);
                    }
                } else {
                    System.out.println("지원하지 않는 명령입니다.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void downloadFile(String fileName, BufferedReader in) {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter("downloaded_" + fileName))) {
            String line;
            while ((line = in.readLine()) != null) {
                fileWriter.write(line);
                fileWriter.newLine();
                if (line.contains("226 Transfer complete.")) {
                    break;
                }
            }
            System.out.println("파일 다운로드 완료: " + fileName);
        } catch (IOException e) {
            System.out.println("파일 다운로드 실패: " + e.getMessage());
        }
    }

    private static void uploadFile(String fileName, PrintWriter out, Scanner scanner) {
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = fileReader.readLine()) != null) {
                    out.println(line);
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

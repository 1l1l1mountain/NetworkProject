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
                } 
                else if (command.startsWith("RETR") || command.startsWith("STOR")) {
                    String[] parts = command.split(" ");
                    if (parts.length < 2) {
                        System.out.println("파일 이름을 입력해야 합니다.");
                        continue;
                    }
                    String fileName = parts[1];

                    if (command.startsWith("RETR")) {
                        out.println("PASV"); // 패시브 모드로 전환
                        StringBuilder ip = new StringBuilder();
                        
                        int dataPort = enterPassiveMode(in, ip); // 데이터 포트 가져오기
                        String dataIP = ip.toString(); // StringBuilder를 String으로 변환

                        try (Socket dataSocket = new Socket(dataIP, dataPort)) {
                            // 이제 dataSocket을 통해 파일 전송
                            // 예를 들어, 파일 다운로드
                            out.println("RETR "+ fileName); // 파일 다운로드 명령
                            downloadFile(fileName,dataIP,dataPort,dataSocket); // 데이터 소켓을 통해 파일 다운로드
                        }      
                        catch (IOException e) {
                                    e.printStackTrace();
                        }
                    }
                    else if (command.startsWith("STOR")) {
                        out.println("PASV"); // 패시브 모드로 전환
                        StringBuilder ip = new StringBuilder();
                        int dataPort = enterPassiveMode(in,ip); // 데이터 포트 가져오기
                        out.println("STOR " + fileName);
                        uploadFile(fileName, dataPort, out, scanner);
                    }
                } 
                else {
                    System.out.println("지원하지 않는 명령입니다.");
                }
            }
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int enterPassiveMode(BufferedReader in, StringBuilder refIP) throws IOException {
        // PASV 명령 전송
        String response;
        response = in.readLine(); // 서버 응답을 읽어들임
        System.out.println(response);
        
        // 서버의 PASV 응답에서 IP와 포트 정보 추출
        String[] parts = response.split("[(),]");
        String ip = parts[1] + "." + parts[2] + "." + parts[3] + "." + parts[4];
        refIP.setLength(0); // StringBuilder 초기화
        refIP.append(ip); // 새로운 IP 설정
        int portHigh = Integer.parseInt(parts[5]);
        int portLow = Integer.parseInt(parts[6]);
        return portHigh * 256 + portLow; // 데이터 포트 반환
    }

    private static void downloadFile(String fileName, String ip,int dataPort, Socket dataSocket) {
        try (
            
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter("downloaded_" + fileName));
         InputStream dataIn = dataSocket.getInputStream()) {

        byte[] buffer = new byte[4096]; // 버퍼 크기 설정
        int bytesRead;

        // 파일 내용을 읽고 파일에 쓰기
        while ((bytesRead = dataIn.read(buffer)) != -1) {
            fileWriter.write(new String(buffer, 0, bytesRead)); // 바이트 배열을 문자열로 변환하여 쓰기
        }
        
            System.out.println("파일 다운로드 완료: " + fileName);
        } catch (IOException e) {
            System.out.println("파일 다운로드 실패: " + e.getMessage());
        }
    }

    private static void uploadFile(String fileName, int dataPort, PrintWriter out, Scanner scanner) {
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            try (Socket dataSocket = new Socket(SERVER_ADDRESS, dataPort);
                 BufferedReader fileReader = new BufferedReader(new FileReader(file));
                 PrintWriter dataOut = new PrintWriter(dataSocket.getOutputStream(), true)) {

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

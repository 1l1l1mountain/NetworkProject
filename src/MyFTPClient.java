import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class MyFTPClient {
    private static Socket controlSocket;
    private static BufferedReader reader;
    private static BufferedWriter writer;
    private static Socket dataSocket;

    public static void main(String[] args) {
        try {
            mainProc();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void mainProc() throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print("FTP 서버 주소 입력: ");
        String server = sc.next();
        controlSocket = new Socket(server, 21);

        reader = new BufferedReader(new InputStreamReader(controlSocket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(controlSocket.getOutputStream()));

        // Read initial connection response
        String connectResponse = reader.readLine();
        System.out.println("Server: " + connectResponse);

        // Login
        if (!doLogin(sc)) {
            System.out.println("로그인 실패");
            return;
        }

        boolean command = true;
        while (command) {
            showMenu();
            command = exeCommand(sc.next());
        }
        doQuit();
    }

    public static boolean doLogin(Scanner sc) throws IOException {
        System.out.print("사용자: ");
        String user = sc.next();
        System.out.print("암호: ");
        String pass = sc.next();

        // Send username
        sendCommand("USER " + user);
        String userResponse = reader.readLine();
        System.out.println("Server: " + userResponse);
        if (!userResponse.startsWith("3") && !userResponse.startsWith("2")) {
            return false;
        }

        // Send password
        sendCommand("PASS " + pass);
        String passResponse = reader.readLine();
        System.out.println("Server: " + passResponse);
        return passResponse.startsWith("2");
    }

    private static String readResponse() throws IOException {
    String response;
    ArrayList<String> responses = new ArrayList<>();
    
    // 모든 응답 라인을 읽음
    while ((response = reader.readLine()) != null) {
        responses.add(response);
        
        // 응답의 첫 글자가 1-5이고 그 다음이 공백이면 마지막 라인
        if (response.length() >= 4 && 
            Character.isDigit(response.charAt(0)) && 
            response.charAt(3) == ' ') {
            break;
        }
    }
    
    // 마지막 응답 반환
    return responses.get(responses.size() - 1);
    }

    public static void showMenu() {
        System.out.println("명령어에 해당하는 번호 입력>");
        System.out.print("  1 ls\n  2 cd\n  3 put\n  4 get\n  5 mkdir\n  6 rmdir\n  7 delete\n  8 quit\n");
    }

    public static boolean exeCommand(String command) throws IOException {
        switch (command) {
            case "1":
                doLs();
                break;
            case "2":
                doCd();
                break;
            case "3":
                doPut();
                break;
            case "4":
                doGet();
                break;
            case "5":
                doMkdir();
                break;
            case "6":
                doRmdir();
                break;
            case "7":
                doDelete();
                break;
            case "8":
                return false; // Exit loop
            default:
                System.out.println("잘못된 번호를 선택하셨습니다. 다시 골라주세요.");
        }
        return true;
    }

    private static void sendCommand(String command) throws IOException {
        writer.write(command + "\r\n");
        writer.flush();
        System.out.println("Command: " + command);
    }

    private static boolean isPositiveResponse() throws IOException {
        String response = reader.readLine();
        System.out.println("Response: " + response);
        return response.startsWith("2");
    }

    public static void doLs() throws IOException {
        enterPassiveMode();
        sendCommand("LIST");

        try (BufferedReader dataReader = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()))) {
            String line;
            while ((line = dataReader.readLine()) != null) {
                System.out.println(line);
            }
        }
        dataSocket.close();
        System.out.println(reader.readLine());
    }

    public static void doCd() throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print("어디로 이동하시겠습니까? ");
        String path = sc.nextLine();
        sendCommand("CWD " + path);
        System.out.println(reader.readLine());
    }

    public static void doPut() throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print("업로드할 로컬 파일 경로: ");
        String localPath = sc.nextLine();
        System.out.print("서버에 저장할 파일명: ");
        String remotePath = sc.nextLine();
    
        // Binary 모드로 설정
        sendCommand("TYPE I");
        System.out.println(reader.readLine());
    
        // Passive 모드 진입
        enterPassiveMode();
    
        // 파일 전송 시작
        sendCommand("STOR " + remotePath);
    
        try (OutputStream dataOut = dataSocket.getOutputStream();
             FileInputStream fileIn = new FileInputStream(localPath)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            long totalBytes = 0;
            long fileSize = new File(localPath).length();
            
            while ((bytesRead = fileIn.read(buffer)) != -1) {
                dataOut.write(buffer, 0, bytesRead);
                totalBytes += bytesRead;
                int progress = (int) ((totalBytes * 100) / fileSize);
                System.out.print("\r업로드 진행률: " + progress + "%");
            }
            System.out.println("\n업로드 완료!");
        }
        dataSocket.close();
        System.out.println("서버 응답: " + reader.readLine());
    }

    public static void doGet() throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print("다운로드할 파일명: ");
        String remotePath = sc.nextLine();
        System.out.print("저장할 로컬 경로: ");
        String localPath = sc.nextLine();
    
        try {
            // 이전 명령어의 남은 응답을 모두 읽어서 버퍼 비우기
            while (reader.ready()) {
                reader.readLine();
            }
    
            // Binary 모드로 설정
            sendCommand("TYPE I");
            String typeResponse = readResponse();
            System.out.println("Server: " + typeResponse);
            if (!typeResponse.startsWith("200")) {
                throw new IOException("Binary 모드 설정 실패: " + typeResponse);
            }
    
            // Passive 모드 진입
            sendCommand("PASV");
            String pasvResponse = readResponse();
            System.out.println("Entering Passive Mode: " + pasvResponse);
    
            if (!pasvResponse.startsWith("227")) {
                throw new IOException("PASV 명령 실패: " + pasvResponse);
            }
    
            // PASV 응답 파싱
            try {
                int startIndex = pasvResponse.indexOf('(');
                int endIndex = pasvResponse.indexOf(')');
                if (startIndex == -1 || endIndex == -1) {
                    throw new IOException("PASV 응답 형식 오류: " + pasvResponse);
                }
    
                String[] numbers = pasvResponse.substring(startIndex + 1, endIndex).split(",");
                if (numbers.length != 6) {
                    throw new IOException("PASV 응답의 숫자 개수가 잘못됨: " + pasvResponse);
                }
    
                String ip = numbers[0].trim() + "." + 
                           numbers[1].trim() + "." + 
                           numbers[2].trim() + "." + 
                           numbers[3].trim();
                int port = (Integer.parseInt(numbers[4].trim()) * 256) + 
                           Integer.parseInt(numbers[5].trim());
    
                System.out.println("데이터 연결: " + ip + ":" + port);
                dataSocket = new Socket(ip, port);
            } catch (Exception e) {
                throw new IOException("PASV 모드 설정 실패: " + e.getMessage());
            }
    
            // 파일 전송 시작
            sendCommand("RETR " + remotePath);
            String retrResponse = readResponse();
            System.out.println("Server: " + retrResponse);
            
            if (!retrResponse.startsWith("150")) {
                throw new IOException("파일 다운로드 시작 실패: " + retrResponse);
            }
    
            // 파일 다운로드
            try (InputStream dataIn = dataSocket.getInputStream();
                 FileOutputStream fileOut = new FileOutputStream(localPath)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                long totalBytes = 0;
                
                while ((bytesRead = dataIn.read(buffer)) != -1) {
                    fileOut.write(buffer, 0, bytesRead);
                    totalBytes += bytesRead;
                    System.out.print("\r다운로드된 크기: " + totalBytes + " bytes");
                }
                System.out.println("\n다운로드 완료!");
            } finally {
                if (dataSocket != null && !dataSocket.isClosed()) {
                    dataSocket.close();
                }
            }
            
            // 완료 응답 읽기
            String completionResponse = readResponse();
            System.out.println("Server: " + completionResponse);
            
        } catch (IOException e) {
            System.out.println("\n다운로드 중 오류 발생: " + e.getMessage());
            throw e;
        }
    }

    public static void doMkdir() throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print("생성할 디렉토리 이름 입력: ");
        String dirName = sc.nextLine();
        sendCommand("MKD " + dirName);
        System.out.println(reader.readLine());
    }

    public static void doRmdir() throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print("삭제할 디렉토리 이름 입력: ");
        String dirName = sc.nextLine();
        sendCommand("RMD " + dirName);
        System.out.println(reader.readLine());
    }

    public static void doDelete() throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print("삭제할 파일 이름 입력: ");
        String fileName = sc.nextLine();
        sendCommand("DELE " + fileName);
        System.out.println(reader.readLine());
    }

    public static void doQuit() throws IOException {
        sendCommand("QUIT");
        controlSocket.close();
        System.out.println("Disconnected.");
    }

    private static void enterPassiveMode() throws IOException {
        sendCommand("PASV");
        String response = reader.readLine();
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
    
            System.out.println("데이터 연결: " + ip + ":" + port);
            dataSocket = new Socket(ip, port);
        } catch (Exception e) {
            System.out.println("PASV 모드 진입 중 오류: " + e.getMessage());
            System.out.println("서버 응답: " + response);
            throw new IOException("PASV 모드 설정 실패", e);
        }
    }
}
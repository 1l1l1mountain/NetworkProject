import java.io.*;
import java.net.Socket;
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
        enterPassiveMode();
        sendCommand("STOR test.txt");

        try (OutputStream dataOut = dataSocket.getOutputStream();
             FileInputStream fileIn = new FileInputStream("test.txt")) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileIn.read(buffer)) != -1) {
                dataOut.write(buffer, 0, bytesRead);
            }
        }
        dataSocket.close();
        System.out.println(reader.readLine());
    }

    public static void doGet() throws IOException {
        enterPassiveMode();
        sendCommand("RETR test.txt");

        try (InputStream dataIn = dataSocket.getInputStream();
             FileOutputStream fileOut = new FileOutputStream("downloaded_test.txt")) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = dataIn.read(buffer)) != -1) {
                fileOut.write(buffer, 0, bytesRead);
            }
        }
        dataSocket.close();
        System.out.println(reader.readLine());
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

        String[] parts = response.split(",");
        String ip = String.join(".", parts[0].split("\\(")[1], parts[1], parts[2], parts[3]);
        int port = Integer.parseInt(parts[4]) * 256 + Integer.parseInt(parts[5].split("\\)")[0]);

        dataSocket = new Socket(ip, port);
    }
}
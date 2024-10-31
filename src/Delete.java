import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Delete {
    private NetStream netStream;
    private Scanner scanner;


    public void Delete() throws IOException {
      Scanner sc = new Scanner(System.in);
      System.out.print("삭제할 파일 이름 입력: ");
      String fileName = sc.nextLine();
      sendCommand("DELE " + fileName);
      System.out.println(readResponse());
    }

    private void sendCommand(String command) throws IOException {
        netStream.writer.write(command + "\r\n");
        netStream.writer.flush();
        System.out.println("Command: " + command);
    }

    private String readResponse() throws IOException {
        String response;
        ArrayList<String> responses = new ArrayList<>();
        
        // 모든 응답 라인을 읽음
        while ((response = netStream.reader.readLine()) != null) {
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
}

import java.io.IOException;

public class PASV {
    public static String[] DoPassiveMode() throws IOException {
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

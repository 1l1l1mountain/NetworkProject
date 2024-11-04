import java.io.IOException;


public class RmdirUI {

    public RmdirUI() {
        
    }

    // 입력된 텍스트를 반환하는 메서드
    public void Do(String input) {
       try{
        //RMD 호출
        NetStream.SendCommand("RMD " + input);
        String response = NetStream.ReceiveResponse();
        // 서버 응답 출력
        CommandUI.outputArea.append("서버 응답: " + response + "\n");
       }
       catch(IOException e){
        CommandUI.outputArea.append("오류 발생: " + e.getMessage() + "\n");
       }
    }
}

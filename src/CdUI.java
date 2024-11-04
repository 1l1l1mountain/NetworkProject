import java.io.IOException;
import javax.swing.*;

public class CdUI extends JFrame {
    
    
    public CdUI() {
    
    }
    public void Do(String input){
        
        try {               
            //CWD 요청
            NetStream.SendCommand("CWD " + input);
            //CWD 응답
            String response = NetStream.ReceiveResponse();
            // 서버 응답 출력
            CommandUI.outputArea.append("서버 응답: " + response + "\n");
        
        } catch (IOException ex) {
            CommandUI.outputArea.append("오류 발생: " + ex.getMessage() + "\n");
        }

    }
}

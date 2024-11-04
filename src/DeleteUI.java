import java.io.IOException;

public class DeleteUI {
  public DeleteUI(){

  }
    

    public void Do(String fileName) {
      
      try {
        NetStream.SendCommand("DELE " + fileName);

        String response = NetStream.ReceiveResponse();
            // 서버 응답 출력
          CommandUI.outputArea.append("서버 응답: " + response + "\n");
            
      } catch (IOException e) {
        CommandUI.outputArea.append("오류 발생: " + e.getMessage()+"\n");
      }  

    }

    
    }
package Server2;

import java.util.ArrayList;
import java.util.List;

public class Broadcast {
    public void broadcastJoin(String username, boolean includeMe) {
        // 모든 클라이언트를 임시 저장할 리스트
        List<ChatThread> chatThreads = new ArrayList<>();
        
        // 'this.list'에 저장된 ChatThread 객체들을 chatThreads에 복사
        for (int i = 0; i < this.list.size(); i++) {
            chatThreads.add(list.get(i));
        }
    
        try {
            String joinMessage = username + "님이 입장하였습니다."; // 입장 메시지 생성
            for (int i = 0; i < chatThreads.size(); i++) {
                ChatThread ct = chatThreads.get(i);
    
                // 만약 자신을 포함하지 말라고 하면 
                if (!includeMe) { // 나를 포함하고 있지 말아라.
                    if (ct == this) {
                        continue; // 본인을 제외
                    }
                }
                // 입장 메시지를 전송함
                ct.sendMessage(joinMessage);
            }
        } catch (Exception ex) {
            System.out.println("입장 메시지 전송 중 오류 발생: " + ex.getMessage());
        }
    }
    
    public void broadcastExit(String username) {
        // 모든 클라이언트를 임시 저장할 리스트
        List<ChatThread> chatThreads = new ArrayList<>();
        
        // 'this.list'에 저장된 ChatThread 객체들을 chatThreads에 복사
        for (int i = 0; i < this.list.size(); i++) {
            chatThreads.add(list.get(i));
        }
    
        try {
            String exitMessage = username + "님이 퇴장하였습니다."; // 퇴장 메시지 생성
            for (int i = 0; i < chatThreads.size(); i++) {
                ChatThread ct = chatThreads.get(i);
                // 퇴장 메시지를 전송함
                ct.sendMessage(exitMessage);
            }
        } catch (Exception ex) {
            System.out.println("퇴장 메시지 전송 중 오류 발생: " + ex.getMessage());
        }
    }
    
}

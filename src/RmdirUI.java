import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.*;

public class RmdirUI extends JFrame {
    private JTextField textField;
    private JLabel label;
    private JButton applyButton;

    public RmdirUI() {
        // 프레임 설정
        setTitle("생성할 디렉토리 이름 입력");
        setSize(300, 200);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());

        // 텍스트 필드 생성
        textField = new JTextField(20);
        add(textField); // 텍스트 필드를 바로 추가

        // 라벨 생성 및 추가 (초기값 설정)

        // 버튼 생성
        applyButton = new JButton("확인");
        add(applyButton); // 버튼 추가

        // 버튼 클릭 이벤트 핸들러 추가
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 텍스트 필드에서 텍스트를 가져와 라벨에 적용
                String dirName = getDirectoryName();
                try {
                    NetStream.SendCommand("RMD " + dirName);
                    JOptionPane.showMessageDialog(null, "서버 응답 : " + NetStream.ReceiveResponse());
                    dispose(); // 현재 프레임 닫기
                } catch (IOException e1) {
                    e1.printStackTrace();
                    label.setText("디렉토리 삭제 실패: " + e1.getMessage()); // 오류 메시지 표시
                }
            }
        });
    }

    // 입력된 텍스트를 반환하는 메서드
    public String getDirectoryName() {
        return textField.getText(); // 텍스트 필드에서 텍스트 반환
    }
    
}

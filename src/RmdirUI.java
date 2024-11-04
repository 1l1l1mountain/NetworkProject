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
        setTitle("삭제할 디렉토리 이름 입력");
        setSize(300, 200);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());

        // 텍스트 필드 생성
        textField = new JTextField(20);
        add(textField); 

        // 라벨 생성 및 추가
        label = new JLabel("");
        add(label); 

        // 버튼 생성
        applyButton = new JButton("확인");
        add(applyButton);

        // 버튼 클릭 이벤트 핸들러 추가
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dirName = getDirectoryName();
                try {
                    NetStream.SendCommand("RMD " + dirName);
                    JOptionPane.showMessageDialog(null, "서버 응답 : " + NetStream.ReceiveResponse());
                    // 현재 프레임 닫기
                    dispose();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    // 오류 메시지 표시
                    label.setText("디렉토리 삭제 실패: " + e1.getMessage());
                }
            }
        });
    }

    // 입력된 텍스트를 반환하는 메서드
    public String getDirectoryName() {
        return textField.getText();
    }
}

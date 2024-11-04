import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class CdUI extends JFrame {
    private JTextField textField;
    private JLabel label;
    private JButton applyButton;

    public CdUI() {
        // 프레임 설정
        setTitle("CD 명령");
        setSize(400, 200);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);  // 폼이 닫혀도 프로그램은 계속 실행
        setLayout(new FlowLayout());

        // 텍스트 필드 생성
        textField = new JTextField(20);

        // 라벨 생성 (초기 텍스트 비어 있음)
        label = new JLabel("완료 시 알려드립니다.");

        // 버튼 생성
        applyButton = new JButton("현재 디렉토리 이동");

        // 버튼 클릭 이벤트 핸들러 추가
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 텍스트 필드에서 텍스트를 가져와 라벨에 적용
                String inputText = textField.getText();
                try {
                    NetStream.SendCommand("CWD " + inputText);
                    String response = NetStream.ReceiveResponse();
                    label.setText("서버 응답 : " + response);
                } catch (Exception exx) {
                    exx.printStackTrace();
                }
            }
        });

        // 컴포넌트 추가
        add(textField);
        add(applyButton);
        add(label);
    }
}

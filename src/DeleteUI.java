import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class DeleteUI extends JFrame {
    public JTextField fileNameField;

    public DeleteUI() {
        setTitle("서버에 있는 파일 삭제");
        setSize(600, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());

        fileNameField = new JTextField(20);
        add(new JLabel("삭제할 파일 이름:"));
        add(fileNameField);

        JButton deleteButton = new JButton("삭제");
        // 버튼에 클릭 이벤트 핸들러 추가
        deleteButton.addActionListener(new DeleteButtonListener()); 
        add(deleteButton);
    }

    public class DeleteButtonListener implements ActionListener {
        @Override
        //버튼 클릭시 발생하는 이벤트
        public void actionPerformed(ActionEvent e) {
            try {
                // 텍스트 필드에서 삭제할 파일 이름 가져오기
                String fileName = fileNameField.getText().trim(); 
                // 파일 삭제 명령을 서버에 전송
                NetStream.SendCommand("DELE " + fileName);
                //서버 응답 출력
                System.out.println(NetStream.ReceiveResponse());
                dispose();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
}

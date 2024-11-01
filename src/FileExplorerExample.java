import java.io.File;
import javax.swing.*;

public class FileExplorerExample {

    public static void main(String[] args) {
        // JFrame 설정
        JFrame frame = new JFrame("File Explorer Example");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 파일 탐색기 버튼 추가
        JButton openButton = new JButton("파일 열기");
        
        // 버튼 함수
        openButton.addActionListener(e -> {
            // JFileChooser 인스턴스 생성
            JFileChooser fileChooser = new JFileChooser();
            
            // 파일 선택 대화창 열기
            int result = fileChooser.showOpenDialog(frame);
            
            // 파일 선택 여부 확인
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                System.out.println("선택된 파일: " + selectedFile.getAbsolutePath());
            } else {
                System.out.println("파일 선택이 취소되었습니다.");
            }
        });

        // 패널에 버튼 추가
        JPanel panel = new JPanel();
        panel.add(openButton);
        frame.add(panel);

        frame.setVisible(true);
    }
}

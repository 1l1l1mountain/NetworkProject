import javax.swing.*;
import java.io.File;

public class CdUI extends JFrame {
    private String directoryPath; // 선택한 디렉토리 경로를 저장할 변수

    public CdUI() {
        // 프레임 설정
        setTitle("디렉토리 선택기");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLayout(null);

        // 바로 디렉토리 선택기 호출
        directoryPath = selectDirectory();
        if (directoryPath != null) {
            System.out.println("선택한 디렉토리 경로: " + directoryPath);
        } else {
            System.out.println("디렉토리가 선택되지 않았습니다.");
        }

        // 프레임 닫기
        dispose(); // 선택 후 프레임 종료
    }

    // 디렉토리 선택기 메서드
    private String selectDirectory() {
        JFileChooser directoryChooser = new JFileChooser();
        directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // 디렉토리만 선택 가능
        int returnValue = directoryChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = directoryChooser.getSelectedFile();
            return selectedDirectory.getAbsolutePath(); // 선택한 디렉토리의 경로 반환
        }
        return null; // 디렉토리가 선택되지 않은 경우 null 반환
    }

    // 선택한 디렉토리 경로를 반환하는 메서드
    public String getDirectory() {
        return directoryPath;
    }
}

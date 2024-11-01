import javax.swing.*;
import java.awt.*;

public class asdftest extends JFrame {
    private JTextField directoryNameField; // 디렉토리 이름 입력 필드

    public asdftest() {
        // 프레임 설정
        setTitle("Mkdir"); // 제목 설정
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLayout(new FlowLayout());

        // 텍스트 박스 생성
        directoryNameField = new JTextField(20); // 20글자 크기의 텍스트 필드
        add(new JLabel("디렉토리 이름 입력:")); // 레이블 추가
        add(directoryNameField); // 텍스트 필드 추가

        // 프레임 보이게
        setVisible(true);
    }

    public static void main(String[] args) {
        new MkdirUI(); // MkdirUI 객체 생성
    }
}
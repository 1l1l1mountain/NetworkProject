import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class LsUI extends JFrame {
    private JTextArea outputArea;

    public LsUI() {
        // 프레임 설정
        setTitle("FTP 출력");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());

        // 텍스트 영역 생성
        outputArea = new JTextArea();
        outputArea.setEditable(false); // 수정 불가
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    // 출력 메서드
    public void printLine(String line) {
        outputArea.append(line + "\n"); // 텍스트 영역에 추가
    }
}
import java.io.*;

public class Main {
    public static void main(String[] args) {
        try {

            // 프로그램 실행
            mainProc();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void mainProc() throws IOException {
        //로그인 UI 실행
        LoginUI loginUI = new LoginUI();
        loginUI.Show();
    }
}

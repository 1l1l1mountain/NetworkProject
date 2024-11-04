import java.io.*;

public class Main {
    public static void main(String[] args) {
        try {
            mainProc();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void mainProc() throws IOException {
        //loginUI
        LoginUI loginUI = new LoginUI();
        loginUI.Show();
    }
}

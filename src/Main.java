import java.io.*;

public class Main {
    

    public static void main(String[] args) {
        try {

            //프로그램 실행
            mainProc();
        } catch (IOException e) {

            System.out.println("------------오류------------");                        
        }
    }

    public static void mainProc() throws IOException {
        
        //로그인 UI 
        LoginUI loginUI = new LoginUI();
        loginUI.Show();

    
        
    }

    
}
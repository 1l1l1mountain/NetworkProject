
import java.io.IOException;

public class Command {
    public static boolean LoopCommand(String command) throws IOException{
        
        Ls ls = new Ls();
        Put put = new Put();
        Get get = new Get();
        Otherfunc other = new Otherfunc();
        switch (command) {
            case "1":
                ls.Do();
                break;
            case "2"://
                other.Cd();
                break;
            case "3":
                put.Do();
                break;
            case "4":
                get.Do();
                break;
            case "5":
                other.Mkdir();
                break;
            case "6":
                other.Rmdir();
                break;
            case "7":
                other.Delete();
                break;
            case "8":
                return false; // Exit loop
            default:
                System.out.println("잘못된 번호를 선택하셨습니다. 다시 골라주세요.");
        }
        return true;

    }
}


import java.io.IOException;

public class Command {
    public static boolean LoopCommand(String command) throws IOException{
        
        Delete delete = new Delete();
        Mkdir mkdir = new Mkdir();
        Rmdir rmdir = new Rmdir();
        Ls ls = new Ls();
        Cd cd = new Cd();
        Put put = new Put();
        Get get = new Get();
        switch (command) {
            case "1":
                ls.Do();
                break;
            case "2":
                cd.Do();
                break;
            case "3":
                put.Do();
                break;
            case "4":
                get.Do();
                break;
            case "5":
                mkdir.Do();
                break;
            case "6":
                rmdir.Do();
                break;
            case "7":
                delete.Do();
                break;
            case "8":
                return false; // Exit loop
            default:
                System.out.println("잘못된 번호를 선택하셨습니다. 다시 골라주세요.");
        }
        return true;

    }
}

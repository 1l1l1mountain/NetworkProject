
import java.io.IOException;

public class Command {
    public static boolean LoopCommand(String command) throws IOException{
        switch (command) {
            case "1":
                Main.doLs();
                break;
            case "2":
                Main.doCd();
                break;
            case "3":
                Main.doPut();
                break;
            case "4":
                Main.doGet();
                break;
            case "5":
                Main.doMkdir();
                break;
            case "6":
                Main.doRmdir();
                break;
            case "7":
                Main.doDelete();
                break;
            case "8":
                return false; // Exit loop
            default:
                System.out.println("잘못된 번호를 선택하셨습니다. 다시 골라주세요.");
        }
        return true;

    }
}

package Server2;

public class JoinSys {
    public void Join(){

        Broadcast broadcast = new Broadcast();
        broadcast.broadcastJoin();
    }
    public static void main(String argv[]){

        //Chat serverChat = new Chat();
        //serverChat.print();
 
        JoinSys joinSys = new JoinSys();
        joinSys.Join();

}

}
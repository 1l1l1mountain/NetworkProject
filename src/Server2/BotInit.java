package Server2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class BotInit extends Thread {

    private String name;
    private BufferedReader br;
    private PrintWriter pw;
    private Socket socket;
    List<BotInit> list;

    public BotInit(Socket socket, List<BotInit> list)throws Exception{

        this.socket = socket;
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter pw =  new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.br = br;
        this.pw = pw;
        this.name = br.readLine();
        this.list = list;
        this.list.add(this);

    }

    public void sendMessage(String msg){
        pw.println(msg);
        pw.flush();
    }

    @Override
    public void run() {

        // ChatThread는 사용자가 보낸 메세지를 읽어들여서,
        // 접속된 모든 클라이언트에게 메세지를 보낸다.
        // 나를 제외한 모든 사용자에게 "00님이 연결되었습니다"..
        try{
            broadcast(name + "님이 연결되었습니다.", false);
            String line = null;
            while((line = br.readLine()) != null){
                if("/quit".equals(line)){
                    break;
                }

                if(line.charAt(0) == '/'){

                    String command = line.substring(1).trim();

                    if(command.equals("도움말")){
                        System.out.println("/투표 : 투표를 열 수 있습니다.");
                        System.out.println("/정보 : 현재 채팅방에 대한 정보들을 제공합니다.");
                    }
                    else if(command.equals("투표")){
                        
                    }
                    else if(command.equals("정보")){
                        System.out.println("현재 사용자 수:" + list.size() + "명");
                    }


                }

                broadcast(name + " : " + line,true);
            }
        }catch(Exception ex){
            // ChatThread가 연결이 끊어졌다는 것.
            ex.printStackTrace();
        }
        finally{
            broadcast(name + "님이 연결이 끊어졌습니다.", false);
            this.list.remove(this);
            try{
                br.close();
            }catch(Exception ex){
            }

            try{
                pw.close();
            }catch(Exception ex){
            }

            try{
                socket.close();
            }catch(Exception ex){
            }
        }
    }

    private void broadcast(String msg, boolean includeMe){
        List<BotInit> chatThreads = new ArrayList<>();
        for(int i=0;i<this.list.size();i++){
            chatThreads.add(list.get(i));
        }

        try{
            for(int i=0;i<chatThreads.size();i++){
                BotInit ct = chatThreads.get(i);
                if(!includeMe){ // 나를 포함하고 있지 말아라.
                    if(ct == this){
                        continue;
                    }
                }
                ct.sendMessage(msg);
            }
        }catch(Exception ex) {
            System.out.println("///");
        }
    }


}

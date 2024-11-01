import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class DeleteUI extends JFrame{
  public JTextField fileNameField;
  
  public DeleteUI(){
  setTitle("파일 삭제");
  setSize(600, 300);
  setDefaultCloseOperation(DISPOSE_ON_CLOSE);
  setLayout(new FlowLayout());

  
  fileNameField = new JTextField(20); 
  add(new JLabel("삭제할 파일 이름:")); 
  add(fileNameField); 

  JButton deleteButton = new JButton("삭제");
  deleteButton.addActionListener(new DeleteButtonListener()); // 클릭 이벤트 핸들러 추가
  add(deleteButton);
}


        

public class DeleteButtonListener implements ActionListener {
  NetStream netstream  = new NetStream();
  @Override
  public void actionPerformed(ActionEvent e) { 
    String fileName = fileNameField.getText().trim(); // 텍스트 필드에서 파일 이름 가져오기
    netstream.SendCommand("DELE " + fileName);
    System.out.println(netstream.ReceiveResponse());
  }
}

}
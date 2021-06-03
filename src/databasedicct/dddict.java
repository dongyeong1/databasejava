package databasedicct;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class dddict extends JPanel implements ActionListener{
   private JTextField inputField = new JTextField(30);
   private JButton searchBtn = new JButton("�˻�");
   private JButton addBtn = new JButton("�߰�");
   
   private Map<String, String> dict = new TreeMap<>();
   private static final String DICT_CLASS_NAME="org.mariadb.jdbc.Driver";
   private static final String DICT_FILE_NAME = "dict.props";
   private static final String DB_URL="jdbc:mariadb://localhost:3306/dong";
   //�� dong ���̺� �����ͺ��̽��� �����ϱ�
   private static final String DB_USER ="root";
   private static final String DB_PASSWORD ="ehddud2020";
   public dddict(){
	   //1. JDBC����̹��� �޸𸮿� �����ϱ�
	   //JDBC ����̹� Ŭ���� �̸��� DBMS���� �ٸ���.
	   
	   
	   
	   try {
		   Class.forName( DICT_CLASS_NAME);
		   buidDictionaryFromDB();
		   
	} catch (Exception e) {
		// TODO: handle exception
		System.out.println(e.getMessage());
	}
	   
	   
	   
      this.add(inputField);
      this.add(searchBtn);
      this.add(addBtn);
      this.setPreferredSize(new Dimension(600, 50));
      searchBtn.addActionListener(this);
      addBtn.addActionListener(this);
      
//      buildDictionaryFromFile();
      
      
   }
   private void buidDictionaryFromDB() {
	   //�����ͺ��̽� �����ϱ�
	 try(Connection con= DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD)) {
		String sql="select * from dict";
//		con.prepareStatement(DB_PASSWORD);
		//preparedstatement��ü�� �����غ� �Ϸ�� sql��ü
		 PreparedStatement pstmt = con.prepareStatement(sql);
		 //insert,delete,update���� ������ 
		 //excuteupdate()�޼��带 ȣ���ϰ�
		 //select ���� ������ executequery()�޼��带 ȣ���Ѵ�
		 
		ResultSet rs= pstmt.executeQuery();
		while(rs.next()) {
//			rs.getString(1);
			String key=rs.getString("kor");
			String value=rs.getString("eng");
			dict.put(key, value);
			System.out.println(key+" : "+value);
		}
		//�ϳ��ϳ� �����͸� �����´�
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   
   }
   
   private void buildDictionaryFromFile() {
      Properties props = new Properties();
      try (FileReader fReader = new FileReader(DICT_FILE_NAME)) {
         props.load(fReader);
      } catch (IOException e) {
         System.out.println(e.getMessage());
      }
      Set<Object> set = props.keySet();
      for(Object key : set) {
         Object value = props.get(key);
         dict.put((String)key, (String)value);
      }
   }
   
   private void addWordToDB(String key, String value) {
	   
	   /*
	    * ����̹��� �޸𸮿� �����Ѵ�.(�޸������ �ѹ��� �ϸ�ǰ� �����ڿ��� �ѻ���)
	    * 
	    * db�� �����ؼ� connetion��ü�� ��ȯ�޴´�
	    * connection��ü���� preparestatement��ü�� ��û�Ѵ�
	    * preparedstatement��ü�� excuteupdate()�޼��带
	    * ȣ���ؼ� db�� �����Ѵ�
	    */
	   try(Connection con= DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD)) {
		String sql ="insert into dict values(?,?)";
		/*
		 * �����غ�
		 * 1.�����˻�
		 * 2.���缺�˻�(���̺�,Į������ ������ �ִ���, �ִٸ� �̻���ڰ� ���ڵ带 ��)
		 * 3.���� ��ȹ�� �����()
		 */
		PreparedStatement pstmt=con.prepareStatement(sql);
		//���� �ְ����ϴ� Į��Ÿ�Կ� ���� �ٲ��
		//?�ڸ��� Į�� ������ ���Կ� ���� ������ setxxx()�޼��带 ȣ���ؾ��Ѵ�.
		//������� Į���� char�Ǵ� varcharŸ���̸� setString()
		//Į���� TimeStamp Ÿ���̸� setDate(), setTimestamp(),
		//Į���� int Ÿ���̸� setInt()...
 		pstmt.setString(1,key);
 		pstmt.setString(2, value);
 		pstmt.executeUpdate();
		//sql��ü�� ������
	} catch (Exception e) {
		// TODO: handle exception
		System.out.println(e.getMessage());
	}
	   
   }
   
   private void addWordToFile(String key, String value) {
      try(FileWriter fw = new FileWriter(DICT_FILE_NAME, true)){
         String str = key+"="+value+"\n";
         fw.write(str);//fw�� true �ɼ��� ���ָ�(default = false) ��������
      }catch(Exception e) {
         System.out.println(e.getMessage());
      }
   }
   
   
   public static void main(String[] args) {
      // TODO Auto-generated method stub
      JFrame frame = new JFrame();
      frame.add(new dddict());
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setResizable(false);
      frame.pack();
      frame.setTitle("���� �ѿ�����");
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
      
   }




   @Override
   public void actionPerformed(ActionEvent e) {
      // TODO Auto-generated method stub
      String key = inputField.getText();
      System.out.println("["+key+"]");
      if(key.trim().length()==0) return; //���ڿ� �յ��� ������ �ĳ�.
      if(e.getSource() == searchBtn) {
         String value = dict.get(key);
         if(value == null) {
            JOptionPane.showMessageDialog(this, "�ܾ �߰��ϼ���.", "404 Not Found", JOptionPane.ERROR_MESSAGE);
         }else {
            JOptionPane.showMessageDialog(this, value, key, JOptionPane.INFORMATION_MESSAGE);
         }
//      if(inputField.getText()==dict.)
         
      }else if(e.getSource() == addBtn) {
         String value = JOptionPane.showInputDialog(this, key + "�� �����Ǵ� ����ܾ �Է��ϼ���");
         dict.put(key, value);
         dict.put(value,key);
         
         if(value == null || value.trim().length() == 0)return;
//         addWordToFile(key, value);
         addWordToDB(key, value);
         JOptionPane.showMessageDialog(this, "����ܾ �߰��Ǿ����ϴ�.","����", JOptionPane.INFORMATION_MESSAGE);
      }
      inputField.requestFocus();
   }

}
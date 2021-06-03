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
   private JButton searchBtn = new JButton("검색");
   private JButton addBtn = new JButton("추가");
   
   private Map<String, String> dict = new TreeMap<>();
   private static final String DICT_CLASS_NAME="org.mariadb.jdbc.Driver";
   private static final String DICT_FILE_NAME = "dict.props";
   private static final String DB_URL="jdbc:mariadb://localhost:3306/dong";
   //이 dong 테이블 데이터베이스에 연결하기
   private static final String DB_USER ="root";
   private static final String DB_PASSWORD ="ehddud2020";
   public dddict(){
	   //1. JDBC드라이버를 메모리에 적재하기
	   //JDBC 드라이버 클래스 이름은 DBMS마다 다르다.
	   
	   
	   
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
	   //데이터베이스 연결하기
	 try(Connection con= DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD)) {
		String sql="select * from dict";
//		con.prepareStatement(DB_PASSWORD);
		//preparedstatement객체는 실행준비가 완료된 sql객체
		 PreparedStatement pstmt = con.prepareStatement(sql);
		 //insert,delete,update문의 실행은 
		 //excuteupdate()메서드를 호출하고
		 //select 문의 실행은 executequery()메서드를 호출한다
		 
		ResultSet rs= pstmt.executeQuery();
		while(rs.next()) {
//			rs.getString(1);
			String key=rs.getString("kor");
			String value=rs.getString("eng");
			dict.put(key, value);
			System.out.println(key+" : "+value);
		}
		//하나하나 데이터를 가져온다
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
	    * 드라이버를 메모리에 적재한다.(메모리적재는 한번만 하면되고 생성자에서 한상태)
	    * 
	    * db에 연결해서 connetion객체를 반환받는다
	    * connection객체에게 preparestatement객체를 요청한다
	    * preparedstatement객체의 excuteupdate()메서드를
	    * 호출해서 db에 저장한다
	    */
	   try(Connection con= DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD)) {
		String sql ="insert into dict values(?,?)";
		/*
		 * 실행준비
		 * 1.문법검사
		 * 2.정당성검사(테이블,칼럼등이 실제로 있는지, 있다면 이사용자가 레코드를 삽ㅇ)
		 * 3.실행 계획을 세운다()
		 */
		PreparedStatement pstmt=con.prepareStatement(sql);
		//내가 넣고자하는 칼럼타입에 따라 바뀐다
		//?자리의 칼럼 데이터 테입에 따라 적절한 setxxx()메서드를 호출해야한다.
		//예를들면 칼럼이 char또는 varchar타입이면 setString()
		//칼럼이 TimeStamp 타입이면 setDate(), setTimestamp(),
		//칼럼이 int 타입이면 setInt()...
 		pstmt.setString(1,key);
 		pstmt.setString(2, value);
 		pstmt.executeUpdate();
		//sql객체를 보낸다
	} catch (Exception e) {
		// TODO: handle exception
		System.out.println(e.getMessage());
	}
	   
   }
   
   private void addWordToFile(String key, String value) {
      try(FileWriter fw = new FileWriter(DICT_FILE_NAME, true)){
         String str = key+"="+value+"\n";
         fw.write(str);//fw에 true 옵션을 안주면(default = false) 덮어써버림
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
      frame.setTitle("나의 한영사전");
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
      
   }




   @Override
   public void actionPerformed(ActionEvent e) {
      // TODO Auto-generated method stub
      String key = inputField.getText();
      System.out.println("["+key+"]");
      if(key.trim().length()==0) return; //문자열 앞뒤의 공백을 쳐냄.
      if(e.getSource() == searchBtn) {
         String value = dict.get(key);
         if(value == null) {
            JOptionPane.showMessageDialog(this, "단어를 추가하세요.", "404 Not Found", JOptionPane.ERROR_MESSAGE);
         }else {
            JOptionPane.showMessageDialog(this, value, key, JOptionPane.INFORMATION_MESSAGE);
         }
//      if(inputField.getText()==dict.)
         
      }else if(e.getSource() == addBtn) {
         String value = JOptionPane.showInputDialog(this, key + "에 대응되는 영어단어를 입력하세요");
         dict.put(key, value);
         dict.put(value,key);
         
         if(value == null || value.trim().length() == 0)return;
//         addWordToFile(key, value);
         addWordToDB(key, value);
         JOptionPane.showMessageDialog(this, "영어단어가 추가되었습니다.","성공", JOptionPane.INFORMATION_MESSAGE);
      }
      inputField.requestFocus();
   }

}
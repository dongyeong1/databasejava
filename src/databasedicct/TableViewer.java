package databasedicct;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;




public class TableViewer extends JFrame implements ActionListener {

	
	private JTextField idField,titleField,publisherField,yearField,priceField;
	private JButton previousButton,nextButton,insertButton,finishButton;
	
	private static final String CLASSNAME="org.mariadb.jdbc.Driver";
	private static final String DB_URL="jdbc:mariadb://localhost:3306/oop1";
	 private static final String DB_USER ="root";
	   private static final String DB_PASSWORD ="ehddud2020";
	private ResultSet rs= null;
	Connection con=null;
	
	public TableViewer() {
		//각 컴포넌트를 frame에 추가하기
		
		
		this.setLayout(new GridLayout(0,2,5,5));
		this.add(new JLabel("id",JLabel.CENTER));
		idField=new JTextField(10);
		this.add(idField);
		
		this.add(new JLabel("title",JLabel.CENTER));
		titleField=new JTextField(10);
		this.add(titleField);
		
		this.add(new JLabel("publisher",JLabel.CENTER));
		publisherField=new JTextField(10);
		this.add(publisherField);
		
		this.add(new JLabel("year",JLabel.CENTER));
		yearField=new JTextField(10);
		this.add(yearField);
		
		this.add(new JLabel("price",JLabel.CENTER));
		priceField=new JTextField(10);
		this.add(priceField);
		
	
		previousButton=new JButton("previous");
		previousButton.addActionListener(this);
		this.add(previousButton);
		

		nextButton=new JButton("next");
		nextButton.addActionListener(this);
		this.add(nextButton);
		
		insertButton=new JButton("insert");
		insertButton.addActionListener(this);
		this.add(insertButton);
		
		finishButton=new JButton("finish");
		finishButton.addActionListener(this);
		this.add(finishButton);
		
		
//		this.setDefaultCloseOperation(EXIT_ON_CLOSE);//finish버튼을위해
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setSize(350,200);
		this.setVisible(true);
		
		
		
		//resultset을 이용해서 privaious를 사용한다
		
		
		
		/*
		 * tableviewer 객체가 생성할때 db의 books테이블의 레코드들을 읽어온다
		 * 1.데이터베이스와 연결
		 * 2.select문 실행하고 반환된 resultset객체를 가지고 있어야함
		 */
		
		try {
			Class.forName(CLASSNAME);
			con = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
			String sql= "select * from books";
			PreparedStatement pstmt =con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
			System.out.println("오류로 인한 데이터베이스종료");
			System.exit(0);
		}
		
		
	}
	
	
	
	
	
	
	
	
	public static void main(String[] args) {
		new TableViewer();
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			
			
			if(e.getSource()==nextButton) {
				rs.next();
				/*
				 * 현재 레코드의 칼럽값을 읽어와, jtextfield의 값으로 설정
				 * idfield,titlefield,publisherField,yearField,priceField
				 */
				
			}
			
			if(e.getSource()==previousButton) {
				rs.previous();
				
			}
			if(e.getSource()==nextButton||e.getSource()==previousButton) {
				int bookid=rs.getInt("book_id");
				idField.setText(String.valueOf(bookid));
				String title =rs.getString("title");
				titleField.setText(title);
				String publisher =rs.getString("publisher");
				publisherField.setText(publisher);
				Date year =rs.getDate("year");
				yearField.setText(year.toString());
				int price =rs.getInt("price");
				priceField.setText(String.valueOf(price));
			}else if(e.getSource()==insertButton) {
				//이미 db연결은 되어있고
				//이미 연결정보를 가지고 있는 connection객체를 insert문을  
				//이용해 prepare하고 반환된 preparedstatement 객체를 이용해서 
				//실행요청을 서버에 보낸다
				String sql="insert into books(title, publisher, YEAR, price) values(?,?,?,?)";
				//서버단에서 준비를 시켜라
				PreparedStatement psmtm=con.prepareStatement(sql);//sql을 서버로 보낸다
//				psmtm.executeUpdate();
				//실행할 sql문이 insert,delete,update-> executeUpdate호출
				//select 문인 경우 executeQuery호출
				
				
				//?자리에 값을 줘야함
				//?자리에 들어갈 값을 설정을 먼저한 후에 실행요청을 서버에 보내야한다
				//"?자리에 값을 설정할때 대응되는 칼럼이 데이터타입에 따라 적절한setxxx메서드들을 호출해야한다
				String title=titleField.getText();
				psmtm.setString(1, title);
				
				String publisher=publisherField.getText();
				psmtm.setString(2, publisher);
				
				String year=yearField.getText();
//				//문자열로부터 java.util.date객체를 생성할수있는 simpledateformat객체를 생성한다
//				//이때 date값을 포맷에 알려준다
//				SimpleDateFormat dt=new SimpleDateFormat("yyyy-mm-dd");
////				java.util.Date date=new Date(dt.df.parse(year).getTime());
//				
//				//문자열을 parsing해서 java.util.Date객체를 생성
//				java.util.Date date=dt.parse(year);//util date
//				
//				//java.util.Date객체로부터 java.sql.Date객체생성
//				Date sqlDate=new Date(date.getTime());//sql date
//				
//				
//				//java.sql.date 객체값을 3번째 ? 자리에 설정
//				psmtm.setDate(3, sqlDate);
//				
			   psmtm.setDate(3, Date.valueOf(year));
				
				String price=priceField.getText();
				psmtm.setInt(4, Integer.valueOf(price));
				
				psmtm.executeUpdate();
				reloading();
				JOptionPane.showMessageDialog(this, "등록성공!");
				
			}else if(e.getSource()==finishButton) {
				con.close();
				this.dispose();
				System.exit(0);
				
			}
			
			
			
		} catch (Exception e2) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(this, "오류발생: "+e2.getMessage());
			
			System.out.println(e2.getMessage());
		}
		
		
		
	}
	private void reloading() throws Exception{
		String sql= "select * from books order by book_id desc";
		PreparedStatement pstmt =con.prepareStatement(sql);
		rs=pstmt.executeQuery();
	}

}

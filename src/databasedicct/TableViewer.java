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
		//�� ������Ʈ�� frame�� �߰��ϱ�
		
		
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
		
		
//		this.setDefaultCloseOperation(EXIT_ON_CLOSE);//finish��ư������
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setSize(350,200);
		this.setVisible(true);
		
		
		
		//resultset�� �̿��ؼ� privaious�� ����Ѵ�
		
		
		
		/*
		 * tableviewer ��ü�� �����Ҷ� db�� books���̺��� ���ڵ���� �о�´�
		 * 1.�����ͺ��̽��� ����
		 * 2.select�� �����ϰ� ��ȯ�� resultset��ü�� ������ �־����
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
			System.out.println("������ ���� �����ͺ��̽�����");
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
				 * ���� ���ڵ��� Į������ �о��, jtextfield�� ������ ����
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
				//�̹� db������ �Ǿ��ְ�
				//�̹� ���������� ������ �ִ� connection��ü�� insert����  
				//�̿��� prepare�ϰ� ��ȯ�� preparedstatement ��ü�� �̿��ؼ� 
				//�����û�� ������ ������
				String sql="insert into books(title, publisher, YEAR, price) values(?,?,?,?)";
				//�����ܿ��� �غ� ���Ѷ�
				PreparedStatement psmtm=con.prepareStatement(sql);//sql�� ������ ������
//				psmtm.executeUpdate();
				//������ sql���� insert,delete,update-> executeUpdateȣ��
				//select ���� ��� executeQueryȣ��
				
				
				//?�ڸ��� ���� �����
				//?�ڸ��� �� ���� ������ ������ �Ŀ� �����û�� ������ �������Ѵ�
				//"?�ڸ��� ���� �����Ҷ� �����Ǵ� Į���� ������Ÿ�Կ� ���� ������setxxx�޼������ ȣ���ؾ��Ѵ�
				String title=titleField.getText();
				psmtm.setString(1, title);
				
				String publisher=publisherField.getText();
				psmtm.setString(2, publisher);
				
				String year=yearField.getText();
//				//���ڿ��κ��� java.util.date��ü�� �����Ҽ��ִ� simpledateformat��ü�� �����Ѵ�
//				//�̶� date���� ���˿� �˷��ش�
//				SimpleDateFormat dt=new SimpleDateFormat("yyyy-mm-dd");
////				java.util.Date date=new Date(dt.df.parse(year).getTime());
//				
//				//���ڿ��� parsing�ؼ� java.util.Date��ü�� ����
//				java.util.Date date=dt.parse(year);//util date
//				
//				//java.util.Date��ü�κ��� java.sql.Date��ü����
//				Date sqlDate=new Date(date.getTime());//sql date
//				
//				
//				//java.sql.date ��ü���� 3��° ? �ڸ��� ����
//				psmtm.setDate(3, sqlDate);
//				
			   psmtm.setDate(3, Date.valueOf(year));
				
				String price=priceField.getText();
				psmtm.setInt(4, Integer.valueOf(price));
				
				psmtm.executeUpdate();
				reloading();
				JOptionPane.showMessageDialog(this, "��ϼ���!");
				
			}else if(e.getSource()==finishButton) {
				con.close();
				this.dispose();
				System.exit(0);
				
			}
			
			
			
		} catch (Exception e2) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(this, "�����߻�: "+e2.getMessage());
			
			System.out.println(e2.getMessage());
		}
		
		
		
	}
	private void reloading() throws Exception{
		String sql= "select * from books order by book_id desc";
		PreparedStatement pstmt =con.prepareStatement(sql);
		rs=pstmt.executeQuery();
	}

}

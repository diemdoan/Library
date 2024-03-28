package LibraryManagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class Search {

	public JFrame SearchFrame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTable table;
	private DefaultTableModel model;
	private int userID;

	public static void main(String[] args) {
		Search window = new Search(0);
		window.SearchFrame.setVisible(true);

	}

	public Search(int id) {
		this.userID = id;

		SearchFrame = new JFrame("Tra cứu");
		SearchFrame.setBounds(100, 100, 838, 601);
		SearchFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SearchFrame.setResizable(false);
		SearchFrame.setLocationRelativeTo(null);
		SearchFrame.getContentPane().setLayout(null);
		SearchFrame.getContentPane().setBackground(new Color(255, 255, 204));

		JLabel lblNewLabel = new JLabel("TRA CỨU SÁCH");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel.setBounds(343, 0, 140, 37);
		SearchFrame.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Nhập thông tin sách cần tra cứu:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(30, 40, 240, 26);
		SearchFrame.getContentPane().add(lblNewLabel_1);

		JLabel lblNewLabel_3 = new JLabel();
		lblNewLabel_3.setBounds(711, 93, 77, 100);
		SearchFrame.getContentPane().add(lblNewLabel_3);
		String iconPath = "C:\\Users\\diemn\\Downloads\\searchBook.png";
		ImageIcon icon = new ImageIcon(iconPath);
		Image image = icon.getImage();
		int labelWidth = lblNewLabel_3.getWidth();
		int labelHeight = lblNewLabel_3.getHeight();
		Image scaledImage = image.getScaledInstance(labelWidth, labelHeight, Image.SCALE_SMOOTH);
		ImageIcon scaledIcon = new ImageIcon(scaledImage);
		lblNewLabel_3.setIcon(scaledIcon);

		JLabel lblNewLabel_2 = new JLabel("Tên sách:");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_2.setBounds(44, 76, 61, 26);
		SearchFrame.getContentPane().add(lblNewLabel_2);

		textField = new JTextField();
		textField.setBounds(162, 76, 248, 26);
		SearchFrame.getContentPane().add(textField);

		JButton btnNewButton = new JButton("Tra cứu theo tên ");
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnNewButton.setBounds(452, 77, 224, 25);
		SearchFrame.getContentPane().add(btnNewButton);

		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String tenSach = textField.getText();
				traCuuSachTheoTen(tenSach);
			}
		});

		JLabel lblNewLabel_2_1 = new JLabel("Thể loại:");
		lblNewLabel_2_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_2_1.setBounds(44, 112, 61, 26);
		SearchFrame.getContentPane().add(lblNewLabel_2_1);

		textField_1 = new JTextField();
		textField_1.setBounds(162, 114, 248, 26);
		SearchFrame.getContentPane().add(textField_1);

		JButton btnTraCuTheo = new JButton("Tra cứu theo thể loại");
		btnTraCuTheo.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnTraCuTheo.setBounds(452, 113, 224, 25);
		SearchFrame.getContentPane().add(btnTraCuTheo);

		btnTraCuTheo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String theLoai = textField_1.getText();
				traCuuSachTheoTheLoai(theLoai);
			}
		});

		JLabel lblNewLabel_2_2 = new JLabel("Tác giả:");
		lblNewLabel_2_2.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_2_2.setBounds(44, 148, 61, 26);
		SearchFrame.getContentPane().add(lblNewLabel_2_2);

		textField_2 = new JTextField();
		textField_2.setBounds(162, 150, 248, 26);
		SearchFrame.getContentPane().add(textField_2);

		JButton btnNewButton_1_1 = new JButton("Tra cứu theo tác giả");
		btnNewButton_1_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnNewButton_1_1.setBounds(452, 149, 224, 25);
		SearchFrame.getContentPane().add(btnNewButton_1_1);

		btnNewButton_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String tacGia = textField_2.getText();
				traCuuSachTheoTacGia(tacGia);
			}
		});

		JLabel lblNewLabel_2_2_1 = new JLabel("Năm xuất bản:");
		lblNewLabel_2_2_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_2_2_1.setBounds(44, 184, 97, 26);
		SearchFrame.getContentPane().add(lblNewLabel_2_2_1);

		textField_3 = new JTextField();
		textField_3.setBounds(162, 186, 248, 26);
		SearchFrame.getContentPane().add(textField_3);

		JButton btnNewButton_1_2 = new JButton("Tra cứu theo năm xuất bản");
		btnNewButton_1_2.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnNewButton_1_2.setBounds(452, 185, 224, 25);
		SearchFrame.getContentPane().add(btnNewButton_1_2);

		btnNewButton_1_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String namXuatBan = textField_3.getText();
				traCuuSachTheoNamXuatBan(namXuatBan);
			}
		});

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(30, 230, 770, 260);
		SearchFrame.getContentPane().add(scrollPane);
		table = new JTable();
		scrollPane.setViewportView(table);

		model = new DefaultTableModel();
		model.addColumn("Mã sách");
		model.addColumn("Tên sách");
		model.addColumn("Thể loại");
		model.addColumn("Tác giả");
		model.addColumn("Năm xuất bản");
		model.addColumn("Quốc gia");
		model.addColumn("Tình trạng");
		table.setModel(model);
		searchBooksTable();

		JButton rfr = new JButton("Refresh");
		rfr.setFont(new Font("Tahoma", Font.BOLD, 13));
		rfr.setBounds(689, 501, 89, 26);
		SearchFrame.getContentPane().add(rfr);

		rfr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchBooksTable();
			}
		});

		JButton btnNewButton_1_3 = new JButton("Menu");
		btnNewButton_1_3.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnNewButton_1_3.setBounds(356, 512, 91, 26);
		SearchFrame.getContentPane().add(btnNewButton_1_3);

		btnNewButton_1_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showMenu();
				SearchFrame.dispose();
			}
		});

	}

	private void showMenu() {
		MenuWindow menuWindow = new MenuWindow(userID);
		menuWindow.frame.setVisible(true);
	}

	public void searchBooksTable() {
		model.setRowCount(0);
		try {
			Connection conn = ConnectSQL.getConnection();
			Statement statement = conn.createStatement();

			String query = "SELECT * FROM books";
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				String bookID = resultSet.getString("ID");
				String title = resultSet.getString("Name");
				String category = resultSet.getString("Category");
				String author = resultSet.getString("Author");
				String year = resultSet.getString("Year");
				String country = resultSet.getString("Country");
				String status = resultSet.getString("Status");

				model.addRow(new Object[] { bookID, title, category, author, year, country, status });
			}

			statement.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void traCuuSachTheoTen(String tenSach) {
		if (tenSach.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Vui lòng nhập thông tin.", "Thông báo", JOptionPane.WARNING_MESSAGE);
			return;
		}

		try {
			model.setRowCount(0);
			Connection conn = ConnectSQL.getConnection();
			Statement statement = conn.createStatement();

			String query = "SELECT * FROM books WHERE Name LIKE '" + tenSach + "%'";
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				String bookID = resultSet.getString("ID");
				String title = resultSet.getString("Name");
				String category = resultSet.getString("Category");
				String author = resultSet.getString("Author");
				String year = resultSet.getString("Year");
				String country = resultSet.getString("Country");
				String status = resultSet.getString("Status");

				model.addRow(new Object[] { bookID, title, category, author, year, country, status });
			}

			statement.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void traCuuSachTheoTheLoai(String theLoai) {
		if (theLoai.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Vui lòng nhập thông tin.", "Thông báo", JOptionPane.WARNING_MESSAGE);
			return;
		}
		try {
			model.setRowCount(0);
			Connection conn = ConnectSQL.getConnection();
			Statement statement = conn.createStatement();

			String query = "SELECT * FROM books WHERE Category = '" + theLoai + "'";
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				String bookID = resultSet.getString("ID");
				String title = resultSet.getString("Name");
				String category = resultSet.getString("Category");
				String author = resultSet.getString("Author");
				String year = resultSet.getString("Year");
				String country = resultSet.getString("Country");
				String status = resultSet.getString("Status");

				model.addRow(new Object[] { bookID, title, category, author, year, country, status });
			}

			statement.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void traCuuSachTheoTacGia(String tacGia) {
		if (tacGia.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Vui lòng nhập thông tin.", "Thông báo", JOptionPane.WARNING_MESSAGE);
			return;
		}
		try {
			model.setRowCount(0);
			Connection conn = ConnectSQL.getConnection();
			Statement statement = conn.createStatement();

			String query = "SELECT * FROM books WHERE Author LIKE '" + tacGia + "%'";
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				String bookID = resultSet.getString("ID");
				String title = resultSet.getString("Name");
				String category = resultSet.getString("Category");
				String author = resultSet.getString("Author");
				String year = resultSet.getString("Year");
				String country = resultSet.getString("Country");
				String status = resultSet.getString("Status");

				model.addRow(new Object[] { bookID, title, category, author, year, country, status });
			}

			statement.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void traCuuSachTheoNamXuatBan(String namXuatBan) {
		if (namXuatBan.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Vui lòng nhập thông tin.", "Thông báo", JOptionPane.WARNING_MESSAGE);
			return;
		}
		try {
			model.setRowCount(0);
			Connection conn = ConnectSQL.getConnection();
			Statement statement = conn.createStatement();

			String query = "SELECT * FROM books WHERE Year = '" + namXuatBan + "'";
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				String bookID = resultSet.getString("ID");
				String title = resultSet.getString("Name");
				String category = resultSet.getString("Category");
				String author = resultSet.getString("Author");
				String year = resultSet.getString("Year");
				String country = resultSet.getString("Country");
				String status = resultSet.getString("Status");

				model.addRow(new Object[] { bookID, title, category, author, year, country, status });
			}

			statement.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

package LibraryManagement;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class UpdateBook {

	public JFrame upFrame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	protected JTable table;
	protected DefaultTableModel model;
	private int userID;

	public static void main(String[] args) {
		UpdateBook window = new UpdateBook(0);
		window.upFrame.setVisible(true);
	}

	public UpdateBook(int id) {
		this.userID = id;
		upFrame = new JFrame("Cập nhật");
		upFrame.setBounds(100, 100, 887, 447);
		upFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		upFrame.setResizable(false);
		upFrame.getContentPane().setLayout(null);
		upFrame.setLocationRelativeTo(null);
		upFrame.getContentPane().setBackground(new Color(250, 214, 250));

		JLabel lblNewLabel_4 = new JLabel();
		lblNewLabel_4.setBounds(226, 10, 73, 69);
		upFrame.getContentPane().add(lblNewLabel_4);
		String iconPath = "C:\\Users\\diemn\\Downloads\\update.png";
		ImageIcon icon = new ImageIcon(iconPath);
		Image image = icon.getImage();
		int labelWidth = lblNewLabel_4.getWidth();
		int labelHeight = lblNewLabel_4.getHeight();
		Image scaledImage = image.getScaledInstance(labelWidth, labelHeight, Image.SCALE_SMOOTH);
		ImageIcon scaledIcon = new ImageIcon(scaledImage);
		lblNewLabel_4.setIcon(scaledIcon);

		JLabel lblNewLabel = new JLabel("CẬP NHẬT SÁCH");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel.setBounds(362, 10, 230, 34);
		upFrame.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Nhập thông tin sách:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(29, 55, 167, 24);
		upFrame.getContentPane().add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("Tên sách:");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_2.setBounds(70, 89, 80, 24);
		upFrame.getContentPane().add(lblNewLabel_2);

		textField = new JTextField();
		textField.setBounds(180, 93, 213, 19);
		upFrame.getContentPane().add(textField);
		textField.setColumns(10);

		JLabel lblNewLabel_2_1 = new JLabel("Thể loại:");
		lblNewLabel_2_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_2_1.setBounds(70, 123, 80, 24);
		upFrame.getContentPane().add(lblNewLabel_2_1);

		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(180, 122, 213, 19);
		upFrame.getContentPane().add(textField_1);

		JLabel lblNewLabel_2_2 = new JLabel("Tác giả:");
		lblNewLabel_2_2.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_2_2.setBounds(70, 153, 80, 24);
		upFrame.getContentPane().add(lblNewLabel_2_2);

		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(180, 153, 213, 19);
		upFrame.getContentPane().add(textField_2);

		JLabel lblNewLabel_2_3 = new JLabel("Năm xuất bản:");
		lblNewLabel_2_3.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_2_3.setBounds(70, 187, 95, 24);
		upFrame.getContentPane().add(lblNewLabel_2_3);

		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(180, 187, 213, 19);
		upFrame.getContentPane().add(textField_3);

		JLabel lblNewLabel_2_3_1 = new JLabel("Quốc gia:");
		lblNewLabel_2_3_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_2_3_1.setBounds(70, 221, 80, 24);
		upFrame.getContentPane().add(lblNewLabel_2_3_1);

		textField_4 = new JTextField();
		textField_4.setColumns(10);
		textField_4.setBounds(180, 221, 213, 19);
		upFrame.getContentPane().add(textField_4);

		JLabel lblNewLabel_2_3_1_1 = new JLabel("ID sách:");
		lblNewLabel_2_3_1_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_2_3_1_1.setBounds(70, 255, 80, 24);
		upFrame.getContentPane().add(lblNewLabel_2_3_1_1);

		textField_5 = new JTextField();
		textField_5.setColumns(10);
		textField_5.setBounds(180, 259, 213, 19);
		upFrame.getContentPane().add(textField_5);

		JButton btnNewButton = new JButton("Cập nhật sách mới");
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 16));
		btnNewButton.setBounds(126, 299, 190, 24);
		upFrame.getContentPane().add(btnNewButton);

		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String tenSach = textField.getText();
				String theLoai = textField_1.getText();
				String tacGia = textField_2.getText();
				String namXBstr = textField_3.getText();
				String quocGia = textField_4.getText();
				String idSachstr = textField_5.getText();
				String status = "chưa mượn";

				if (tenSach.isEmpty() || theLoai.isEmpty() || tacGia.isEmpty() || namXBstr.isEmpty()
						|| quocGia.isEmpty() || idSachstr.isEmpty()) {
					JOptionPane.showMessageDialog(upFrame, "Vui lòng điền đầy đủ thông tin sách.");
				} else {
					try {
						int namXB = Integer.parseInt(namXBstr);
						int idSach = Integer.parseInt(idSachstr);

						if (namXB > 2023) {
							JOptionPane.showMessageDialog(upFrame, "Năm xuất bản không được lớn hơn 2023.");
						} else {
							Connection conn = ConnectSQL.getConnection();

							String checkSql = "SELECT * FROM books WHERE ID = ?";
							PreparedStatement checkStatement = conn.prepareStatement(checkSql);
							checkStatement.setInt(1, idSach);
							ResultSet resultSet = checkStatement.executeQuery();
							if (resultSet.next()) {
								JOptionPane.showMessageDialog(upFrame, "ID sách đã tồn tại.");
							} else {

								String sql = "INSERT INTO books (Name, Category, Author, Year, Country, ID, Status) VALUES (?, ?, ?, ?, ?, ?, ?)";
								PreparedStatement statement = conn.prepareStatement(sql);
								statement.setString(1, tenSach);
								statement.setString(2, theLoai);
								statement.setString(3, tacGia);
								statement.setInt(4, namXB);
								statement.setString(5, quocGia);
								statement.setInt(6, idSach);
								statement.setString(7, status);

								int rowsInserted = statement.executeUpdate();
								if (rowsInserted > 0) {
									JOptionPane.showMessageDialog(upFrame, "Thêm sách thành công.");
									Books();
								}

								statement.close();
							}

							checkStatement.close();
							conn.close();
						}
					} catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog(upFrame, "Vui lòng nhập số cho năm xuất bản và ID sách.");
					} catch (SQLException ex) {
						ex.printStackTrace();
					}
					Books();
				}
			}
		});

		JButton btnNewButton_1 = new JButton("Menu");
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnNewButton_1.setBounds(373, 376, 101, 24);
		upFrame.getContentPane().add(btnNewButton_1);

		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showMenu();
				upFrame.dispose();
			}
		});

		JLabel lblNewLabel_3 = new JLabel("Danh sách sách hiện có");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel_3.setBounds(577, 57, 159, 20);
		upFrame.getContentPane().add(lblNewLabel_3);

		JButton btnNewButton_edit = new JButton("Sửa thông tin");
		btnNewButton_edit.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnNewButton_edit.setBounds(494, 314, 130, 21);
		upFrame.getContentPane().add(btnNewButton_edit);

		btnNewButton_edit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRowIndex = table.getSelectedRow();
				if (selectedRowIndex != -1) {
					String choosedBook = (String) table.getValueAt(selectedRowIndex, 0);
					showEditForm(choosedBook);
					Books();

				} else {
					JOptionPane.showMessageDialog(upFrame, "Vui lòng chọn một sách để chỉnh sửa.");
				}

			}
		});

		JButton btnNewButton_2_1 = new JButton("Xóa sách");
		btnNewButton_2_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnNewButton_2_1.setBounds(676, 314, 130, 21);
		upFrame.getContentPane().add(btnNewButton_2_1);

		btnNewButton_2_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int selectedRowIndex = table.getSelectedRow();

				if (selectedRowIndex != -1) {
					String choosedBook = (String) table.getValueAt(selectedRowIndex, 0);

					int confirmation = JOptionPane.showConfirmDialog(upFrame, "Bạn có chắc muốn xóa sách này?",
							"Xác nhận xóa", JOptionPane.YES_NO_OPTION);

					if (confirmation == JOptionPane.YES_OPTION) {
						deleteBook(choosedBook);
						Books();
					}
				} else {
					JOptionPane.showMessageDialog(upFrame, "Vui lòng chọn một sách để xóa.");
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(400, 89, 460, 210);
		upFrame.getContentPane().add(scrollPane);
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

		Books();
	}

	private void showMenu() {
		MenuWindow menuWindow = new MenuWindow(userID);
		menuWindow.frame.setVisible(true);
	}

	private void showEditForm(String choosedBook) {
		EditForm editForm = new EditForm(choosedBook, userID);
		editForm.upFormFrame.setVisible(true);
		upFrame.dispose();
	}

	public void Books() {
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

	private void deleteBook(String choosedBook) {
		try {
			Connection conn = ConnectSQL.getConnection();
			String sql = "DELETE FROM books WHERE ID = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, choosedBook);

			int rowsDeleted = statement.executeUpdate();
			if (rowsDeleted > 0) {
				JOptionPane.showMessageDialog(upFrame, "Xóa sách thành công.");
			}
			statement.close();
			conn.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
}

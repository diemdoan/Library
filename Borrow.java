package LibraryManagement;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Borrow {
	public JFrame borFrame;
	private JTextField textFieldName;
	private int userID;
	private DefaultTableModel model;

	public static void main(String[] args) {

		Borrow window = new Borrow(0);
		window.borFrame.setVisible(true);

	}

	public Borrow(int id) {
		this.userID = id;

		borFrame = new JFrame("Mượn sách");
		borFrame.setBounds(100, 100, 558, 388);
		borFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		borFrame.setResizable(false);
		borFrame.getContentPane().setLayout(null);
		borFrame.setLocationRelativeTo(null);
		borFrame.getContentPane().setBackground(new Color(214, 224, 255));

		JLabel lblNewLabel = new JLabel("MƯỢN SÁCH");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel.setBounds(220, 10, 125, 31);
		borFrame.getContentPane().add(lblNewLabel);

		JTextField roleTextField = new JTextField();
		roleTextField.setFont(new Font("Tahoma", Font.ITALIC, 13));
		roleTextField.setBounds(460, 31, 73, 19);
		borFrame.getContentPane().add(roleTextField);
		roleTextField.setEditable(false);

		JTextField idTextField = new JTextField();
		idTextField.setFont(new Font("Tahoma", Font.ITALIC, 13));
		idTextField.setBounds(495, 53, 38, 19);
		borFrame.getContentPane().add(idTextField);
		idTextField.setEditable(false);

		try {
			Connection conn = ConnectSQL.getConnection();
			String sql = "SELECT * FROM accounts WHERE id = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, userID);
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				String role = resultSet.getString("Role");
				roleTextField.setText(role);
				String idstr = resultSet.getString("id");
				idTextField.setText(idstr);
				roleTextField.setHorizontalAlignment(SwingConstants.RIGHT);
			}
			resultSet.close();
			statement.close();
			conn.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		JLabel lblNewLabel_1 = new JLabel("Nhập Mã sách bạn muốn mượn:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(32, 81, 286, 24);
		borFrame.getContentPane().add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("Mã sách:");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_2.setBounds(64, 139, 80, 24);
		borFrame.getContentPane().add(lblNewLabel_2);

		textFieldName = new JTextField();
		textFieldName.setBounds(142, 139, 291, 31);
		borFrame.getContentPane().add(textFieldName);

		JLabel lblNewLabel_3 = new JLabel();
		lblNewLabel_3.setBounds(32, 201, 107, 109);
		borFrame.getContentPane().add(lblNewLabel_3);
		String iconPath = "C:\\Users\\diemn\\Downloads\\Borrow.png";
		ImageIcon icon = new ImageIcon(iconPath);
		Image image = icon.getImage();
		int labelWidth = lblNewLabel_3.getWidth();
		int labelHeight = lblNewLabel_3.getHeight();
		Image scaledImage = image.getScaledInstance(labelWidth, labelHeight, Image.SCALE_SMOOTH);
		ImageIcon scaledIcon = new ImageIcon(scaledImage);
		lblNewLabel_3.setIcon(scaledIcon);

		JButton btnNewButton = new JButton("Xác nhận mượn sách");
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 16));
		btnNewButton.setBounds(174, 181, 224, 31);
		borFrame.getContentPane().add(btnNewButton);

		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Connection conn = ConnectSQL.getConnection();

					int idCard = Integer.parseInt(idTextField.getText());
					PreparedStatement cardStatement = conn.prepareStatement("SELECT * FROM card WHERE idCard = ?");
					cardStatement.setInt(1, idCard);
					ResultSet cardResult = cardStatement.executeQuery();
					if (!cardResult.next()) {
						cardResult.close();
						cardStatement.close();
						conn.close();
						return;
					}
					cardResult.close();
					cardStatement.close();

					String bookIdText = textFieldName.getText();
					if (bookIdText.isEmpty()) {
						JOptionPane.showMessageDialog(null, "Vui lòng nhập ID sách cần mượn");
						conn.close();
						return;
					}

					int bookId = Integer.parseInt(textFieldName.getText());
					PreparedStatement checkBookStatement = conn.prepareStatement("SELECT * FROM books WHERE ID = ?");
					checkBookStatement.setInt(1, bookId);
					ResultSet checkBookResult = checkBookStatement.executeQuery();
					if (!checkBookResult.next()) {
						JOptionPane.showMessageDialog(null, "ID không hợp lệ");
						checkBookResult.close();
						checkBookStatement.close();
						conn.close();
						return;
					}
					checkBookResult.close();
					checkBookStatement.close();

					PreparedStatement bookStatement = conn.prepareStatement("SELECT status FROM books WHERE id = ?");
					bookStatement.setInt(1, bookId);
					ResultSet bookResult = bookStatement.executeQuery();
					if (bookResult.next()) {
						String status = bookResult.getString("status");
						if (status.equals("đã mượn")) {
							JOptionPane.showMessageDialog(null, "Sách đã được mượn");
							bookResult.close();
							bookStatement.close();
							conn.close();
							return;
						}
					}
					bookResult.close();
					bookStatement.close();

					PreparedStatement borrowStatement = conn
							.prepareStatement("SELECT Book1, Book2, Book3 FROM card WHERE idCard = ?");
					borrowStatement.setInt(1, idCard);
					ResultSet borrowResult = borrowStatement.executeQuery();
					borrowResult.next();
					int book1 = borrowResult.getInt("Book1");
					int book2 = borrowResult.getInt("Book2");
					int book3 = borrowResult.getInt("Book3");
					borrowResult.close();
					borrowStatement.close();

					if (book1 == 0) {
						PreparedStatement updateStatement = conn
								.prepareStatement("UPDATE card SET Book1 = ? WHERE idCard = ?");
						updateStatement.setInt(1, bookId);
						updateStatement.setInt(2, idCard);
						updateStatement.executeUpdate();
						updateStatement.close();
					} else if (book2 == 0) {
						PreparedStatement updateStatement = conn
								.prepareStatement("UPDATE card SET Book2 = ? WHERE idCard = ?");
						updateStatement.setInt(1, bookId);
						updateStatement.setInt(2, idCard);
						updateStatement.executeUpdate();
						updateStatement.close();
					} else if (book3 == 0) {
						PreparedStatement updateStatement = conn
								.prepareStatement("UPDATE card SET Book3 = ? WHERE idCard = ?");
						updateStatement.setInt(1, bookId);
						updateStatement.setInt(2, idCard);
						updateStatement.executeUpdate();
						updateStatement.close();
					} else {
						JOptionPane.showMessageDialog(null, "Chỉ được mượn 3 sách, vui lòng trả bớt sách để mượn thêm");
						conn.close();
						return;
					}

					PreparedStatement updateStatusStatement = conn
							.prepareStatement("UPDATE books SET status = ? WHERE ID = ?");
					updateStatusStatement.setString(1, "đã mượn");
					updateStatusStatement.setInt(2, bookId);
					updateStatusStatement.executeUpdate();
					updateStatusStatement.close();

					conn.close();
					JOptionPane.showMessageDialog(null, "Mượn sách thành công");
					ds(id);

				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		});

		JLabel dsLb = new JLabel("Danh sách sách đã mượn");
		dsLb.setFont(new Font("Tahoma", Font.BOLD, 13));
		dsLb.setBounds(189, 219, 189, 24);
		borFrame.getContentPane().add(dsLb);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(189, 242, 189, 43);
		borFrame.getContentPane().add(scrollPane);
		JTable table = new JTable();
		scrollPane.setViewportView(table);

		model = new DefaultTableModel();
		model.addColumn("Book1");
		model.addColumn("Book2");
		model.addColumn("Book3");
		scrollPane.setViewportView(table);
		table.setModel(model);

		ds(id);

		JButton btnNewButton_1 = new JButton("Menu");
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnNewButton_1.setBounds(230, 313, 87, 24);
		borFrame.getContentPane().add(btnNewButton_1);

		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showMenu();
				borFrame.dispose();
			}
		});

	}

	private void ds(int id) {
		try {
			model.setRowCount(0);
			Connection conn = ConnectSQL.getConnection();
			Statement statement = conn.createStatement();

			String query = "SELECT * FROM card WHERE idCard = '" + id + "'";
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				String b1 = resultSet.getString("Book1");
				String b2 = resultSet.getString("Book2");
				String b3 = resultSet.getString("Book3");

				model.addRow(new Object[] { b1, b2, b3 });
			}

			statement.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showMenu() {
		MenuWindow menuWindow = new MenuWindow(userID);
		menuWindow.frame.setVisible(true);
	}
}

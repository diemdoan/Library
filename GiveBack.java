package LibraryManagement;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GiveBack {
	public JFrame giveFrame;
	private JTextField textField;
	private int userID;
	private DefaultTableModel model;

	public static void main(String[] args) {

		GiveBack window = new GiveBack(0);
		window.giveFrame.setVisible(true);

	}

	public GiveBack(int id) {
		this.userID = id;

		giveFrame = new JFrame("Trả sách");
		giveFrame.setBounds(100, 100, 533, 346);
		giveFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		giveFrame.setResizable(false);
		giveFrame.setLocationRelativeTo(null);
		giveFrame.getContentPane().setLayout(null);
		giveFrame.getContentPane().setBackground(new Color(255, 204, 204));

		JLabel lblNewLabel = new JLabel("TRẢ SÁCH");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel.setBounds(220, 10, 125, 31);
		giveFrame.getContentPane().add(lblNewLabel);

		JTextField roleTextField = new JTextField();
		roleTextField.setFont(new Font("Tahoma", Font.ITALIC, 13));
		roleTextField.setBounds(440, 31, 73, 19);
		giveFrame.getContentPane().add(roleTextField);
		roleTextField.setEditable(false);

		JTextField idTextField = new JTextField();
		idTextField.setFont(new Font("Tahoma", Font.ITALIC, 13));
		idTextField.setBounds(475, 53, 38, 19);
		giveFrame.getContentPane().add(idTextField);
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

		JLabel lblNewLabel_1 = new JLabel("Nhập Mã sách cần trả:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(52, 65, 193, 31);
		giveFrame.getContentPane().add(lblNewLabel_1);

		textField = new JTextField();
		textField.setBounds(146, 106, 291, 31);
		giveFrame.getContentPane().add(textField);
		textField.setColumns(10);

		JLabel lblNewLabel_3 = new JLabel();
		lblNewLabel_3.setBounds(32, 169, 107, 109);
		giveFrame.getContentPane().add(lblNewLabel_3);
		String iconPath = "C:\\Users\\diemn\\Downloads\\giveback.png";
		ImageIcon icon = new ImageIcon(iconPath);
		Image image = icon.getImage();
		int labelWidth = lblNewLabel_3.getWidth();
		int labelHeight = lblNewLabel_3.getHeight();
		Image scaledImage = image.getScaledInstance(labelWidth, labelHeight, Image.SCALE_SMOOTH);
		ImageIcon scaledIcon = new ImageIcon(scaledImage);
		lblNewLabel_3.setIcon(scaledIcon);

		JButton btnNewButton = new JButton("Xác nhận trả sách");
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 16));
		btnNewButton.setBounds(174, 151, 224, 31);
		giveFrame.getContentPane().add(btnNewButton);
		

		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Connection conn = ConnectSQL.getConnection();
					int cardId = Integer.parseInt(idTextField.getText());

					String selectCardQuery = "SELECT * FROM card WHERE idCard = ?";
					PreparedStatement selectCardS = conn.prepareStatement(selectCardQuery);
					selectCardS.setInt(1, cardId);
					ResultSet cardResult = selectCardS.executeQuery();

					if (cardResult.next()) {

						boolean bookFound = false;
						for (int i = 1; i <= 3; i++) {
							String bookColumn = "Book" + i;
							String bookValue = cardResult.getString(bookColumn);

							if (bookValue != null && bookValue.equals(textField.getText())) {

								String clearBookQuery = "UPDATE card SET " + bookColumn + " = NULL WHERE idCard = ?";
								PreparedStatement clearBookStmt = conn.prepareStatement(clearBookQuery);
								clearBookStmt.setInt(1, cardId);
								clearBookStmt.executeUpdate();
								bookFound = true;

								String updateBookQuery = "UPDATE books SET status = 'chưa mượn' WHERE ID = ?";
								PreparedStatement updateBookStmt = conn.prepareStatement(updateBookQuery);
								updateBookStmt.setString(1, textField.getText());
								updateBookStmt.executeUpdate();
								JOptionPane.showMessageDialog(null, "Trả sách thành công.");
								ds(id);
								break;
							}
						}

						if (!bookFound) {
							JOptionPane.showMessageDialog(giveFrame, "Không có sách tương ứng để trả.");
						}

					}

					conn.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		});

		JLabel dsLb = new JLabel("Danh sách sách đã mượn");
		dsLb.setFont(new Font("Tahoma", Font.BOLD, 13));
		dsLb.setBounds(189, 189, 189, 24);
		giveFrame.getContentPane().add(dsLb);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(183, 217, 189, 43);
		giveFrame.getContentPane().add(scrollPane);
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
		btnNewButton_1.setBounds(233, 278, 87, 24);
		giveFrame.getContentPane().add(btnNewButton_1);
		
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showMenu();
				giveFrame.dispose();
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

package LibraryManagement;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.*;

public class LoginWindow {

	public JFrame logFrame;
	protected JTextField textField;
	private JPasswordField textField_pass;
	private int userID;

	public static void main(String[] args) {

		LoginWindow window = new LoginWindow();
		window.logFrame.setVisible(true);

	}

	public LoginWindow() {
		logFrame = new JFrame("Cửa Sổ Đăng Nhập");
		logFrame.setBounds(100, 100, 450, 300);
		logFrame.setLocationRelativeTo(null);
		logFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		logFrame.setResizable(false);
		logFrame.getContentPane().setLayout(null);
		ImageIcon backgroundImage = new ImageIcon("C:\\Users\\diemn\\Downloads\\lib_image.png");
		JLabel backgroundLabel = new JLabel(backgroundImage);
		backgroundLabel.setBounds(0, 0, logFrame.getWidth(), logFrame.getHeight());
		logFrame.setContentPane(backgroundLabel);

		JLabel lblNewLabel_1 = new JLabel(" THƯ VIỆN DIEM NAm ");
		lblNewLabel_1.setBounds(147, 21, 163, 23);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel_1.setBackground(Color.WHITE);
		lblNewLabel_1.setOpaque(true);
		logFrame.getContentPane().add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("Tên tài khoản:");
		lblNewLabel_2.setBounds(58, 63, 102, 16);
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel_2.setBackground(Color.WHITE);
		lblNewLabel_2.setOpaque(true);
		logFrame.getContentPane().add(lblNewLabel_2);

		textField = new JTextField();
		textField.setBounds(206, 62, 185, 19);
		logFrame.getContentPane().add(textField);

		JLabel lblNewLabel = new JLabel("Mật khẩu:");
		lblNewLabel.setBounds(58, 88, 70, 16);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel.setBackground(Color.WHITE);
		lblNewLabel.setOpaque(true);
		logFrame.getContentPane().add(lblNewLabel);

		textField_pass = new JPasswordField();
		textField_pass.setBounds(206, 87, 185, 19);
		logFrame.getContentPane().add(textField_pass);
		textField_pass.setEchoChar('*');

		JButton btnNewButton = new JButton("Đăng nhập");
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnNewButton.setBounds(97, 136, 110, 29);
		logFrame.getContentPane().add(btnNewButton);

		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = textField.getText();
				String password = new String(textField_pass.getPassword());

				if (username.isEmpty() || password.isEmpty()) {
					JOptionPane.showMessageDialog(logFrame, "Vui lòng điền đầy đủ thông tin!");
				} else {
					try {
						Connection conn = ConnectSQL.getConnection();

						String sql = "SELECT * FROM accounts WHERE AccName = ? AND Pass = ?";
						PreparedStatement statement = conn.prepareStatement(sql);
						statement.setString(1, username);
						statement.setString(2, password);

						ResultSet resultSet = statement.executeQuery();

						if (resultSet.next()) {
							userID = resultSet.getInt("id");
							showMenu();
							logFrame.dispose();
						} else {
							JOptionPane.showMessageDialog(logFrame, "Thông tin đăng nhập không chính xác!");
						}

						resultSet.close();
						statement.close();
						conn.close();
					} catch (SQLException ex) {
						ex.printStackTrace();
					}
				}
			}
		});

		JButton btnNewButton_Thoat = new JButton("Thoát");
		btnNewButton_Thoat.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnNewButton_Thoat.setBounds(240, 136, 110, 29);
		logFrame.getContentPane().add(btnNewButton_Thoat);
		btnNewButton_Thoat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logFrame.dispose();
			}
		});

		JLabel lblNewLabel_3 = new JLabel("Bạn chưa có tài khoản?");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 12));
		lblNewLabel_3.setBounds(59, 209, 143, 23);
		lblNewLabel_3.setBackground(Color.WHITE);
		lblNewLabel_3.setOpaque(true);
		logFrame.getContentPane().add(lblNewLabel_3);

		JButton btnNewButton_2 = new JButton("Đăng kí tài khoản mới");
		btnNewButton_2.setFont(new Font("Tahoma", Font.ITALIC, 11));
		btnNewButton_2.setBounds(227, 201, 146, 33);
		logFrame.getContentPane().add(btnNewButton_2);
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showCreateAccount();
			}
		});
	}

	private void showMenu() {
		MenuWindow menuWindow = new MenuWindow(userID);
		menuWindow.frame.setVisible(true);
	}

	private void showCreateAccount() {
		CreateAccount newAccWindow = new CreateAccount();
		newAccWindow.newAccFrame.setVisible(true);
	}

}

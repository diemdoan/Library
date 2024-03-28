package LibraryManagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CreateAccount {

	public JFrame newAccFrame;
	private JTextField textField;
	private JTextField textField_sdt;
	private JTextField textField_namsinh;
	private JPasswordField passwordField;
	private JTextField textField_tenTK;
	private JTextField textField_CD;

	public static void main(String[] args) {

		CreateAccount window = new CreateAccount();
		window.newAccFrame.setVisible(true);

	}

	public CreateAccount() {
		newAccFrame = new JFrame("Đăng kí tài khoản mới");
		newAccFrame.setBounds(100, 100, 499, 443);
		newAccFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		newAccFrame.setResizable(false);
		newAccFrame.setLocationRelativeTo(null);
		newAccFrame.getContentPane().setLayout(null);
		newAccFrame.getContentPane().setBackground(new Color(132, 235, 200));

		JLabel lblNewLabel = new JLabel("Nhập thông tin cá nhân");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel.setBounds(134, 0, 219, 43);
		newAccFrame.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1_1 = new JLabel("Họ và Tên:");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_1_1.setBounds(36, 48, 78, 26);
		newAccFrame.getContentPane().add(lblNewLabel_1_1);

		textField = new JTextField();
		textField.setBounds(149, 53, 280, 19);
		newAccFrame.getContentPane().add(textField);

		JLabel lblNewLabel_1 = new JLabel("Số điện thoại:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_1.setBounds(36, 84, 94, 26);
		newAccFrame.getContentPane().add(lblNewLabel_1);

		textField_sdt = new JTextField();
		textField_sdt.setColumns(10);
		textField_sdt.setBounds(149, 89, 280, 19);
		newAccFrame.getContentPane().add(textField_sdt);

		JLabel lblNewLabel_1_2 = new JLabel("Năm sinh:");
		lblNewLabel_1_2.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_1_2.setBounds(36, 120, 94, 26);
		newAccFrame.getContentPane().add(lblNewLabel_1_2);

		textField_namsinh = new JTextField();
		textField_namsinh.setBounds(148, 125, 96, 19);
		newAccFrame.getContentPane().add(textField_namsinh);

		JLabel lblNewLabel_1_2_1 = new JLabel("Giới tính:");
		lblNewLabel_1_2_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_1_2_1.setBounds(264, 120, 53, 26);
		newAccFrame.getContentPane().add(lblNewLabel_1_2_1);

		ButtonGroup gioitinh = new ButtonGroup();

		JCheckBox chckbxNewCheckBox_Nam = new JCheckBox("Nam");
		chckbxNewCheckBox_Nam.setBounds(331, 124, 53, 21);
		newAccFrame.getContentPane().add(chckbxNewCheckBox_Nam);
		gioitinh.add(chckbxNewCheckBox_Nam);

		JCheckBox chckbxNewCheckBox_Nu = new JCheckBox("Nữ");
		chckbxNewCheckBox_Nu.setBounds(386, 124, 43, 21);
		newAccFrame.getContentPane().add(chckbxNewCheckBox_Nu);
		gioitinh.add(chckbxNewCheckBox_Nu);

		JLabel lblNewLabel_1_2_2 = new JLabel("Số CCCD:");
		lblNewLabel_1_2_2.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_1_2_2.setBounds(36, 156, 102, 26);
		newAccFrame.getContentPane().add(lblNewLabel_1_2_2);

		textField_CD = new JTextField();
		textField_CD.setBounds(148, 161, 281, 19);
		newAccFrame.getContentPane().add(textField_CD);

		JLabel lblNewLabel_2 = new JLabel();
		lblNewLabel_2.setBounds(30, 185, 72, 68);
		newAccFrame.getContentPane().add(lblNewLabel_2);
		String iconPath = "C:\\Users\\diemn\\Downloads\\create-account.png";
		ImageIcon icon = new ImageIcon(iconPath);
		Image image = icon.getImage();
        int labelWidth = lblNewLabel_2.getWidth();
        int labelHeight = lblNewLabel_2.getHeight();
        Image scaledImage = image.getScaledInstance(labelWidth, labelHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        lblNewLabel_2.setIcon(scaledIcon);

		JLabel lblNhpThngTin = new JLabel("Nhập thông tin tài khoản");
		lblNhpThngTin.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNhpThngTin.setBounds(134, 203, 234, 43);
		newAccFrame.getContentPane().add(lblNhpThngTin);

		JLabel lblNewLabel_1_1_1 = new JLabel("Tên tài khoản:");
		lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_1_1_1.setBounds(36, 253, 94, 26);
		newAccFrame.getContentPane().add(lblNewLabel_1_1_1);

		textField_tenTK = new JTextField();
		textField_tenTK.setBounds(149, 258, 280, 19);
		newAccFrame.getContentPane().add(textField_tenTK);

		JLabel lblNewLabel_1_1_2 = new JLabel("Mật khẩu:");
		lblNewLabel_1_1_2.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_1_1_2.setBounds(36, 289, 78, 26);
		newAccFrame.getContentPane().add(lblNewLabel_1_1_2);

		passwordField = new JPasswordField();
		passwordField.setBounds(149, 294, 280, 19);
		newAccFrame.getContentPane().add(passwordField);
		passwordField.setEchoChar('*');

		JButton btnNewButton = new JButton("Đăng kí");
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnNewButton.setBounds(111, 344, 96, 29);
		newAccFrame.getContentPane().add(btnNewButton);

		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fullName = textField.getText();
				String phone = textField_sdt.getText();
				String birthYears = textField_namsinh.getText();
				String sex = "";
				if (chckbxNewCheckBox_Nam.isSelected()) {
					sex = "Nam";
				} else if (chckbxNewCheckBox_Nu.isSelected()) {
					sex = "Nữ";
				}
				String cccd = textField_CD.getText();
				String accName = textField_tenTK.getText();
				String pass = new String(passwordField.getPassword());

				if (fullName.isEmpty() || phone.isEmpty() || birthYears.isEmpty() || sex.isEmpty() || cccd.isEmpty()
						|| accName.isEmpty() || pass.isEmpty()) {
					JOptionPane.showMessageDialog(newAccFrame, "Vui lòng điền đầy đủ thông tin!");
					return;
				}

				try {
					int birthYear = Integer.parseInt(birthYears);
					if (birthYear >= 2023) {
						JOptionPane.showMessageDialog(newAccFrame, "Năm sinh phải bé hơn 2023!");
						return;
					}
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(newAccFrame, "Năm sinh phải là số!");
					return;
				}

				try {
					Long.parseLong(cccd);
					if (cccd.length() != 12) {
						JOptionPane.showMessageDialog(newAccFrame, "CCCD phải có 12 chữ số!");
						return;
					}
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(newAccFrame, "CCCD phải là số!");
					return;
				}
				try {
					Long.parseLong(phone);
					if (phone.length() != 10) {
						JOptionPane.showMessageDialog(newAccFrame, "Số điện thoại phải có 10 chữ số!");
						return;
					}
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(newAccFrame, "Số điện thoại phải là số!");
					return;
				}
				try {
				    Connection conn = ConnectSQL.getConnection();
				    String checkDuplicateSql = "SELECT AccName FROM accounts WHERE AccName = ?";
				    PreparedStatement checkStatement = conn.prepareStatement(checkDuplicateSql);
				    checkStatement.setString(1, accName);
				    ResultSet resultSet = checkStatement.executeQuery();
				    if (resultSet.next()) {
				        JOptionPane.showMessageDialog(newAccFrame, "Tên tài khoản đã được sử dụng!");
				        checkStatement.close();
				        conn.close();
				        return;
				    }
				    checkStatement.close();
				    conn.close();
				} catch (SQLException ex) {
				    ex.printStackTrace();
				    return;
				}

				try {
					Connection conn = ConnectSQL.getConnection();
					String sql = "INSERT INTO accounts (FullName, CCCD, BirthYear, Phone, Sex, AccName, Pass, Role) VALUES (?, ?, ?, ?, ?, ?, ?, 'độc giả')";
					PreparedStatement statement = conn.prepareStatement(sql);
					statement.setString(1, fullName);
					statement.setString(2, cccd);
					statement.setString(3, birthYears);
					statement.setString(4, phone);
					statement.setString(5, sex);
					statement.setString(6, accName);
					statement.setString(7, pass);
					statement.executeUpdate();

					ResultSet generatedKeys = statement.executeQuery("SELECT LAST_INSERT_ID()");
					if (generatedKeys.next()) {
						int accountId = generatedKeys.getInt(1);
						String cardSql = "INSERT INTO card (idCard) VALUES (?)";
						PreparedStatement cardStatement = conn.prepareStatement(cardSql);
						cardStatement.setInt(1, accountId);
						cardStatement.executeUpdate();
						cardStatement.close();

					}

					statement.close();
					conn.close();
					JOptionPane.showMessageDialog(newAccFrame, "Đăng ký thành công!");
					newAccFrame.dispose();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		});

		JButton btnThoat = new JButton("Thoát");
		btnThoat.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnThoat.setBounds(257, 344, 96, 29);
		newAccFrame.getContentPane().add(btnThoat);

		btnThoat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newAccFrame.dispose();
			}
		});
	}

}

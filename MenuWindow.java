package LibraryManagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class MenuWindow {

	public JFrame frame;
	private int id;

	public static void main(String[] args) {
		MenuWindow menuWindow = new MenuWindow(0);
		menuWindow.frame.setVisible(true);
	}

	public MenuWindow(int userID) {

		frame = new JFrame("Menu");
		frame.setBounds(100, 100, 588, 400);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		ImageIcon backgroundImage = new ImageIcon("C:\\Users\\diemn\\Downloads\\lib_image.png");
		JLabel backgroundLabel = new JLabel(backgroundImage);
		backgroundLabel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
		frame.setContentPane(backgroundLabel);

		JLabel lblNewLabel = new JLabel("THƯ VIỆN DIEM");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel.setBounds(220, 10, 150, 30);
		frame.getContentPane().add(lblNewLabel);
		lblNewLabel.setBackground(Color.WHITE);
		lblNewLabel.setOpaque(true);

		JTextField roleTextField = new JTextField();
		roleTextField.setFont(new Font("Tahoma", Font.ITALIC, 13));
		roleTextField.setBounds(39, 31, 96, 19);
		frame.getContentPane().add(roleTextField);
		roleTextField.setEditable(false);

		JTextField idTextField = new JTextField();
		idTextField.setFont(new Font("Tahoma", Font.ITALIC, 13));
		idTextField.setBounds(39, 53, 38, 19);
		frame.getContentPane().add(idTextField);
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
				id = resultSet.getInt("id");
			}
			resultSet.close();
			statement.close();
			conn.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		JButton btnSearch = new JButton("Tra cứu sách");
		btnSearch.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnSearch.setBounds(105, 68, 153, 66);
		frame.getContentPane().add(btnSearch);
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				showSearch();
			}
		});

		JButton btnBor = new JButton("Mượn sách");
		btnBor.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnBor.setBounds(105, 171, 153, 66);
		frame.getContentPane().add(btnBor);
		btnBor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				showBor();
			}
		});

		JButton btnGive = new JButton("Trả sách");
		btnGive.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnGive.setBounds(323, 171, 153, 66);
		frame.getContentPane().add(btnGive);
		btnGive.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				showGiv();
			}
		});

		JButton btnUp = new JButton("Cập nhật sách");
		btnUp.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnUp.setBounds(323, 68, 153, 66);
		frame.getContentPane().add(btnUp);

		btnUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String role = roleTextField.getText();
				if (role.equals("thủ thư")) {
					frame.dispose();
					showUpdate();
				} else {
					JOptionPane.showMessageDialog(null, "Chỉ thủ thư mới có quyền sử dụng chức năng này");
				}
			}
		});

		JButton logOut = new JButton("Đăng xuất");
		logOut.setFont(new Font("Tahoma", Font.BOLD, 13));
		logOut.setBounds(39, 298, 102, 27);
		frame.getContentPane().add(logOut);

		logOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showLogin();
				frame.dispose();
			}
		});

		JButton btnNewButton_1 = new JButton("Thoát");
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnNewButton_1.setBounds(456, 298, 94, 27);
		frame.getContentPane().add(btnNewButton_1);

		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});

	}

	private void showSearch() {
		Search searchWindow = new Search(id);
		searchWindow.SearchFrame.setVisible(true);
	}

	private void showUpdate() {
		UpdateBook updateWindow = new UpdateBook(id);
		updateWindow.upFrame.setVisible(true);
	}

	private void showBor() {
		Borrow borWindow = new Borrow(id);
		borWindow.borFrame.setVisible(true);
	}

	private void showGiv() {
		GiveBack giveWindow = new GiveBack(id);
		giveWindow.giveFrame.setVisible(true);
	}

	private void showLogin() {
		LoginWindow loginWindow = new LoginWindow();
		loginWindow.logFrame.setVisible(true);
	}

}

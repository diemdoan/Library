package LibraryManagement;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;

public class EditForm implements ActionListener {

	JFrame upFormFrame;
	JTextField txtMaSach, txtTenSach, txtTheLoai, txtTacGia, txtNamXuatBan, txtQuocGia, txtTinhTrang;
	private int userID;

	public static void main(String[] args) {

		EditForm editForm = new EditForm(null, 0);
		editForm.upFormFrame.setVisible(true);

	}

	public EditForm(String choosedBook, int id) {

		this.userID = id;
		upFormFrame = new JFrame();
		upFormFrame.setTitle("Cập nhật thông tin sách");
		upFormFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		upFormFrame.setResizable(false);
		upFormFrame.setSize(400, 300);
		upFormFrame.setLayout(new GridLayout(8, 2));
		upFormFrame.setLocationRelativeTo(null);
		upFormFrame.getContentPane().setBackground(new Color(250, 214, 250));

		JLabel lblMaSach = new JLabel("Mã sách (không được sửa):");
		txtMaSach = new JTextField();
		txtMaSach.setEditable(false);
		JLabel lblTenSach = new JLabel("Tên sách:");
		txtTenSach = new JTextField();
		JLabel lblTheLoai = new JLabel("Thể loại:");
		txtTheLoai = new JTextField();
		JLabel lblTacGia = new JLabel("Tác giả:");
		txtTacGia = new JTextField();
		JLabel lblNamXuatBan = new JLabel("Năm xuất bản:");
		txtNamXuatBan = new JTextField();
		JLabel lblQuocGia = new JLabel("Quốc gia:");
		txtQuocGia = new JTextField();
		JLabel lblTinhTrang = new JLabel("Tình trạng:");
		txtTinhTrang = new JTextField();

		JButton btnSave = new JButton("Lưu");
		btnSave.addActionListener(this);
		JButton btnExit = new JButton("Thoát");
		btnExit.addActionListener(this);

		upFormFrame.add(lblMaSach);
		upFormFrame.add(txtMaSach);
		upFormFrame.add(lblTenSach);
		upFormFrame.add(txtTenSach);
		upFormFrame.add(lblTheLoai);
		upFormFrame.add(txtTheLoai);
		upFormFrame.add(lblTacGia);
		upFormFrame.add(txtTacGia);
		upFormFrame.add(lblNamXuatBan);
		upFormFrame.add(txtNamXuatBan);
		upFormFrame.add(lblQuocGia);
		upFormFrame.add(txtQuocGia);
		upFormFrame.add(lblTinhTrang);
		upFormFrame.add(txtTinhTrang);
		upFormFrame.add(btnSave);
		upFormFrame.add(btnExit);

		try {
			Connection conn = ConnectSQL.getConnection();
			String sql = "SELECT * FROM books WHERE ID = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, choosedBook);
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				String bookid = resultSet.getString("ID");
				String title = resultSet.getString("Name");
				String category = resultSet.getString("Category");
				String author = resultSet.getString("Author");
				String year = resultSet.getString("Year");
				String country = resultSet.getString("Country");
				String status = resultSet.getString("Status");

				txtMaSach.setText(bookid);
				txtTenSach.setText(title);
				txtTheLoai.setText(category);
				txtTacGia.setText(author);
				txtNamXuatBan.setText(year);
				txtQuocGia.setText(country);
				txtTinhTrang.setText(status);
			}

			statement.close();
			conn.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Lưu")) {
			int confirmation = JOptionPane.showConfirmDialog(upFormFrame, "Bạn có chắc muốn lưu thông tin này?",
					"Xác nhận", JOptionPane.YES_NO_OPTION);
			if (confirmation == JOptionPane.YES_OPTION) {
				String bookID = txtMaSach.getText();
				String title = txtTenSach.getText();
				String category = txtTheLoai.getText();
				String author = txtTacGia.getText();
				String year = txtNamXuatBan.getText();
				String country = txtQuocGia.getText();
				String status = txtTinhTrang.getText();

				if (!status.equals("chưa mượn") && !status.equals("đã mượn")) {
					JOptionPane.showMessageDialog(null, "Vui lòng nhập 'chưa mượn' hoặc 'đã mượn' ở ô Tình trạng.");
					return;
				} else if (title.isEmpty() || category.isEmpty() || author.isEmpty() || year.isEmpty()
						|| country.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin!");
					return;
				} else if (!isNumeric(year)) {
					JOptionPane.showMessageDialog(null, "Vui lòng nhập số cho năm!");
					return;
				} else if (status.equals("chưa mượn")) {
					upCard(bookID);
				}
				saveEdit(bookID, title, category, author, year, country, status);
				JOptionPane.showMessageDialog(null, "Thông tin đã được lưu.");

			}
			showUpdate();
			upFormFrame.dispose();
		} else if (e.getActionCommand().equals("Thoát")) {
			showUpdate();
			upFormFrame.dispose();
		}

	}

	private boolean isNumeric(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private void saveEdit(String bookID, String title, String category, String author, String year, String country,
			String status) {

		try {
			Connection conn = ConnectSQL.getConnection();
			String sql = "UPDATE books SET Name = ?, Category = ?, Author = ?, Year = ?, Country = ?, Status = ? WHERE ID = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, title);
			statement.setString(2, category);
			statement.setString(3, author);
			statement.setString(4, year);
			statement.setString(5, country);
			statement.setString(6, status);
			statement.setString(7, bookID);

			statement.executeUpdate();

			statement.close();
			conn.close();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	private void upCard(String bookID) {
		try {
			Connection conn = ConnectSQL.getConnection();
			String sql = "SELECT Book1, Book2, Book3 FROM card";
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				String book1 = resultSet.getString("Book1");
				String book2 = resultSet.getString("Book2");
				String book3 = resultSet.getString("Book3");

				if (bookID.equals(book1)) {
					updateColumnToNull(conn, "Book1", bookID);
				} else if (bookID.equals(book2)) {
					updateColumnToNull(conn, "Book2", bookID);
				} else if (bookID.equals(book3)) {
					updateColumnToNull(conn, "Book3", bookID);
				}
			}

			resultSet.close();
			statement.close();
			conn.close();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	private void updateColumnToNull(Connection conn, String columnName, String bookID) throws SQLException {
		String updateSql = "UPDATE card SET " + columnName + " = NULL WHERE Book1 = ? OR Book2 = ? OR Book3 = ?";
		PreparedStatement updateStatement = conn.prepareStatement(updateSql);
		updateStatement.setString(1, bookID);
		updateStatement.setString(2, bookID);
		updateStatement.setString(3, bookID);
		updateStatement.executeUpdate();
		updateStatement.close();
	}

	private void showUpdate() {
		UpdateBook updateWindow = new UpdateBook(userID);
		updateWindow.upFrame.setVisible(true);
	}

}

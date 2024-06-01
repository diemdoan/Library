package LibraryManagement;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;


public class Chat extends JFrame {
	private JTextField txtMessage;
	private JButton btnSend;
	private JButton btnFile;
	private JScrollPane chatPanel;
	private JLabel lbReceiver = new JLabel(" ");
	private JTextPane chatWindow;
	JFrame cframe;

	JComboBox<String> onlineUsers = new JComboBox<String>();
	JComboBox<String> comboBox = new JComboBox<String>();

	private String username;
	private DataInputStream dis;
	private DataOutputStream dos;

	private HashMap<String, JTextPane> chatWindows = new HashMap<String, JTextPane>();

	Thread receiver;

	private int id;

	private void autoScroll() {
		chatPanel.getVerticalScrollBar().setValue(chatPanel.getVerticalScrollBar().getMaximum());
	}

	public void setUsername(String username) {
		this.username = username;
	}

	private void newEmoji(String username, String emoji, Boolean yourMessage) {

		StyledDocument doc;
		if (username.equals(this.username)) {
			doc = chatWindows.get(lbReceiver.getText()).getStyledDocument();
		} else {
			doc = chatWindows.get(username).getStyledDocument();
		}

		Style userStyle = doc.getStyle("User style");
		if (userStyle == null) {
			userStyle = doc.addStyle("User style", null);
			StyleConstants.setBold(userStyle, true);
		}

		if (yourMessage == true) {
			StyleConstants.setForeground(userStyle, Color.red);
		} else {
			StyleConstants.setForeground(userStyle, Color.BLUE);
		}

		try {
			doc.insertString(doc.getLength(), username + ": ", userStyle);
		} catch (BadLocationException e) {
		}

		Style iconStyle = doc.getStyle("Icon style");
		if (iconStyle == null) {
			iconStyle = doc.addStyle("Icon style", null);
		}

		StyleConstants.setIcon(iconStyle, new ImageIcon(emoji));

		try {
			doc.insertString(doc.getLength(), "invisible text", iconStyle);
		} catch (BadLocationException e) {
		}

		try {
			doc.insertString(doc.getLength(), "\n", userStyle);
		} catch (BadLocationException e) {
		}

		autoScroll();
	}

	private void newFile(String username, String filename, byte[] file, Boolean yourMessage) {

		StyledDocument doc;
		String window = null;
		if (username.equals(this.username)) {
			window = lbReceiver.getText();
		} else {
			window = username;
		}
		doc = chatWindows.get(window).getStyledDocument();

		Style userStyle = doc.getStyle("User style");
		if (userStyle == null) {
			userStyle = doc.addStyle("User style", null);
			StyleConstants.setBold(userStyle, true);
		}

		if (yourMessage == true) {
			StyleConstants.setForeground(userStyle, Color.red);
		} else {
			StyleConstants.setForeground(userStyle, Color.BLUE);
		}

		try {
			doc.insertString(doc.getLength(), username + ": ", userStyle);
		} catch (BadLocationException e) {
		}

		Style linkStyle = doc.getStyle("Link style");
		if (linkStyle == null) {
			linkStyle = doc.addStyle("Link style", null);
			StyleConstants.setForeground(linkStyle, Color.BLUE);
			StyleConstants.setUnderline(linkStyle, true);
			StyleConstants.setBold(linkStyle, true);
			linkStyle.addAttribute("link", new HyberlinkListener(filename, file));
		}

		if (chatWindows.get(window).getMouseListeners() != null) {
			chatWindows.get(window).addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(MouseEvent e) {
					Element ele = doc.getCharacterElement(chatWindow.viewToModel(e.getPoint()));
					AttributeSet as = ele.getAttributes();
					HyberlinkListener listener = (HyberlinkListener) as.getAttribute("link");
					if (listener != null) {
						listener.execute();
					}
				}

				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub

				}

			});
		}

		try {
			doc.insertString(doc.getLength(), "<" + filename + ">", linkStyle);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}

		try {
			doc.insertString(doc.getLength(), "\n", userStyle);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}

		autoScroll();
	}

	private void newMessage(String username, String message, Boolean yourMessage) {

		StyledDocument doc;
		if (username.equals(this.username)) {
			doc = chatWindows.get(lbReceiver.getText()).getStyledDocument();
		} else {
			doc = chatWindows.get(username).getStyledDocument();
		}

		Style userStyle = doc.getStyle("User style");
		if (userStyle == null) {
			userStyle = doc.addStyle("User style", null);
			StyleConstants.setBold(userStyle, true);
		}

		if (yourMessage == true) {
			StyleConstants.setForeground(userStyle, Color.red);
		} else {
			StyleConstants.setForeground(userStyle, Color.BLUE);
		}

		try {
			doc.insertString(doc.getLength(), username + ": ", userStyle);
		} catch (BadLocationException e) {
		}

		Style messageStyle = doc.getStyle("Message style");
		if (messageStyle == null) {
			messageStyle = doc.addStyle("Message style", null);
			StyleConstants.setForeground(messageStyle, Color.BLACK);
			StyleConstants.setBold(messageStyle, false);
		}

		try {
			doc.insertString(doc.getLength(), message + "\n", messageStyle);
		} catch (BadLocationException e) {
		}

		autoScroll();
	}

	public Chat(String username, DataInputStream dis, DataOutputStream dos, int id) {

		cframe = new JFrame();
		cframe.setBounds(100, 100, 1015, 611);
		cframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		cframe.getContentPane().setLayout(null);
		cframe.setLocationRelativeTo(null);
		cframe.setResizable(false);
		cframe.setVisible(true);

		this.username = username;
		this.dis = dis;
		this.dos = dos;
		receiver = new Thread(new Receiver(dis));
		receiver.start();

		this.id = id;

		ImageIcon backgroundImage = new ImageIcon(getClass().getResource("/DACS1_UngDungNhanTin/Img/nt.jpg"));
		JLabel backgroundLabel = new JLabel(backgroundImage);
		backgroundLabel.setBounds(0, 0, cframe.getWidth(), cframe.getHeight());
		cframe.setContentPane(backgroundLabel);

		JButton btnThot = new JButton("Thoát");
		btnThot.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnThot.setBounds(10, 13, 139, 33);
		cframe.getContentPane().add(btnThot);

		JLabel lblNewLabel = new JLabel(" Message");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 47));
		lblNewLabel.setBounds(397, 2, 237, 59);
		cframe.getContentPane().add(lblNewLabel);

		txtMessage = new JTextField();
		txtMessage.setBounds(191, 514, 555, 50);
		cframe.getContentPane().add(txtMessage);
		txtMessage.setFont(new Font("Tahoma", Font.PLAIN, 27));
		txtMessage.setColumns(1);

		ImageIcon sendIcon = new ImageIcon(getClass().getResource("/DACS1_UngDungNhanTin/Img/component/send.png"));
		Image scaledImage1 = sendIcon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
		ImageIcon scaledIcon1 = new ImageIcon(scaledImage1);
		btnSend = new JButton("", scaledIcon1);
		btnSend.setEnabled(false);
		btnSend.setBounds(841, 514, 150, 50);
		cframe.getContentPane().add(btnSend);

		chatPanel = new JScrollPane();
		chatPanel.setBounds(191, 109, 810, 376);
		cframe.getContentPane().add(chatPanel);
		chatPanel.setOpaque(false);
		chatPanel.getViewport().setOpaque(false);

		ImageIcon attIcon = new ImageIcon(getClass().getResource("/DACS1_UngDungNhanTin/Img/component/attach.png"));
		btnFile = new JButton("", attIcon);
		btnFile.setBounds(759, 514, 71, 50);
		cframe.getContentPane().add(btnFile);
		btnFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFileChooser fileChooser = new JFileChooser();
				int rVal = fileChooser.showOpenDialog(cframe.getParent());
				if (rVal == JFileChooser.APPROVE_OPTION) {
					byte[] selectedFile = new byte[(int) fileChooser.getSelectedFile().length()];
					BufferedInputStream bis;
					try {
						bis = new BufferedInputStream(new FileInputStream(fileChooser.getSelectedFile()));
						bis.read(selectedFile, 0, selectedFile.length);

						dos.writeUTF("File");
						dos.writeUTF(lbReceiver.getText());
						dos.writeUTF(fileChooser.getSelectedFile().getName());
						dos.writeUTF(String.valueOf(selectedFile.length));

						int size = selectedFile.length;
						int bufferSize = 2048;
						int offset = 0;

						while (size > 0) {
							dos.write(selectedFile, offset, Math.min(size, bufferSize));
							offset += Math.min(size, bufferSize);
							size -= bufferSize;
						}

						dos.flush();

						bis.close();
						newFile(username, fileChooser.getSelectedFile().getName(), selectedFile, true);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		btnFile.setEnabled(false);

		JLabel lblNewLabel_3 = new JLabel();
		lblNewLabel_3.setBounds(60, 110, 79, 79);
		cframe.getContentPane().add(lblNewLabel_3);
		ImageIcon icon = new ImageIcon(getClass().getResource("/DACS1_UngDungNhanTin/Img/user.png"));
		Image image = icon.getImage();
		int labelWidth = lblNewLabel_3.getWidth();
		int labelHeight = lblNewLabel_3.getHeight();
		Image scaledImage = image.getScaledInstance(labelWidth, labelHeight, Image.SCALE_SMOOTH);
		ImageIcon scaledIcon = new ImageIcon(scaledImage);
		lblNewLabel_3.setIcon(scaledIcon);

		JLabel lbUser = new JLabel("Chọn người nhắn");
		lbUser.setFont(new Font("Arial", Font.BOLD, 17));
		lbUser.setBounds(29, 302, 171, 27);
		cframe.getContentPane().add(lbUser);

		cframe.getContentPane().add(onlineUsers);
		onlineUsers.setBounds(13, 329, 171, 27);

		JPanel emojis = new JPanel();
		emojis.setBounds(297, 485, 569, 28);
		cframe.getContentPane().add(emojis);

		JLabel smileIcon = new JLabel(
				new ImageIcon("D:\\eclipse_workspace\\projecttesst\\src\\DACS1_UngDungNhanTin\\Img\\emoji\\mad.png"));
		smileIcon.addMouseListener(new IconListener(smileIcon.getIcon().toString()));
		emojis.add(smileIcon);

		JLabel bigSmileIcon = new JLabel(new ImageIcon(
				"D:\\eclipse_workspace\\projecttesst\\src\\DACS1_UngDungNhanTin\\Img\\emoji\\big-smile.png"));
		bigSmileIcon.addMouseListener(new IconListener(bigSmileIcon.getIcon().toString()));
		emojis.add(bigSmileIcon);

		JLabel happyIcon = new JLabel(
				new ImageIcon("D:\\eclipse_workspace\\projecttesst\\src\\DACS1_UngDungNhanTin\\Img\\emoji\\sad.png"));
		happyIcon.addMouseListener(new IconListener(happyIcon.getIcon().toString()));
		emojis.add(happyIcon);

		JLabel loveIcon = new JLabel(
				new ImageIcon("D:\\eclipse_workspace\\projecttesst\\src\\DACS1_UngDungNhanTin\\Img\\emoji\\love.png"));
		loveIcon.addMouseListener(new IconListener(loveIcon.getIcon().toString()));
		emojis.add(loveIcon);

		JLabel suspiciousIcon = new JLabel(new ImageIcon(
				"D:\\eclipse_workspace\\projecttesst\\src\\DACS1_UngDungNhanTin\\Img\\emoji\\suspicious.png"));
		suspiciousIcon.addMouseListener(new IconListener(suspiciousIcon.getIcon().toString()));
		emojis.add(suspiciousIcon);

		JLabel vietnamIcon = new JLabel(new ImageIcon(
				"D:\\eclipse_workspace\\projecttesst\\src\\DACS1_UngDungNhanTin\\Img\\emoji\\vietnam.png"));
		vietnamIcon.addMouseListener(new IconListener(vietnamIcon.getIcon().toString()));
		emojis.add(vietnamIcon);

		JLabel italyIcon = new JLabel(
				new ImageIcon("D:\\eclipse_workspace\\projecttesst\\src\\DACS1_UngDungNhanTin\\Img\\emoji\\italy.png"));
		italyIcon.addMouseListener(new IconListener(italyIcon.getIcon().toString()));
		emojis.add(italyIcon);

		JLabel ukIcon = new JLabel(
				new ImageIcon("D:\\eclipse_workspace\\projecttesst\\src\\DACS1_UngDungNhanTin\\Img\\emoji\\uk.png"));
		ukIcon.addMouseListener(new IconListener(ukIcon.getIcon().toString()));
		emojis.add(ukIcon);

		onlineUsers.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					lbReceiver.setText((String) onlineUsers.getSelectedItem());
					if (chatWindow != chatWindows.get(lbReceiver.getText())) {
						txtMessage.setText("");
						chatWindow = chatWindows.get(lbReceiver.getText());
						chatPanel.setViewportView(chatWindow);
						chatPanel.validate();
					}

					if (lbReceiver.getText().isBlank()) {
						btnSend.setEnabled(false);
						btnFile.setEnabled(false);
						txtMessage.setEnabled(false);
					} else {
						btnSend.setEnabled(true);
						btnFile.setEnabled(true);
						txtMessage.setEnabled(true);
					}
				}

			}
		});

		JLabel lbUsername = new JLabel(this.username);
		lbUsername.setFont(new Font("Arial", Font.BOLD, 23));
		lbUsername.setBounds(13, 190, 171, 27);
		cframe.getContentPane().add(lbUsername);
		lbUsername.setHorizontalAlignment(JLabel.CENTER);

		JPanel usernamePanel = new JPanel();
		usernamePanel.setBackground(new Color(230, 240, 247));
		chatPanel.setColumnHeaderView(usernamePanel);

		lbReceiver.setFont(new Font("Arial", Font.BOLD, 23));
		usernamePanel.add(lbReceiver);

		chatWindows.put(" ", new JTextPane());
		chatWindow = chatWindows.get(" ");
		chatWindow.setFont(new Font("Arial", Font.PLAIN, 18));
		chatWindow.setEditable(false);

		chatPanel.setViewportView(chatWindow);

		txtMessage.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (txtMessage.getText().isBlank() || lbReceiver.getText().isBlank()) {
					btnSend.setEnabled(false);
				} else {
					btnSend.setEnabled(true);
				}
			}
		});

		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					dos.writeUTF("Text");
					dos.writeUTF(lbReceiver.getText());
					dos.writeUTF(txtMessage.getText());
					dos.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
					newMessage("ERROR", "Network error!", true);
				}
				newMessage(username, txtMessage.getText(), true);
				txtMessage.setText("");
			}
		});

		cframe.getRootPane().setDefaultButton(btnSend);

		cframe.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {

				try {
					dos.writeUTF("Log out");
					dos.flush();

					try {
						receiver.join();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}

					if (dos != null) {
						dos.close();
					}
					if (dis != null) {
						dis.close();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		btnThot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					dos.writeUTF("Log out");
					dos.flush();

					try {
						receiver.join();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}

					if (dos != null) {
						dos.close();
					}
					if (dis != null) {
						dis.close();
					}

					showMenu();
					cframe.dispose();
					new LoginWindow();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		JLabel lblNewLabel_2 = new JLabel("Chọn background");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel_2.setBounds(743, 29, 139, 21);
		cframe.getContentPane().add(lblNewLabel_2);

		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		comboBox.setBounds(884, 29, 107, 21);
		cframe.getContentPane().add(comboBox);
		comboBox.addItem("Đỏ");
		comboBox.addItem("Lục");
		comboBox.addItem("Tím");
		comboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					String selectedItem = (String) comboBox.getSelectedItem();
					switch (selectedItem) {
					case "Tím":
						ImageIcon backgroundImage3 = new ImageIcon(
								getClass().getResource("/DACS1_UngDungNhanTin/Img/nt.jpg"));
						backgroundLabel.setIcon(backgroundImage3);
						break;
					case "Đỏ":
						ImageIcon backgroundImage1 = new ImageIcon(
								getClass().getResource("/DACS1_UngDungNhanTin/Img/redbg.jpg"));
						backgroundLabel.setIcon(backgroundImage1);
						break;
					case "Lục":
						ImageIcon backgroundImage2 = new ImageIcon(
								getClass().getResource("/DACS1_UngDungNhanTin/Img/green.jpg"));
						backgroundLabel.setIcon(backgroundImage2);
						break;

					}
					// Cập nhật lại hình nền
					cframe.getContentPane().repaint();
				}
			}
		});

	}

	private void showMenu() {
		MenuWindow menuWindow = new MenuWindow(id);
		menuWindow.frame.setVisible(true);
	}

	class Receiver implements Runnable {

		private DataInputStream dis;

		public Receiver(DataInputStream dis) {
			this.dis = dis;
		}

		@Override
		public void run() {
			try {

				while (true) {
					String method = dis.readUTF();

					if (method.equals("Text")) {
						String sender = dis.readUTF();
						String message = dis.readUTF();

						newMessage(sender, message, false);
					}

					else if (method.equals("Emoji")) {
						String sender = dis.readUTF();
						String emoji = dis.readUTF();
						newEmoji(sender, emoji, false);
					}

					else if (method.equals("File")) {
						String sender = dis.readUTF();
						String filename = dis.readUTF();
						int size = Integer.parseInt(dis.readUTF());
						int bufferSize = 2048;
						byte[] buffer = new byte[bufferSize];
						ByteArrayOutputStream file = new ByteArrayOutputStream();

						while (size > 0) {
							dis.read(buffer, 0, Math.min(bufferSize, size));
							file.write(buffer, 0, Math.min(bufferSize, size));
							size -= bufferSize;
						}

						// In ra màn hình file đó
						newFile(sender, filename, file.toByteArray(), false);

					}

					else if (method.equals("Online users")) {
						// Nhận yêu cầu cập nhật danh sách người dùng trực tuyến
						String[] users = dis.readUTF().split(",");
						onlineUsers.removeAllItems();

						String chatting = lbReceiver.getText();

						boolean isChattingOnline = false;

						for (String user : users) {
							if (user.equals(username) == false) {

								onlineUsers.addItem(user);
								if (chatWindows.get(user) == null) {
									JTextPane temp = new JTextPane();
									temp.setFont(new Font("Arial", Font.PLAIN, 14));
									temp.setEditable(false);
									chatWindows.put(user, temp);
								}
							}
							if (chatting.equals(user)) {
								isChattingOnline = true;
							}
						}

						if (isChattingOnline == false) {

							onlineUsers.setSelectedItem(" ");
							JOptionPane.showMessageDialog(null,
									chatting + " đã đăng xuất!\nThoát khỏi cuộc trò chuyện");
						} else {
							onlineUsers.setSelectedItem(chatting);
						}

						onlineUsers.validate();
					}

					else if (method.equals("Safe to leave")) {
						break;
					}

				}

			} catch (IOException ex) {
				System.err.println(ex);
			} finally {
				try {
					if (dis != null) {
						dis.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	class HyberlinkListener extends AbstractAction {
		String filename;
		byte[] file;

		public HyberlinkListener(String filename, byte[] file) {
			this.filename = filename;
			this.file = Arrays.copyOf(file, file.length);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			execute();
		}

		public void execute() {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setSelectedFile(new File(filename));
			int rVal = fileChooser.showSaveDialog(cframe.getParent());
			if (rVal == JFileChooser.APPROVE_OPTION) {

				File saveFile = fileChooser.getSelectedFile();
				BufferedOutputStream bos = null;
				try {
					bos = new BufferedOutputStream(new FileOutputStream(saveFile));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

				int nextAction = JOptionPane.showConfirmDialog(null,
						"Saved file to " + saveFile.getAbsolutePath() + "\nDo you want to open this file?",
						"Successful", JOptionPane.YES_NO_OPTION);
				if (nextAction == JOptionPane.YES_OPTION) {
					try {
						Desktop.getDesktop().open(saveFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				if (bos != null) {
					try {
						bos.write(this.file);
						bos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	class IconListener extends MouseAdapter {
		String emoji;

		public IconListener(String emoji) {
			this.emoji = emoji;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (txtMessage.isEnabled() == true) {

				try {
					dos.writeUTF("Emoji");
					dos.writeUTF(lbReceiver.getText());
					dos.writeUTF(this.emoji);
					dos.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
					newMessage("ERROR", "Network error!", true);
				}

				newEmoji(username, this.emoji, true);
			}
		}
	}
}
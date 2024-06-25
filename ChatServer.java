package LibraryManagement;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.sql.*;

public class ChatServer {
	private Object lock;

	private ServerSocket s;
	private Socket socket;
	static ArrayList<Handler> clients = new ArrayList<Handler>();

	public ChatServer() throws IOException {
		try {
			lock = new Object();

			s = new ServerSocket(9999);

			while (true) {

				socket = s.accept();

				DataInputStream dis = new DataInputStream(socket.getInputStream());
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

				String request = dis.readUTF();

				if (request.equals("Log in")) {
					String username = dis.readUTF();

					try {
						Connection conn = ConnectSQL.getConnection();
						PreparedStatement stmt = conn
								.prepareStatement("SELECT COUNT(*) FROM accounts WHERE AccName = ? ");
						stmt.setString(1, username);
						ResultSet rs = stmt.executeQuery();
						rs.next();
						int count = rs.getInt(1);
						rs.close();
						stmt.close();
						conn.close();

						if (count > 0) {
							// Tài khoản tồn tại, xử lý đăng nhập
							Handler newHandler = new Handler(socket, username, true, lock);
							clients.add(newHandler);
							dos.writeUTF("Log in successful");
							dos.flush();
							Thread t = new Thread(newHandler);
							t.start();
							updateOnlineUsers();
						} else {
							dos.writeUTF("Username or password is not correct");
							dos.flush();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}

			}

		} catch (Exception ex) {
			System.err.println(ex);
		} finally {
			if (s != null) {
				s.close();
			}
		}
	}

	public boolean isExisted(String name) {
		try {
			Connection conn = ConnectSQL.getConnection();
			PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM account WHERE accName = ?");
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			int count = rs.getInt(1);
			rs.close();
			stmt.close();
			conn.close();
			return count > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void updateOnlineUsers() {
		String message = " ";
		for (Handler client : clients) {
			if (client.getIsLoggedIn() == true) {
				message += ",";
				message += client.getUsername();
			}
		}
		for (Handler client : clients) {
			if (client.getIsLoggedIn() == true) {
				try {
					client.getDos().writeUTF("Online users");
					client.getDos().writeUTF(message);
					client.getDos().flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}

class Handler implements Runnable {

	private Object lock;

	private Socket socket;
	private DataInputStream dis;
	private DataOutputStream dos;
	private String username;
	private String password;
	private boolean isLoggedIn;

	public Handler(Socket socket, String username, boolean isLoggedIn, Object lock) throws IOException {
		this.socket = socket;
		this.username = username;
		this.dis = new DataInputStream(socket.getInputStream());
		this.dos = new DataOutputStream(socket.getOutputStream());
		this.isLoggedIn = isLoggedIn;
		this.lock = lock;
	}

	public Handler(String username, boolean isLoggedIn, Object lock) {
		this.username = username;
		this.isLoggedIn = isLoggedIn;
		this.lock = lock;
	}

	public void setIsLoggedIn(boolean IsLoggedIn) {
		this.isLoggedIn = IsLoggedIn;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
		try {
			this.dis = new DataInputStream(socket.getInputStream());
			this.dos = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void closeSocket() {
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean getIsLoggedIn() {
		return this.isLoggedIn;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public DataOutputStream getDos() {
		return this.dos;
	}

	@Override
	public void run() {

		while (true) {
			try {
				String message = null;

				message = dis.readUTF();

				if (message.equals("Log out")) {

					dos.writeUTF("Safe to leave");
					dos.flush();

					socket.close();
					this.isLoggedIn = false;

					ChatServer.updateOnlineUsers();
					break;
				}

				else if (message.equals("Text")) {
					String receiver = dis.readUTF();
					String content = dis.readUTF();

					for (Handler client : ChatServer.clients) {
						if (client.getUsername().equals(receiver)) {
							synchronized (lock) {
								client.getDos().writeUTF("Text");
								client.getDos().writeUTF(this.username);
								client.getDos().writeUTF(content);
								client.getDos().flush();
								break;
							}
						}
					}
				}

				else if (message.equals("Emoji")) {
					String receiver = dis.readUTF();
					String emoji = dis.readUTF();

					for (Handler client : ChatServer.clients) {
						if (client.getUsername().equals(receiver)) {
							synchronized (lock) {
								client.getDos().writeUTF("Emoji");
								client.getDos().writeUTF(this.username);
								client.getDos().writeUTF(emoji);
								client.getDos().flush();
								break;
							}
						}
					}
				}

				else if (message.equals("File")) {

					String receiver = dis.readUTF();
					String filename = dis.readUTF();
					int size = Integer.parseInt(dis.readUTF());
					int bufferSize = 2048;
					byte[] buffer = new byte[bufferSize];

					for (Handler client : ChatServer.clients) {
						if (client.getUsername().equals(receiver)) {
							synchronized (lock) {
								client.getDos().writeUTF("File");
								client.getDos().writeUTF(this.username);
								client.getDos().writeUTF(filename);
								client.getDos().writeUTF(String.valueOf(size));
								while (size > 0) {

									dis.read(buffer, 0, Math.min(size, bufferSize));
									client.getDos().write(buffer, 0, Math.min(size, bufferSize));
									size -= bufferSize;
								}
								client.getDos().flush();
								break;
							}
						}
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}
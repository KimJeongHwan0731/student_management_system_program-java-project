package studentProject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class DBConnection {

	private Connection connection = null;

	public void connect() {
		Properties properties = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream("C:/202301java_Workspace123/student_project/src/studentProject/db.properties");
			properties.load(fis);
		} catch (FileNotFoundException e) {
			System.out.println("FileInputStream error" + e.getStackTrace());
		} catch (IOException e) {
			System.out.println("Properties.load error" + e.getStackTrace());
		}

		try {
			Class.forName(properties.getProperty("driverName"));
			connection = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("user"),
					properties.getProperty("password"));
		} catch (ClassNotFoundException e) {
			System.out.println("[데이터베이스 로드오류]" + e.getStackTrace());
		} catch (SQLException e) {
			System.out.println("[데이터베이스 연결오류]" + e.getStackTrace());
		}

	}

	public int insert(Student s) {
		this.connect();
		PreparedStatement ps = null;
		int returnValue = -1;
		String query = "insert into studentTBL values(null,?,?,?,?,?,?,?,?)";

		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, s.getName());
			ps.setInt(2, s.getAge());
			ps.setInt(3, s.getKor());
			ps.setInt(4, s.getEng());
			ps.setInt(5, s.getMath());
			ps.setInt(6, s.getTotal());
			ps.setDouble(7, s.getAvg());
			ps.setString(8, s.getGrade());
			returnValue = ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("insert 오류발생" + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close 오류" + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("connection close 오류" + e.getMessage());
			}
		}

		return returnValue;
	}

	public ArrayList<Student> select() {
		ArrayList<Student> list = new ArrayList<>();
		this.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "select * from studentTBL";

		try {
			ps = connection.prepareStatement(query);
			rs = ps.executeQuery();
			if (rs == null) {
				return null;
			}
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				int kor = rs.getInt("kor");
				int eng = rs.getInt("eng");
				int math = rs.getInt("math");
				int total = rs.getInt("total");
				double avg = rs.getDouble("avg");
				String grade = rs.getString("grade");
				list.add(new Student(id, name, age, kor, eng, math, total, avg, grade));
			}
		} catch (Exception e) {
			System.out.println("select 오류발생" + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close 오류" + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("connection close 오류" + e.getMessage());
			}
		}
		return list;
	}

	public ArrayList<Student> analizeSelect() {
		ArrayList<Student> list = new ArrayList<>();
		this.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "select id, name, age, total, avg, grade from studentTBL";

		try {
			ps = connection.prepareStatement(query);
			rs = ps.executeQuery();
			if (rs == null) {
				return null;
			}
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				int total = rs.getInt("total");
				double avg = rs.getDouble("avg");
				String grade = rs.getString("grade");
				list.add(new Student(id, name, age, 0, 0, 0, total, avg, grade));
			}
		} catch (Exception e) {
			System.out.println("select 오류발생" + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close 오류" + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("connection close 오류" + e.getMessage());
			}
		}
		return list;
	}

	public ArrayList<Student> nameSearchSelect(String dataName) {
		ArrayList<Student> list = new ArrayList<>();
		this.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "select * from studentTBL where name like ?";

		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, "%" + dataName + "%");
			rs = ps.executeQuery();
			if (rs == null) {
				return null;
			}
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				int kor = rs.getInt("kor");
				int eng = rs.getInt("eng");
				int math = rs.getInt("math");
				int total = rs.getInt("total");
				double avg = rs.getDouble("avg");
				String grade = rs.getString("grade");
				list.add(new Student(id, name, age, kor, eng, math, total, avg, grade));
			}
		} catch (Exception e) {
			System.out.println("select 오류발생" + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close 오류" + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("connection close 오류" + e.getMessage());
			}
		}
		return list;
	}

	public Student selectId(int dataId) {
		Student student = null;
		this.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "select * from studentTBL where id = ?";

		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, dataId);
			rs = ps.executeQuery();
			if (rs == null) {
				return null;
			}
			if (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				int kor = rs.getInt("kor");
				int eng = rs.getInt("eng");
				int math = rs.getInt("math");
				int total = rs.getInt("total");
				double avg = rs.getDouble("avg");
				String grade = rs.getString("grade");
				student = new Student(id, name, age, kor, eng, math, total, avg, grade);
			}
		} catch (Exception e) {
			System.out.println("select id 오류발생" + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close 오류" + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("connection close 오류" + e.getMessage());
			}
		}
		return student;
	}

	public int update(Student s) {
		this.connect();
		PreparedStatement ps = null;
		int returnValue = -1;
		String query = "update studentTBL set kor = ?, eng = ?, math = ?, total = ?, avg = ?, grade = ? where id = ?";

		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, s.getKor());
			ps.setInt(2, s.getEng());
			ps.setInt(3, s.getMath());
			ps.setInt(4, s.getTotal());
			ps.setDouble(5, s.getAvg());
			ps.setString(6, s.getGrade());
			ps.setInt(7, s.getId());
			returnValue = ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("update 오류발생" + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close 오류" + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("connection close 오류" + e.getMessage());
			}
		}
		return returnValue;
	}

	public ArrayList<Student> selectSort() {
		ArrayList<Student> list = new ArrayList<>();
		this.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "select * from studentTBL order by total desc";

		try {
			ps = connection.prepareStatement(query);
			rs = ps.executeQuery();
			if (rs == null) {
				return null;
			}
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				int kor = rs.getInt("kor");
				int eng = rs.getInt("eng");
				int math = rs.getInt("math");
				int total = rs.getInt("total");
				double avg = rs.getDouble("avg");
				String grade = rs.getString("grade");
				list.add(new Student(id, name, age, kor, eng, math, total, avg, grade));
			}
		} catch (Exception e) {
			System.out.println("select 오류발생" + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close 오류" + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("connection close 오류" + e.getMessage());
			}
		}
		return list;
	}

	public int delete(int deleteId) {
		this.connect();
		PreparedStatement ps = null;
		int returnValue = -1;
		String query = "delete from studentTBL where id = ?";

		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, deleteId);

			returnValue = ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("insert 오류발생" + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close 오류" + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("connection close 오류" + e.getMessage());
			}
		}
		return returnValue;
	}
}

package studentProject;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentMain {

	public static Scanner sc = new Scanner(System.in);
	public static final int INPUT = 1, PRINT = 2, ANLYZE = 3, SEARCH = 4, UPDATE = 5, SORT = 6, DELETE = 7, EXIT = 8;

	public static void main(String[] args) {

		boolean run = true;
		int no = 0;
		DBConnection dbCon = new DBConnection();

		while (run) {
			System.out.println(
					"============================================= ÇĞ»ı ¼ºÀû ÇÁ·Î±×·¥ ==============================================");
			System.out.println(
					"[1]Á¤º¸ ÀÔ·Â | [2]Á¤º¸ Ãâ·Â | [3]Á¤º¸ ºĞ¼® | [4]Á¤º¸ °Ë»ö | [5]Á¤º¸ ¼öÁ¤ | [6] ¼øÀ§ Á¤·Ä | [7] Á¤º¸ »èÁ¦ | [8] ÇÁ·Î±×·¥ Á¾·á");
			System.out.println(
					"===========================================================================================================");
			System.out.print("¼±ÅÃ > ");
			no = Integer.parseInt(sc.nextLine());
			switch (no) {
			case INPUT:
				Student student = inputDataStudent();
				int rValue = dbCon.insert(student);
				if (rValue == 1) {
					System.out.println("»ğÀÔ¼º°ø");
				} else {
					System.err.println("»ğÀÔ½ÇÆĞ");
				}
				break;
			case PRINT:
				ArrayList<Student> list2 = dbCon.select();
				if (list2 == null) {
					System.err.println("¼±ÅÃ½ÇÆĞ");
				} else {
					printStudent(list2);
				}
				break;
			case ANLYZE:
				ArrayList<Student> list3 = dbCon.analizeSelect();
				if (list3 == null) {
					System.err.println("¼±ÅÃ½ÇÆĞ");
				} else {
					printAnalyze(list3);
				}
				break;
			case SEARCH:
				String dataName = searchStudent();
				ArrayList<Student> list4 = dbCon.nameSearchSelect(dataName);
				if (list4.size() >= 1) {
					printStudent(list4);
				} else {
					System.err.println("ÇĞ»ı ÀÌ¸§ °Ë»ö ¿À·ù");
				}
				break;
			case UPDATE:
				int updateReturnValue = 0;
				int id = inputId();
				Student stu = dbCon.selectId(id);
				if (stu == null) {
					System.err.println("¼öÁ¤¿À·ù ¹ß»ı");
				} else {
					Student updateStudent = updateStudent(stu);
					updateReturnValue = dbCon.update(updateStudent);
				}

				if (updateReturnValue == 1) {
					System.out.println("¼öÁ¤ ¼º°ø");
				} else {
					System.err.println("¼öÁ¤ ½ÇÆĞ");
				}
				break;
			case SORT:
				ArrayList<Student> list5 = dbCon.selectSort();
				if (list5 == null) {
					System.err.println("Á¤·Ä ½ÇÆĞ");
				} else {
					printScoreSort(list5);
				}
				break;
			case DELETE:
				int deleteId = inputId();
				int deleteReturnValue = dbCon.delete(deleteId);
				if (deleteReturnValue == 1) {
					System.out.println("»èÁ¦ ¼º°ø");
				} else {
					System.err.println("»èÁ¦ ½ÇÆĞ");
				}
				break;
			case EXIT:
				run = false;
				break;
			default:
				System.err.println("º¸±â¸¦ È®ÀÎ ÈÄ ´Ù½Ã ÀÔ·ÂÇØ ÁÖ¼¼¿ä.");
				continue;
			}
		}
		System.out.println("ÇÁ·Î±×·¥ Á¾·á");
	}

	private static int inputId() {
		boolean run = true;
		int id = 0;
		while (run) {
			try {
				System.out.print("ID ÀÔ·Â(number): ");
				id = Integer.parseInt(sc.nextLine());
				if (id > 0 && id < Integer.MAX_VALUE) {
					run = false;
				}
			} catch (NumberFormatException e) {
				System.out.println("id ÀÔ·Â ¿À·ù");
			}
		}
		return id;
	}

	private static void printScoreSort(ArrayList<Student> list) {
		System.out.println("¼øÀ§" + "\t" + "ID" + "\t" + "ÀÌ ¸§" + "\t" + "³ªÀÌ" + "\t" + "±¹¾î" + "\t" + "¿µ¾î" + "\t" + "¼öÇĞ"
				+ "\t" + "ÃÑÁ¡" + "\t" + "Æò±Õ" + "\t" + "µî±Ş");
		for (int i = 0; i < list.size(); i++) {
			System.out.println(i + 1 + "µî\t" + list.get(i));
		}
	}

	private static Student updateStudent(Student student) {
		int kor = inputScoreSubject(student.getName(), "±¹¾î", student.getKor());
		student.setKor(kor);
		int eng = inputScoreSubject(student.getName(), "¿µ¾î", student.getEng());
		student.setEng(eng);
		int math = inputScoreSubject(student.getName(), "¼öÇĞ", student.getMath());
		student.setMath(math);
		student.calTotal();
		student.calAvg();
		student.calGrade();
		System.out.println("ID" + "\t" + "ÀÌ ¸§" + "\t" + "³ªÀÌ" + "\t" + "±¹¾î" + "\t" + "¿µ¾î" + "\t" + "¼öÇĞ" + "\t" + "ÃÑÁ¡"
				+ "\t" + "Æò±Õ" + "\t" + "µî±Ş");
		System.out.println(student);
		return student;
	}

	private static int inputScoreSubject(String subject, String name, int score) {
		boolean run = true;
		int data = 0;
		while (run) {
			System.out.print(name + " " + subject + " " + score + ">>");
			try {
				data = Integer.parseInt(sc.nextLine());
				Pattern pattern = Pattern.compile("^[0-9]{1,3}$");
				Matcher matcher = pattern.matcher(String.valueOf(data));
				if (matcher.find() && data < 101 && data >= 0) {
					run = false;
				} else {
					System.out.println("Á¡¼ö¸¦ Àß¸øÀÔ·ÂÇÏ¿´½À´Ï´Ù. ÀçÀÔ·Â¿äÃ»");
				}
			} catch (NumberFormatException e) {
				System.out.println("Á¡¼ö ÀÔ·Â¿¡ ¿À·ù ¹ß»ı");
				data = 0;
			}
		}
		return data;
	}

	private static String matchingNamePattern() {
		String name = null;
		while (true) {
			try {
				System.out.print("°Ë»öÇÒ ÇĞ»ıÀÌ¸§: ");
				name = sc.nextLine();
				Pattern pattern = Pattern.compile("^[°¡-ÆR]{2,4}$");
				Matcher matcher = pattern.matcher(name);
				if (!matcher.find()) {
					System.out.println("ÀÌ¸§ÀÔ·Â¿À·ù¹ß»ı ´Ù½ÃÀçÀÔ·Â¿äÃ»ÇÕ´Ï´Ù.");
				} else {
					break;
				}
			} catch (Exception e) {
				System.out.println("ÀÔ·Â¿¡¼­ ¿À·ù°¡ ¹ß»ıÇß½À´Ï´Ù.");
				break;
			}

		}
		return name;
	}

	private static String searchStudent() {
		String name = null;
		name = matchingNamePattern();
		return name;
	}

	private static void printAnalyze(ArrayList<Student> list) {
		System.out.println("ID" + "\t" + "ÀÌ ¸§" + "\t" + "³ªÀÌ" + "\t" + "ÃÑÁ¡" + "\t" + "Æò±Õ" + "\t" + "µî±Ş");
		for (Student data : list) {
			System.out.println(data.getId() + "\t" + data.getName() + "\t" + data.getAge() + "\t" + data.getTotal()
					+ "\t" + String.format("%.2f", data.getAvg()) + "\t" + data.getGrade());
		}
	}

	private static void printStudent(ArrayList<Student> list) {
		System.out.println("ID" + "\t" + "ÀÌ ¸§" + "\t" + "³ªÀÌ" + "\t" + "±¹¾î" + "\t" + "¿µ¾î" + "\t" + "¼öÇĞ" + "\t" + "ÃÑÁ¡"
				+ "\t" + "Æò±Õ" + "\t" + "µî±Ş");
		for (Student data : list) {
			System.out.println(data);
		}
	}

	private static Student inputDataStudent() {
		String name = inputName();
		int age = inputAge();
		int kor = inputScore("±¹¾î");
		int eng = inputScore("¿µ¾î");
		int math = inputScore("¼öÇĞ");
		Student student = new Student(name, age, kor, eng, math);
		student.calTotal();
		student.calAvg();
		student.calGrade();
		return student;
	}

	private static int inputScore(String subject) {
		int score = 0;

		while (true) {
			try {
				System.out.print(subject + "Á¡¼ö ÀÔ·Â : ");
				score = Integer.parseInt(sc.nextLine());
				Pattern pattern = Pattern.compile("^[0-9]{1,3}$");
				Matcher matcher = pattern.matcher(String.valueOf(score));
				if (matcher.find() && score <= 100) {
					break;
				} else {
					System.err.println("Á¡¼ö ÀÔ·Â ¿À·ù ¹ß»ı! ÀÔ·ÂÇÏ½Å ÀÌ¸§À» ÀçÈ®ÀÎÇØÁÖ¼¼¿ä.");
				}
			} catch (NumberFormatException e) {
				System.err.println("Á¡¼ö ÀÔ·Â ¿À·ù ¹ß»ı");
				score = 0;
				break;
			}
		}
		return score;
	}

	private static int inputAge() {
		int age = 0;

		while (true) {
			try {
				System.out.print("³ªÀÌ ÀÔ·Â : ");
				age = Integer.parseInt(sc.nextLine());
				Pattern pattern = Pattern.compile("^[0-9]{1,3}$");
				Matcher matcher = pattern.matcher(String.valueOf(age));
				if (matcher.find() && age <= 100) {
					break;
				} else {
					System.err.println("³ªÀÌ ÀÔ·Â ¿À·ù ¹ß»ı! ÀÔ·ÂÇÏ½Å ÀÌ¸§À» ÀçÈ®ÀÎÇØÁÖ¼¼¿ä.");
				}
			} catch (NumberFormatException e) {
				System.err.println("³ªÀÌ ÀÔ·Â ¿À·ù ¹ß»ı");
				age = 0;
				break;
			}
		}
		return age;
	}

	public static String inputName() {
		String name = null;

		while (true) {
			try {
				System.out.print("ÀÌ¸§ ÀÔ·Â : ");
				name = sc.nextLine();
				Pattern pattern = Pattern.compile("^[°¡-ÆR]{2,4}$");
				Matcher matcher = pattern.matcher(name);
				if (matcher.find()) {
					break;
				} else {
					System.err.println("ÀÌ¸§ ÀÔ·Â ¿À·ù ¹ß»ı! ÀÔ·ÂÇÏ½Å ÀÌ¸§À» ÀçÈ®ÀÎÇØÁÖ¼¼¿ä.");
				}
			} catch (Exception e) {
				System.err.println("ÀÌ¸§ ÀÔ·Â ¿À·ù ¹ß»ı");
				name = null;
				break;
			}
		}
		return name;
	}
}

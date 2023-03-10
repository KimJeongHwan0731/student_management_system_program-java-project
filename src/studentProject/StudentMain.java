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
					"============================================= 학생 성적 프로그램 ==============================================");
			System.out.println(
					"[1]정보 입력 | [2]정보 출력 | [3]정보 분석 | [4]정보 검색 | [5]정보 수정 | [6] 순위 정렬 | [7] 정보 삭제 | [8] 프로그램 종료");
			System.out.println(
					"===========================================================================================================");
			System.out.print("선택 > ");
			no = Integer.parseInt(sc.nextLine());
			switch (no) {
			case INPUT:
				Student student = inputDataStudent();
				int rValue = dbCon.insert(student);
				if (rValue == 1) {
					System.out.println("삽입성공");
				} else {
					System.err.println("삽입실패");
				}
				break;
			case PRINT:
				ArrayList<Student> list2 = dbCon.select();
				if (list2 == null) {
					System.err.println("선택실패");
				} else {
					printStudent(list2);
				}
				break;
			case ANLYZE:
				ArrayList<Student> list3 = dbCon.analizeSelect();
				if (list3 == null) {
					System.err.println("선택실패");
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
					System.err.println("학생 이름 검색 오류");
				}
				break;
			case UPDATE:
				int updateReturnValue = 0;
				int id = inputId();
				Student stu = dbCon.selectId(id);
				if (stu == null) {
					System.err.println("수정오류 발생");
				} else {
					Student updateStudent = updateStudent(stu);
					updateReturnValue = dbCon.update(updateStudent);
				}

				if (updateReturnValue == 1) {
					System.out.println("수정 성공");
				} else {
					System.err.println("수정 실패");
				}
				break;
			case SORT:
				ArrayList<Student> list5 = dbCon.selectSort();
				if (list5 == null) {
					System.err.println("정렬 실패");
				} else {
					printScoreSort(list5);
				}
				break;
			case DELETE:
				int deleteId = inputId();
				int deleteReturnValue = dbCon.delete(deleteId);
				if (deleteReturnValue == 1) {
					System.out.println("삭제 성공");
				} else {
					System.err.println("삭제 실패");
				}
				break;
			case EXIT:
				run = false;
				break;
			default:
				System.err.println("보기를 확인 후 다시 입력해 주세요.");
				continue;
			}
		}
		System.out.println("프로그램 종료");
	}

	private static int inputId() {
		boolean run = true;
		int id = 0;
		while (run) {
			try {
				System.out.print("ID 입력(number): ");
				id = Integer.parseInt(sc.nextLine());
				if (id > 0 && id < Integer.MAX_VALUE) {
					run = false;
				}
			} catch (NumberFormatException e) {
				System.out.println("id 입력 오류");
			}
		}
		return id;
	}

	private static void printScoreSort(ArrayList<Student> list) {
		System.out.println("순위" + "\t" + "ID" + "\t" + "이 름" + "\t" + "나이" + "\t" + "국어" + "\t" + "영어" + "\t" + "수학"
				+ "\t" + "총점" + "\t" + "평균" + "\t" + "등급");
		for (int i = 0; i < list.size(); i++) {
			System.out.println(i + 1 + "등\t" + list.get(i));
		}
	}

	private static Student updateStudent(Student student) {
		int kor = inputScoreSubject(student.getName(), "국어", student.getKor());
		student.setKor(kor);
		int eng = inputScoreSubject(student.getName(), "영어", student.getEng());
		student.setEng(eng);
		int math = inputScoreSubject(student.getName(), "수학", student.getMath());
		student.setMath(math);
		student.calTotal();
		student.calAvg();
		student.calGrade();
		System.out.println("ID" + "\t" + "이 름" + "\t" + "나이" + "\t" + "국어" + "\t" + "영어" + "\t" + "수학" + "\t" + "총점"
				+ "\t" + "평균" + "\t" + "등급");
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
					System.out.println("점수를 잘못입력하였습니다. 재입력요청");
				}
			} catch (NumberFormatException e) {
				System.out.println("점수 입력에 오류 발생");
				data = 0;
			}
		}
		return data;
	}

	private static String matchingNamePattern() {
		String name = null;
		while (true) {
			try {
				System.out.print("검색할 학생이름: ");
				name = sc.nextLine();
				Pattern pattern = Pattern.compile("^[가-힣]{2,4}$");
				Matcher matcher = pattern.matcher(name);
				if (!matcher.find()) {
					System.out.println("이름입력오류발생 다시재입력요청합니다.");
				} else {
					break;
				}
			} catch (Exception e) {
				System.out.println("입력에서 오류가 발생했습니다.");
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
		System.out.println("ID" + "\t" + "이 름" + "\t" + "나이" + "\t" + "총점" + "\t" + "평균" + "\t" + "등급");
		for (Student data : list) {
			System.out.println(data.getId() + "\t" + data.getName() + "\t" + data.getAge() + "\t" + data.getTotal()
					+ "\t" + String.format("%.2f", data.getAvg()) + "\t" + data.getGrade());
		}
	}

	private static void printStudent(ArrayList<Student> list) {
		System.out.println("ID" + "\t" + "이 름" + "\t" + "나이" + "\t" + "국어" + "\t" + "영어" + "\t" + "수학" + "\t" + "총점"
				+ "\t" + "평균" + "\t" + "등급");
		for (Student data : list) {
			System.out.println(data);
		}
	}

	private static Student inputDataStudent() {
		String name = inputName();
		int age = inputAge();
		int kor = inputScore("국어");
		int eng = inputScore("영어");
		int math = inputScore("수학");
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
				System.out.print(subject + "점수 입력 : ");
				score = Integer.parseInt(sc.nextLine());
				Pattern pattern = Pattern.compile("^[0-9]{1,3}$");
				Matcher matcher = pattern.matcher(String.valueOf(score));
				if (matcher.find() && score <= 100) {
					break;
				} else {
					System.err.println("점수 입력 오류 발생! 입력하신 이름을 재확인해주세요.");
				}
			} catch (NumberFormatException e) {
				System.err.println("점수 입력 오류 발생");
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
				System.out.print("나이 입력 : ");
				age = Integer.parseInt(sc.nextLine());
				Pattern pattern = Pattern.compile("^[0-9]{1,3}$");
				Matcher matcher = pattern.matcher(String.valueOf(age));
				if (matcher.find() && age <= 100) {
					break;
				} else {
					System.err.println("나이 입력 오류 발생! 입력하신 이름을 재확인해주세요.");
				}
			} catch (NumberFormatException e) {
				System.err.println("나이 입력 오류 발생");
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
				System.out.print("이름 입력 : ");
				name = sc.nextLine();
				Pattern pattern = Pattern.compile("^[가-힣]{2,4}$");
				Matcher matcher = pattern.matcher(name);
				if (matcher.find()) {
					break;
				} else {
					System.err.println("이름 입력 오류 발생! 입력하신 이름을 재확인해주세요.");
				}
			} catch (Exception e) {
				System.err.println("이름 입력 오류 발생");
				name = null;
				break;
			}
		}
		return name;
	}
}

package Home;

import java.io.*;
import java.sql.*;

public class GradeReport {

	public GradeReport(Connection connection) throws NumberFormatException, IOException, SQLException {

		String csvFile = "/home/shiru/V Sem/DBMS/Lab/Home Assignment/grdreport.csv";

		BufferedReader csvLine = new BufferedReader(new FileReader(csvFile));

		String rowOFcsv = null;
		int addedStudent = 0, passedStudent = 0;

		while ((rowOFcsv = csvLine.readLine()) != null) {

			String data[] = rowOFcsv.split(",");
			String id = data[0];
			String course_id = data[1];
			String grade = data[5];

			addedStudent += 1;

			String query1 = "SELECT COUNT(*) FROM course WHERE course_id = ?";
			PreparedStatement checkingCourse = connection.prepareStatement(query1);
			checkingCourse.setString(1, course_id);

			ResultSet countCourse = checkingCourse.executeQuery();
			countCourse.next();

			if (countCourse.getInt("count") != 0) {

				Student stud = new Student(connection, id, grade, course_id);

				if (stud.isPassed()) {
					passedStudent += 1;
				}
			} else {
				System.out.println("There doesn't exist course with course_id : " + course_id);
			}
		}

		csvLine.close();

		System.out.println("No. of students in the grade report : " + addedStudent);
		System.out.println("No. of students that have passed : " + passedStudent);
		System.out.println("No. of students with FF grade : " + (addedStudent - passedStudent));

		connection.close();
	}
}

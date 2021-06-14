package Home;

import java.io.*;
import java.sql.*;

public class Student {
	
	private boolean passed;
	
	public boolean isPassed() {
		return passed;
	}
	
	private void setPassed(boolean passed) {
		this.passed = passed;
	}
	
	public Student(Connection connection,String id,String grade,String course_id) {
		
		try {
			
			String query2 = "SELECT COUNT(*) FROM takes WHERE id = ?";
			PreparedStatement checkingStudent = connection.prepareStatement(query2);
			checkingStudent.setString(1, id);
			ResultSet countStudent = checkingStudent.executeQuery();
			countStudent.next();
			
			if (countStudent.getInt("count") != 0) {
				String query3 = "UPDATE takes SET grade = ? WHERE id = ?";
				PreparedStatement updateGrade = connection.prepareStatement(query3);
				updateGrade.setString(1, grade);
				updateGrade.setString(2, id);
				updateGrade.executeUpdate();

				if (!grade.equals("F")) {
					
					setPassed(true);

					String query5 = "SELECT credits FROM course WHERE course_id = ?";
					PreparedStatement getCredits = connection.prepareStatement(query5);
					getCredits.setString(1, course_id);
					ResultSet noOfCredits = getCredits.executeQuery();
					noOfCredits.next();

					String query6 = "SELECT tot_cred FROM student WHERE id = ?";
					PreparedStatement getTotalStudentCredits = connection.prepareStatement(query6);
					getTotalStudentCredits.setString(1, id);
					ResultSet totalCreditsRow = getTotalStudentCredits.executeQuery();
					totalCreditsRow.next();

					int creditsToUpdate = totalCreditsRow.getInt("tot_cred")
							+ Integer.parseInt(noOfCredits.getString("credits"));

					// UPDATE tot_cred
					String query4 = "UPDATE student SET tot_cred = ? WHERE id = ?";
					PreparedStatement updateTotalStudentCredits = connection.prepareStatement(query4);

					updateTotalStudentCredits.setInt(1, creditsToUpdate);
					updateTotalStudentCredits.setString(2, id);
					updateTotalStudentCredits.executeUpdate();
				}
			} else {
				System.out.println("Student doesn't exist with id : " + id);
			}
		} catch (NumberFormatException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}

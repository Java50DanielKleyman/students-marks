package telran.students;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

import telran.students.dto.Student;
import telran.students.exceptions.StudentIllegalStateException;
import telran.students.service.StudentsService;

@SpringBootTest
class StudentsMarksServiceTests {
	private static final String SERVICE_TEST = "Service: ";
	private static final long ID1 = 123l;
	private static final String PHONE1 = "05756896";

	@Autowired
	StudentsService studentsService;

	Student student1 = new Student(ID1, PHONE1);

	@Test
	@DisplayName(SERVICE_TEST + TestDisplayNames.ADDING_STUDENT_NORMAL)
	void addStudentNormal() {
		Student res = studentsService.addStudent(student1);
		assertEquals(student1, res);
	}

	@Test
	@DisplayName(SERVICE_TEST + TestDisplayNames.ADDING_STUDENT_ALREADY_EXISTS)
	void addStudentAlreadyExists() {
		assertThrowsExactly(StudentIllegalStateException.class, () -> studentsService.addStudent(student1));
	}
}
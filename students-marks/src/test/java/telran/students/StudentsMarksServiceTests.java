package telran.students;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import telran.students.dto.Mark;
import telran.students.dto.Student;
import telran.students.exceptions.StudentIllegalStateException;
import telran.students.exceptions.StudentNotFoundException;
import telran.students.model.StudentDoc;
import telran.students.repo.StudentRepo;
import telran.students.service.StudentsService;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StudentsMarksServiceTests {
	private static final String SERVICE_TEST = "Service: ";
	private static final long ID1 = 123l;
	private static final long ID2 = 124l;
	private static final long ID3 = 125l;
	private static final String PHONE1 = "0123456";
	private static final String PHONE2 = "01234567";
	private static final String PHONE3 = "012345678";
	private static final String SUBJECT = "Mathematics";
	private static final int SCORE = 95;
	private static final LocalDate DATE = LocalDate.of(2023, 12, 31);

	Student student = new Student(ID1, PHONE1);
	Student student1 = new Student(ID3, PHONE3);
	Mark mark = new Mark(SUBJECT, SCORE, DATE);

	@Autowired
	StudentsService studentsService;

	@Autowired
	StudentRepo studentRepo;

	@BeforeAll
	void setUp() {
		studentsService.addStudent(student);
	}

	@Test
	@DisplayName(SERVICE_TEST + TestDisplayNames.ADDING_STUDENT_NORMAL)
	void addStudentNormal() {
		Student res = studentsService.addStudent(student1);
		assertEquals(student1, res);
		StudentDoc studentDoc = studentRepo.findById(ID3).orElseThrow(() -> new StudentNotFoundException());
		assertEquals(student1, studentDoc.build());
	}

	@Test
	@DisplayName(SERVICE_TEST + TestDisplayNames.ADDING_STUDENT_ALREADY_EXISTS)
	void addStudentAlreadyExists() {
		assertThrowsExactly(StudentIllegalStateException.class, () -> studentsService.addStudent(student));
	}

	@Test
	@DisplayName(SERVICE_TEST + TestDisplayNames.UPDATE_PHONE_NUMBER_NORMAL)
	void updatePhoneNumberNormal() {
		Student res = studentsService.updatePhoneNumber(ID1, PHONE2);
		assertEquals(PHONE2, res.phone());
		StudentDoc studentDoc = studentRepo.findById(ID1).orElseThrow(() -> new StudentNotFoundException());
		assertEquals(PHONE2, studentDoc.getPhone());
	}

	@Test
	@DisplayName(SERVICE_TEST + TestDisplayNames.UPDATE_PHONE_NUMBER_STUDENT_NOT_FOUND)
	void updatePhoneNumberStudentNotFound() {
		assertThrowsExactly(StudentNotFoundException.class, () -> studentsService.updatePhoneNumber(ID2, PHONE2));
	}

	@Test
	@DisplayName(SERVICE_TEST + TestDisplayNames.ADDING_MARK_NORMAL)
	void addMarkNormal() {
		Mark res = studentsService.addMark(ID1, mark);
		assertEquals(res.subject(), SUBJECT);
		assertEquals(res.score(), SCORE);
		assertEquals(res.date(), DATE);
		StudentDoc studentDoc = studentRepo.findById(ID1).orElseThrow(() -> new StudentNotFoundException());
		List<Mark> marksList = studentDoc.getMarks();
		assertEquals(marksList.get(0).subject(), SUBJECT);
		assertEquals(marksList.get(0).score(), SCORE);
		assertEquals(marksList.get(0).date(), DATE);

	}

	@Test
	@DisplayName(SERVICE_TEST + TestDisplayNames.ADDING_MARK_STUDENT_NOT_FOUND)
	void addMarkStudentNotFound() {
		assertThrowsExactly(StudentNotFoundException.class, () -> studentsService.addMark(ID2, mark));
	}
}
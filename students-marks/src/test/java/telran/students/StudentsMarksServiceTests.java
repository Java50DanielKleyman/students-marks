package telran.students;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import static telran.students.TestDb.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import telran.students.dto.*;
import telran.students.exceptions.*;
import telran.students.repo.StudentRepo;
import telran.students.service.StudentsService;

@SpringBootTest

class StudentsMarksServiceTests {
	@Autowired
	StudentsService studentsService;
	@Autowired
	StudentRepo studentRepo;
	@Autowired
	TestDb testDb;
	
	static final long ID8 = 8;	
	static final String PHONE8 = "058-1234567";
	Student student8 = new Student(ID8, PHONE8);
	Student studentUpdated = new Student(ID1, PHONE2);
	Mark mark = new Mark(SUBJECT2,70,DATE1);
	
	@BeforeEach
	void setUp() {
		testDb.createDb();
	}

	@Test
	
	void addStudentTest() {
		//FIXME according to TestDb
		assertEquals(student8, studentsService.addStudent(student8));
		assertEquals(student8, studentRepo.findById(ID8).orElseThrow().build());
		assertThrowsExactly(StudentIllegalStateException.class, ()->studentsService.addStudent(student8));
	}
	@Test

	void updatePhoneNumberTest() {
		//FIXME according to TestDb		
		assertEquals(studentUpdated, studentsService.updatePhoneNumber(ID1, PHONE2));
		assertEquals(studentUpdated, studentRepo.findById(ID1).orElseThrow().build());
		assertThrowsExactly(StudentNotFoundException.class,
				()->studentsService.updatePhoneNumber(ID1 + 1000, PHONE2));
	}
	@Test
	
	void addMarkTest() {
		//FIXME according to TestDb
		assertFalse(studentRepo.findById(ID1).orElseThrow().getMarks().contains(mark));
		assertEquals(mark, studentsService.addMark(ID1, mark));
		assertTrue(studentRepo.findById(ID1).orElseThrow().getMarks().contains(mark));
		assertThrowsExactly(StudentNotFoundException.class,
				()->studentsService.addMark(ID1 + 1000, mark));
		
	}
	@Test
	void getStudentTest() {
		assertEquals(students[0], studentsService.getStudent(ID1));
		assertThrowsExactly(StudentNotFoundException.class, ()->studentsService.getStudent(100000));
	}
	@Test
	void getMarksTest() {
		assertArrayEquals(testDb.getMarks()[0], studentsService.getMarks(ID1).toArray(Mark[]::new));
		assertThrowsExactly(StudentNotFoundException.class, ()->studentsService.getMarks(100000));
	}
	@Test
	void getStudentByPhoneNumberTest() {
		assertEquals(students[0], studentsService.getStudentByPhoneNumber(PHONE1));
	}
	@Test
	void getStudentsByPhonePrefix() {
		List<Student> expected = List.of(students[0], students[6]);
		assertIterableEquals(expected, studentsService.getStudentsByPhonePrefix("051"));
	}
	//TODO tests of the interface methods for HW #71 (see StudentsService interface)
	
	

}
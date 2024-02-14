package telran.students.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import telran.students.model.StudentDoc;

public interface StudentRepo extends MongoRepository<StudentDoc, Long> {
	@Query(value = "{id:?0}", fields = "{id:1, phone:1}")
	StudentDoc findStudentNoMarks(long id);

	@Query(value = "{id:?0}", fields = "{id:0, marks:1}")
	StudentDoc findStudentOnlyMarks(long id);

	/********************************/
	IdPhone findByPhone(String phone);

	List<IdPhone> findByPhoneRegex(String regex);

	@Query(value = "{ 'marks' : { $elemMatch: { 'date' : ?0 } } }", fields = "{id:1, phone:1}")
	List<StudentDoc> findStudentsWithMarksOnDate(LocalDate date);

	@Query(value = "{ 'marks.date' : { $gte: ?0, $lte: ?1 } }", fields = "{id:1, phone:1}")
	List<StudentDoc> findStudentsWithMarksOnMonthAndYear(LocalDate startDate, LocalDate endDate);

	@Query(value = "{ 'marks' : {$elemMatch: { 'subject' : ?0 , 'score' : {$gt: ?1}} }}", fields = "{id:1, phone:1}")
	List<StudentDoc> findStudentsWithGoodSubjectMark(String subject, int markThreshold);

	@Query(value = "{ 'marks' : {$elemMatch: { 'score' : {$gt: ?0}} }}", fields = "{id:1, phone:1}")
	List<StudentDoc> findStudentsWithAllGoodMarks(int markThreshold);

	@Query(value = "{ 'marks' : {$size : {$lt : ?0} }}", fields = "{id:1, phone:1}")
	List<StudentDoc> findStudentsWithFewMarks(int nMarks);
}
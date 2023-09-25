package adaptiveschool.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import adaptiveschool.dto.SubjectAvgMarkDTO;
import adaptiveschool.model.Mark;

@Repository
public interface MarkRepository extends JpaRepository<Mark, Integer> {
    
    @Query(value="select AVG(CAST(m.mark as DECIMAL)) as avg_mark, COUNT(m.mark) as count, l.date as date "
            + "from mark m left join lesson l on m.lesson_id = l.id "
            + "where (:subjectId is null or l.subject_id = :subjectId)"
            + "and (:classId is null or l.clazz_id = :classId)"
            + "and (:studentId is null or m.student_id = :studentId)"
            + "and (:startDate is null or l.date >= :startDate)"
            + "and (:endDate is null or l.date <= :endDate) "
            + "group by l.date order by l.date", nativeQuery=true)
    List<Map<String, Object>> getFilteredByParamsGroupedByDate(@Param("subjectId") Integer subjectId, @Param("classId")Integer classId,
            @Param("studentId") Integer studentId, @Param("startDate") String startDate, @Param("endDate") String endDate);

    @Modifying
    @Transactional
    @Query(value = "insert into mark(mark,note,lesson_id,student_id) values(:mark,:note,:idLesson,:idStudent)\n" +
            "on duplicate key update mark=values(mark),note = values(note)", nativeQuery = true)
    void saveMarkByLesson(@Param("idStudent") int idStudent,@Param("idLesson") int idLesson,
                                @Param("mark") byte mark,@Param("note") String note);

    Mark findTopByStudentIdAndLessonId(int studentId,int lessonId);

    @Modifying
    @Transactional
    @Query(value = "update lesson set mark_type_id = :markTypeID where id=:idLesson", nativeQuery = true)
    void saveTypeByLesson(@Param("idLesson") int idLesson,@Param("markTypeID") int markTypeID);

    @Query("select new adaptiveschool.dto.SubjectAvgMarkDTO(avg(m.mark), m.lesson.subject.id, m.lesson.subject.name)"
            + " from Mark m"
            + " where m.student.id = :studentId"
            + " and (m.lesson.date >= :startDate or :startDate is null)"
            + " and (m.lesson.date <= :endDate  or :endDate is null)"
            + " group by m.lesson.subject.id")
    List<SubjectAvgMarkDTO> getFilteredByStudentGroupedBySubject(
            @Param("studentId") Integer studentId, 
            @Param("startDate") Date startDate, 
            @Param("endDate") Date endDate);
}

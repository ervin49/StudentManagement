package com.example.usermanagement.services;

import com.example.usermanagement.models.Grade;
import com.example.usermanagement.models.Student;
import com.example.usermanagement.models.Subject;
import com.example.usermanagement.repositories.GradeRepository;
import com.example.usermanagement.repositories.SubjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class GradeServiceTest {
    @InjectMocks
    private GradeService gradeService;

    @Mock
    private GradeRepository gradeRepository;

    @Mock
    private SubjectRepository subjectRepository;

    Subject math = Subject.builder()
            .id(100)
            .credits(5)
            .name("Math")
            .build();

    Student student = Student.builder()
            .email("student@example.com")
            .password("parola123")
            .build();

    Grade mathGrade = Grade.builder()
            .value(10)
            .subject(math)
            .student(student)
            .build();

    @Test
    public void should_add_grade() {
        gradeService.addGrade(mathGrade);
        verify(gradeRepository, times(1)).save(mathGrade);
    }

    @Test
    public void should_return_grades() {
        List<Grade> grades = gradeService.getAllGrades();
        verify(gradeRepository, times(1)).findAll();
    }

    @Test
    public void should_return_student_grades() {
        Integer student_id = 1;
        List<Grade> grades = gradeService.getGradesOfStudent(student_id);
        verify(gradeRepository, times(1)).findGradesByStudent_Id(student_id);
    }

    @Test
    public void update_grade() {
        gradeService.updateGrade(1, mathGrade);
        verify(gradeRepository).deleteById(1);
        verify(gradeRepository).save(mathGrade);
    }

    @Test
    public void get_grade() {
        gradeService.getSpecificGrade(1);
        verify(gradeRepository).findById(1);
    }

    @Test
    public void should_delete_grade() {
        Integer grade_id = 100;
        gradeService.deleteGrade(grade_id);
        verify(gradeRepository, times(1)).deleteById(grade_id);
    }

    @Test
    public void should_return_average_of_grades() {
        Subject chemistry = Subject.builder()
                .id(101)
                .credits(4)
                .name("Chemistry")
                .build();
        Subject physics = Subject.builder()
                .id(102)
                .credits(3)
                .name("Physics")
                .build();

        Grade physicsGrade = Grade.builder()
                .value(5)
                .subject(physics)
                .student(student)
                .build();

        Grade chemistryGrade = Grade.builder()
                .value(7)
                .subject(chemistry)
                .student(student)
                .build();

        when(gradeRepository.findGradesByStudent_Id(1)).thenReturn(List.of(mathGrade, physicsGrade, chemistryGrade));

        Double average = gradeService.getAverage(1);

        assertThat(average).isEqualTo(7.75);
    }

    @Test
    public void should_return_zero_when_no_grades() {
        when(gradeRepository.findGradesByStudent_Id(1)).thenReturn(List.of());

        Double average = gradeService.getAverage(1);

        assertThat(average).isEqualTo(0.0);
    }
}

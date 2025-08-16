package com.example.usermanagement;

import com.example.usermanagement.controllers.GradeController;
import com.example.usermanagement.services.StudentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentServiceUnitTest {

    @Autowired
    private GradeController gradeController;

    @Test
    public void test() {
        assertThat(gradeController).isNotNull();
    }
}

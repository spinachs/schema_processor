package com.jd.datamill9n.component.autoconfigure;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * TODO
 *
 * @author maliang56
 * @date 2020-09-30
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTest {
    @Autowired
    private StudentMapper studentMapper;

    @Test
    public void testInsert() {
        Student stu = new Student();
        stu.setName("a");
        stu.setAge(1);
        stu.setEmail("a@b.com");
        studentMapper.insert(stu);
    }

    @Test
    public void testSelectOne() {
        Student student = studentMapper.selectOne(1);
        System.out.println(student);
    }

    @Test
    public void testSelectOnes() {
        Student student = studentMapper.selectOnes(1, 1);
        System.out.println(student);
    }
}

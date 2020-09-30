package com.jd.datamill9n.component.autoconfigure;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @author maliang56
 * @date 2020-09-29
 */
@Mapper
@Component
public interface StudentMapper {
    @Insert("INSERT INTO student (id, name, age, email) VALUES (#{id}, #{name}, #{age}, #{email})")
    int insert(Student stu);
    @Select("select * from student where id = #{id}")
    Student selectOne(Integer id);
    @Select("select * from student where id = #{id}")
    Student selectOnes(Integer id, Integer age);
}
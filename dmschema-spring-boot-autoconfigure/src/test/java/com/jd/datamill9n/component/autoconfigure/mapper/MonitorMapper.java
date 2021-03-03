package com.jd.datamill9n.component.autoconfigure.mapper;

import com.jd.datamill9n.component.autoconfigure.table.MonitorTable;
import com.jd.datamill9n.component.autoconfigure.table.Student;
import com.jd.datamill9n.component.processor.annotation.JsfSchema;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * TODO
 *
 * @author maliang56
 * @date 2020-09-29
 */
@Mapper
@Component
public interface MonitorMapper {
    @JsfSchema(datasource = "targetGroup")
    List<MonitorTable> selectMonitor(@Param("monitorId") Long monitorId, @Param("startDate") String startDate,
                                     @Param("endDate") String endDate);

    List<Student> selectById(@Param("id") Long id);
}
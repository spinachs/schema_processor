package com.jd.datamill9n.component.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jd.datamill9n.component.autoconfigure.mapper.MonitorMapper;
import com.jd.datamill9n.component.multi_ds.service.jsf.DatasourceService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * TODO
 *
 * @author maliang56
 * @date 2020-09-30
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTest {
    @MockBean
    private DatasourceService datasourceService;
    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private MonitorMapper studentMapper;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setup() throws IOException {
        initMocks(this);

        Resource resource = resourceLoader.getResource("classpath:/monitor.json");
        List content = objectMapper.readValue(resource.getInputStream(), List.class);

        doReturn(content).when(datasourceService).executeSql(any());
    }

    @Test
    public void testSelect() {
        studentMapper.selectMonitor(0L, "", "");
    }

    @Test
    public void testStudent() {
        studentMapper.selectById(1L);
    }
}

package com.jd.datamill9n.component.processor.autoconfigure;

import com.jd.datamill9n.component.multi_ds.service.jsf.DatasourceService;
import com.jd.datamill9n.component.processor.interceptor.JsfQueryInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动
 *
 * @author maliang56
 * @date 2020-09-30
 */
@Configuration
@ConditionalOnClass({SqlSessionFactory.class, DatasourceService.class})
public class SchemaProcessorAutoConfiguration {
    @Bean
    @ConditionalOnBean({DatasourceService.class})
    public JsfQueryInterceptor jsfQueryInterceptor(DatasourceService datasourceService) {
        JsfQueryInterceptor interceptor = new JsfQueryInterceptor();
        interceptor.setDatasourceService(datasourceService);
        return interceptor;
    }
}

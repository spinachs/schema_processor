package com.jd.datamill9n.component.processor.interceptor;

import com.jd.datamill9n.common.error.exception.DatamillException;
import com.jd.datamill9n.common.error.exception.ErrorStatus;
import com.jd.datamill9n.component.multi_ds.service.jsf.DatasourceService;
import com.jd.datamill9n.component.multi_ds.service.jsf.bean.DatasourceRequest;
import com.jd.datamill9n.component.multi_ds.service.jsf.bean.Statement;
import com.jd.datamill9n.component.processor.annotation.JsfSchema;
import com.jd.datamill9n.component.processor.post.Post;
import com.jd.jsf.gd.util.RpcContext;
import lombok.Data;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.MDC;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;

/**
 * TODO
 *
 * @author maliang56
 * @date 2020-09-29
 */
@Data
@Intercepts({@Signature(type = Executor.class, method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class JsfQueryInterceptor implements Interceptor {
    private DatasourceService datasourceService;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        String id = ms.getId();
        int splitIndex = id.lastIndexOf(".");
        String className = id.substring(0, splitIndex);
        String methodName = id.substring(splitIndex + 1);

        Class<?> mapperClass = ClassUtils.forName(className, getClass().getClassLoader());
        Method method = Arrays.stream(mapperClass.getMethods())
                .filter(it -> it.getName().equals(methodName))
                .findFirst()
                .orElseThrow(() -> new DatamillException(ErrorStatus.REG_REQUEST_PARAM_INVALID, "查询方法不存在"));

        JsfSchema annotation = method.getAnnotation(JsfSchema.class);
        if (annotation == null) {
            return invocation.proceed();
        }

        List<LinkedHashMap<String, Object>> dataList = requestData(ms, args[1], annotation.datasource());
        if (annotation.tableBean() != Void.class && annotation.postActions().length > 0) {
            dataList = handlePost(dataList, annotation.postActions(), annotation.tableBean());
        }

        if (StringUtils.hasText(annotation.transformSpecs())) {
            dataList = transform(dataList, annotation.transformSpecs());
        }

        return dataList;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    private List<LinkedHashMap<String, Object>> requestData(MappedStatement ms, Object args, String datasource) {
        BoundSql boundSql = ms.getBoundSql(args);
        String sql = buildSql(ms.getConfiguration(), boundSql);

        DatasourceRequest request = new DatasourceRequest();
        request.setModuleType(datasource);

        Statement statement = new Statement();
        statement.setSql(sql);
        statement.setDatasource(request);

        resetRpcAttachments();
        return datasourceService.executeSql(statement);
    }

    private List<LinkedHashMap<String, Object>> handlePost(List<LinkedHashMap<String, Object>> dataList,
                                                           Class<? extends Post>[] postActions, Class<?> tableBean) {
        return dataList;
    }

    private List<LinkedHashMap<String, Object>> transform(List<LinkedHashMap<String, Object>> dataList, String specs) {
        return dataList;
    }

    private Map<String, Object> getRpcAttachments() {
        Map<String, Object> mdcMapObj = RpcContext.getContext().getAttachments();
        if (mdcMapObj != null && mdcMapObj.size() > 0) {
            Map<String, String> mdcMap = new HashMap<>();
            mdcMapObj.forEach((k, v) -> mdcMap.put(k, String.valueOf(v)));
            MDC.setContextMap(mdcMap);
        }
        return mdcMapObj;
    }

    private void resetRpcAttachments() {
        Map<String, Object> mdcMapObj = getRpcAttachments();

        Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();
        if (CollectionUtils.isEmpty(mdcMapObj) && CollectionUtils.isEmpty(copyOfContextMap)) {
            return;
        }

        if (mdcMapObj == null) {
            mdcMapObj = new HashMap<>();
        }
        if (copyOfContextMap != null) {
            for (String key : copyOfContextMap.keySet()) {
                mdcMapObj.put(key, copyOfContextMap.get(key));
            }
        }
        RpcContext.getContext().setAttachments(mdcMapObj);
    }

    private String getParameterValue(Object obj) {
        if (obj == null) {
            return "";
        }

        if (obj instanceof String) {
            return String.format("'%s'", obj);
        }

        if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT,
                    DateFormat.DEFAULT, Locale.CHINA);
            return String.format("'%s'", formatter.format(obj));
        }

        return obj.toString();
    }

    private String buildSql(Configuration configuration, BoundSql boundSql) {
        String sql = boundSql.getSql()
                .replaceAll("[\\s\n ]+", " ");
        Object parameterObject = boundSql.getParameterObject();
        if (parameterObject == null) {
            return sql;
        }
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (CollectionUtils.isEmpty(parameterMappings)) {
            return sql;
        }
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
            sql = sql.replaceFirst("\\?",
                    Matcher.quoteReplacement(getParameterValue(parameterObject)));
        } else {
            // MetaObject主要是封装了originalObject对象，提供了get和set的方法用于获取和设置originalObject的属性值,主要支持对JavaBean、Collection、Map三种类型对象的操作
            MetaObject metaObject = configuration.newMetaObject(parameterObject);
            for (ParameterMapping parameterMapping : parameterMappings) {
                String propertyName = parameterMapping.getProperty();
                Object param;
                if (metaObject.hasGetter(propertyName)) {
                    param = metaObject.getValue(propertyName);
                } else if (boundSql.hasAdditionalParameter(propertyName)) {
                    param = boundSql.getAdditionalParameter(propertyName);
                } else {
                    throw new RuntimeException(String.format("参数%s缺失", propertyName));
                }

                sql = sql.replaceFirst("\\?",
                        Matcher.quoteReplacement(getParameterValue(param)));
            }
        }
        return sql;
    }
}

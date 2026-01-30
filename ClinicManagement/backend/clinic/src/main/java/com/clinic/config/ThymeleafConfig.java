package com.clinic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.dialect.SpringStandardDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;
import org.thymeleaf.context.IContext;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.LinkedHashSet;
import java.util.Set;

@Configuration
public class ThymeleafConfig {

    @Bean
    public IExpressionObjectDialect customExpressionObjectDialect() {
        return new IExpressionObjectDialect() {
            @Override
            public String getName() {
                return "custom";
            }

            @Override
            public IExpressionObjectFactory getExpressionObjectFactory() {
                return new IExpressionObjectFactory() {
                    @Override
                    public Set<String> getAllExpressionObjectNames() {
                        Set<String> names = new LinkedHashSet<>();
                        names.add("request");
                        return names;
                    }

                    @Override
                    public Object buildObject(IContext context, String expressionObjectName) {
                        if ("request".equals(expressionObjectName)) {
                            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                            if (attributes != null) {
                                return attributes.getRequest();
                            }
                        }
                        return null;
                    }

                    @Override
                    public boolean isCacheable(String expressionObjectName) {
                        return false;
                    }
                };
            }
        };
    }
}

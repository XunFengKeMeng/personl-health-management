package com.example.health.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author huanghaiming
 * @date 2025/4/20 10:59
 * @description: Spring注入工具类
 *      Spring应用上下文(ApplicationContext)的工具类，用于在非Spring管理的类中获取Spring容器中的Bean。
 */
@Component
public class ApplicationContextHolder implements ApplicationContextAware {
    /**
     * 上下文作为静态变量，方便全局访问
     */
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHolder.applicationContext = applicationContext;
    }

    /**
     * 根据Bean名获取实例
     */
    public static Object getBeanByName(String beanName) {
        if (beanName == null || applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(beanName);
    }

    /**
     * 根据类型获取Bean
     */
    public static Object getBeanByType(Class clazz) {
        if (clazz == null || applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(clazz);
    }

    /**
     * 获取容器中所有 Bean 的定义名称
     */
    public static String[] getBeanDefinitionNames() {
        return applicationContext.getBeanDefinitionNames();
    }
}
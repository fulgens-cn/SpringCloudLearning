package cn.fulgens.order.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 解决org.springframework.beans.factory.BeanCreationNotAllowedException:
 * Error creating bean with name 'eurekaAutoServiceRegistration':
 * Singleton bean creation not allowed while singletons of this factory are in destruction
 *
 * 参考自：https://github.com/spring-cloud/spring-cloud-netflix/issues/1952
 */
@Component
public class FeignBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        if (containsBeanDefinition(beanFactory, "feignContext", "eurekaAutoServiceRegistration")) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition("feignContext");
            beanDefinition.setDependsOn("eurekaAutoServiceRegistration");
        }
    }

    private boolean containsBeanDefinition(ConfigurableListableBeanFactory beanFactory, String... beans) {
        return Arrays.stream(beans).allMatch(b -> beanFactory.containsBeanDefinition(b));
    }
}

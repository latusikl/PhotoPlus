package pl.polsl.photoplus.components;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Class stores injected application context.
 * Allows to inject beans to non Spring managed objects e.g. POJO.
 */
@Component
public class ContextProvider
        implements ApplicationContextAware
{
    private static ApplicationContext context;

    public static <T extends Object> T getBean(final Class<T> beanClass, final String beanName)
    {
        return context.getBean(beanClass, beanName);
    }

    public static <T extends Object> T getBean(final Class<T> beanClass)
    {
        return context.getBean(beanClass);
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException
    {
        ContextProvider.context = applicationContext;
    }
}

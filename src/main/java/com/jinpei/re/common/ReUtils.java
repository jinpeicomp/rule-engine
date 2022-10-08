package com.jinpei.re.common;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.FatalBeanException;
import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.springframework.beans.BeanUtils.getPropertyDescriptor;
import static org.springframework.beans.BeanUtils.getPropertyDescriptors;

/**
 * 工具类
 *
 * @author Mingo.Liu
 * @date 2022-09-28
 */
@NoArgsConstructor
@Slf4j
public class ReUtils {
    /**
     * 打印执行规则
     *
     * @param name 规则名称
     */
    public static void printRule(String name) {
        log.info("==========> Rule {} executes", name);
    }

    /**
     * 复制对象属性
     *
     * @param source     源对象
     * @param dest       目标对象
     * @param ignoreNull 是否忽略null值
     * @param <T>        泛型
     * @return 目标对象
     */
    public static <T> T copy(Object source, T dest, boolean ignoreNull) {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(dest, "Dest must not be null");

        Class<?> actualEditable = dest.getClass();
        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);

        for (PropertyDescriptor targetPd : targetPds) {
            Method writeMethod = targetPd.getWriteMethod();
            if (null != writeMethod) {
                PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (null != sourcePd) {
                    Method readMethod = sourcePd.getReadMethod();
                    if (null != readMethod) {
                        ResolvableType sourceResolvableType = ResolvableType.forMethodReturnType(readMethod);
                        ResolvableType targetResolvableType = ResolvableType.forMethodParameter(writeMethod, 0);
                        if (targetResolvableType.isAssignableFrom(sourceResolvableType)) {
                            try {
                                if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                    readMethod.setAccessible(true);
                                }
                                Object value = readMethod.invoke(source);
                                if (ignoreNull && null == value) {
                                    continue;
                                }

                                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                    writeMethod.setAccessible(true);
                                }
                                writeMethod.invoke(dest, value);
                            } catch (Throwable ex) {
                                throw new FatalBeanException("Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                            }
                        }
                    }
                }
            }
        }

        return dest;
    }
}

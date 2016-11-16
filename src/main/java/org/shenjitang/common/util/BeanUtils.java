package org.shenjitang.common.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BeanUtils {

	private BeanUtils() {

	}
	
	public static void fillTargetObject(Map<String, Object> propertyMap,
			Object targetObject) {
		try {
			List<String> propertyList = new ArrayList<String>(propertyMap
					.keySet());
			String method = "";
			for (String property : propertyList) {
				method = "set" + property.substring(0, 1).toUpperCase()
						+ property.substring(1);
				invokeMethod(targetObject, method, new Object[] { propertyMap
						.get(property) }, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static Object invokeMethod(Object target, String methodName,
			Object[] args, boolean isStatic) {
		Object obj = null;
		try {
			Class clazz = target.getClass();
			Method method = null;
			if (SystemUtils.isNotEmpty(args)) {
				Class[] argsClasses = new Class[args.length];
				for (int i = 0; i < argsClasses.length; i++) {
					argsClasses[i] = args[i].getClass();
				}
				method = clazz.getMethod(methodName, argsClasses);
			} else {
				method = clazz.getMethod(methodName);
			}
			if (isStatic) {
				obj = method.invoke(null, args);
			} else {
				obj = method.invoke(target, args);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	public static Object newInstance(String className) {
		return newInstance(className, null);
	}
	
	@SuppressWarnings("unchecked")
	public static Annotation getAnnotation(String className,
			Class annotationClass) {
		Annotation annotation = null;
		try {
			Class clazz = Class.forName(className);
			annotation = clazz.getAnnotation(annotationClass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return annotation;
	}

	@SuppressWarnings("unchecked")
	public static Object newInstance(String className, Object[] args) {
		Object obj = null;
		try {
			Class clazz = Class.forName(className);
			if (SystemUtils.isNotEmpty(args)) {
				Class[] argsClasses = new Class[args.length];
				for (int i = 0; i < argsClasses.length; i++) {
					argsClasses[i] = args[i].getClass();
				}
				Constructor constructor = clazz.getConstructor(argsClasses);
				obj = constructor.newInstance(args);
			}
			obj = clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

}

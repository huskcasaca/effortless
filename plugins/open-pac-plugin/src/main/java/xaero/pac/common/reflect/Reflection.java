package xaero.pac.common.reflect;

import xaero.pac.common.platform.Services;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Reflection {

	public static Field getFieldReflection(Class<?> c, String deobfName, String forgeObfuscatedName, String fabricObfuscatedName, String fabricObfuscatedDescriptor) {
		Field field = Services.PLATFORM.getMappingHelper().findForgeField(c, deobfName, forgeObfuscatedName);
		if(field == null) {
			try {
				fabricObfuscatedName = Services.PLATFORM.getMappingHelper().fixFabricFieldMapping(c, fabricObfuscatedName, fabricObfuscatedDescriptor);
				field = c.getDeclaredField(fabricObfuscatedName);
			} catch (NoSuchFieldException e1) {
				throw new RuntimeException(e1);
			}
		}
		return field;
	}

	@SuppressWarnings("unchecked")
	public static <A, B> B getReflectFieldValue(A parentObject, Field field) {
		boolean accessibleBU = field.isAccessible();
		field.setAccessible(true);
		B result = null;
		try {
			result = (B) field.get(parentObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		field.setAccessible(accessibleBU);
		return result;
	}

	public static <A, B> void setReflectFieldValue(A parentObject, Field field, B value) {
		boolean accessibleBU = field.isAccessible();
		field.setAccessible(true);
		try {
			field.set(parentObject, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		field.setAccessible(accessibleBU);
	}

	public static Method getMethodReflection(Class<?> c, String deobfName, String forgeObfuscatedName, String fabricObfuscatedName, String fabricObfuscatedDescriptor, Class<?>... parameters) {
		Method method = Services.PLATFORM.getMappingHelper().findForgeMethod(c, deobfName, forgeObfuscatedName);
		if(method == null){
			try {
				fabricObfuscatedName = Services.PLATFORM.getMappingHelper().fixFabricMethodMapping(c, fabricObfuscatedName, fabricObfuscatedDescriptor);
				method = c.getDeclaredMethod(fabricObfuscatedName, parameters);
			} catch (NoSuchMethodException e1) {
				throw new RuntimeException(e1);
			}
		}
		return method;
	}

	@SuppressWarnings("unchecked")
	public static <A, B> B getReflectMethodValue(A parentObject, Method method, Object... arguments) {
		boolean accessibleBU = method.isAccessible();
		method.setAccessible(true);
		B result = null;
		try {
			result = (B) method.invoke(parentObject, arguments);
		} catch (Exception e) {
			e.printStackTrace();
		}
		method.setAccessible(accessibleBU);
		return result;
	}

}

package it.itj.academy.blogbe.util;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

@Component
public class OutputDtoResponseUtil {
    public void filter(Object outputDto) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (outputDto instanceof Collection collection) {
            filterCollection(collection);
        } else {
            filterObject(outputDto);
        }
    }
    private void filterObject(Object object) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        if (object == null) {
            return;
        }
        Class<?> clazz = getClazz(object);
        for (Field field : clazz.getDeclaredFields()) {
            if (securityContextHolderAuthoritiesFilter().isEmpty() && (field.getName().equals("createdBy") || field.getName().equals("updatedBy"))) {
                hideField(object, field);
            }
        }
    }
    private void filterCollection(Collection collection) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        if (collection.isEmpty()) {
            return;
        }
        for (Object object : collection) {
            filterObject(object);
        }
    }
    private Method getGetter(Object outputDto, Field field) throws NoSuchMethodException {
        String getter = "get" + Character.toUpperCase(field.getName().charAt(0)) + field.getName().substring(1);
        return getClazz(outputDto).getDeclaredMethod(getter);
    }
    private Method getSetter(Object outputDto, Field field) throws NoSuchMethodException {
        String setter = "set" + Character.toUpperCase(field.getName().charAt(0)) + field.getName().substring(1);
        return getClazz(outputDto).getDeclaredMethod(setter, field.getType());
    }
    private Class<?> getClazz(Object outputDto) {
        return outputDto.getClass();
    }
    private List<String> securityContextHolderAuthoritiesFilter() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .filter(authority -> !authority.equals("ROLE_USER") && !authority.equals("ROLE_ANONYMOUS"))
            .toList();
    }
    private void hideField(Object outputDto, Field field) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (getGetter(outputDto, field).invoke(outputDto) != null) {
            getSetter(outputDto, field).invoke(outputDto, (Object) null);
        }
    }
}

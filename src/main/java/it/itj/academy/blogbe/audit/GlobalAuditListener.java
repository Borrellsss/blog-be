package it.itj.academy.blogbe.audit;

import it.itj.academy.blogbe.entity.User;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
public class GlobalAuditListener {
    @PrePersist
    public void prePersist(Object entity) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        setCreatedDate(entity);
        setLastModifiedDate(entity);
    }
    @PostPersist
    public void postPersist(Object entity) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        setCreatedBy(entity);
        setLastModifiedBy(entity);
    }
    @PreUpdate
    public void preUpdate(Object entity) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        setLastModifiedDate(entity);
        setLastModifiedBy(entity);
    }
    private void setCreatedDate(Object entity) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> clazz = getClazz(entity);
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getAnnotation(CreatedDate.class) != null && getGetter(entity, field).invoke(entity) == null) {
                getSetter(entity, field).invoke(entity, LocalDate.now());
            }
        }
    }
    private void setLastModifiedDate(Object entity) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> clazz = getClazz(entity);
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getAnnotation(LastModifiedDate.class) != null) {
                getSetter(entity, field).invoke(entity, LocalDateTime.now());
            }
        }
    }
    private void setCreatedBy(Object entity) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Class<?> clazz = getClazz(entity);
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getAnnotation(CreatedBy.class) != null && getGetter(entity, field).invoke(entity) == null) {
                setPrincipalOrUserId(entity, getSetter(entity, field));
            }
        }
    }
    private void setLastModifiedBy(Object entity) throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException {
        Class<?> clazz = getClazz(entity);
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getAnnotation(LastModifiedBy.class) != null && getGetter(entity, field).invoke(entity) == null) {
                setPrincipalOrUserId(entity, getSetter(entity, field));
            }
        }
    }
    private void setPrincipalOrUserId(Object entity, Method setter) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Class<?> clazz = getClazz(entity);
        if (SecurityContextHolder.getContext().getAuthentication() == null || SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            Method getter = getGetter(entity, clazz.getDeclaredField("id"));
            setter.invoke(entity, getter.invoke(entity));
        } else {
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            setter.invoke(entity, principal.getId());
        }
    }
    private Method getGetter(Object entity, Field field) throws NoSuchMethodException {
        String getter = "get" + Character.toUpperCase(field.getName().charAt(0)) + field.getName().substring(1);
        return getClazz(entity).getDeclaredMethod(getter);
    }
    private Method getSetter(Object entity, Field field) throws NoSuchMethodException {
        String setter = "set" + Character.toUpperCase(field.getName().charAt(0)) + field.getName().substring(1);
        return getClazz(entity).getDeclaredMethod(setter, field.getType());
    }
    private Class<?> getClazz(Object entity) {
        return entity.getClass();
    }
}

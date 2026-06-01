package pe.edu.upc.soft.work.platform.shared.test.util;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * Centralized reflection helper for unit tests.
 *
 * <p>Provides field-injection primitives that DO NOT depend on Spring's
 * own {@code org.springframework.test.util.ReflectionTestUtils}. Tests
 * across every bounded context should use this class instead of
 * implementing their own ad-hoc reflection helpers.
 *
 * <p>All exceptions are wrapped as {@link IllegalStateException} with
 * descriptive messages that name the target class and field. This means
 * a refactor that renames or removes a private field fails LOUDLY at the
 * exact test that depended on it, instead of silently producing a
 * mysterious {@code NoSuchFieldException} stack trace.
 *
 * <p>This class is intentionally final and has a private constructor:
 * it is a stateless utility, never to be instantiated or subclassed.
 */
public final class ReflectionTestUtils {

    private ReflectionTestUtils() {
        throw new AssertionError("ReflectionTestUtils is a utility class and must not be instantiated.");
    }

    /**
     * Sets the inherited {@code id} field on any aggregate root or
     * entity that extends an auditable base class. Walks the entire
     * class hierarchy until the field is located.
     *
     * @param target the entity/aggregate root whose id must be set; must not be {@code null}
     * @param value  the id value to assign (typically a {@link Long}); may be {@code null}
     *               only when the test explicitly intends to clear the field
     * @throws NullPointerException  if {@code target} is {@code null}
     * @throws IllegalStateException if no field named {@code "id"} is reachable on the
     *                               target's class hierarchy, or if access is denied
     */
    public static void setId(Object target, Object value) {
        Objects.requireNonNull(target, "[ReflectionTestUtils.setId] target must not be null");
        setField(target, "id", value);
    }

    /**
     * Sets a private (or otherwise non-accessible) field on a target
     * object. Walks the entire class hierarchy of the target until the
     * field is located, so inherited fields from mapped superclasses
     * (e.g. {@code AuditableAbstractAggregateRoot}) are handled
     * transparently.
     *
     * @param target    the object whose internal field must be modified; must not be {@code null}
     * @param fieldName the declared name of the field; must not be {@code null} or blank
     * @param value     the value to assign; may be {@code null}
     * @throws NullPointerException     if {@code target} or {@code fieldName} is {@code null}
     * @throws IllegalArgumentException if {@code fieldName} is blank
     * @throws IllegalStateException    if the field cannot be found, accessed, or assigned
     */
    public static void setField(Object target, String fieldName, Object value) {
        Objects.requireNonNull(target, "[ReflectionTestUtils.setField] target must not be null");
        Objects.requireNonNull(fieldName, "[ReflectionTestUtils.setField] fieldName must not be null");
        if (fieldName.isBlank()) {
            throw new IllegalArgumentException(
                    "[ReflectionTestUtils.setField] fieldName must not be blank");
        }

        Class<?> targetClass = target.getClass();
        Class<?> cursor = targetClass;
        while (cursor != null && cursor != Object.class) {
            try {
                Field field = cursor.getDeclaredField(fieldName);
                field.setAccessible(true);
                try {
                    field.set(target, value);
                    return;
                } catch (IllegalAccessException ex) {
                    throw new IllegalStateException(
                            "[ReflectionTestUtils.setField] Access denied while writing field '"
                                    + fieldName + "' on '" + targetClass.getName() + "'.", ex);
                }
            } catch (NoSuchFieldException ignored) {
                cursor = cursor.getSuperclass();
            }
        }

        throw new IllegalStateException(
                "[ReflectionTestUtils.setField] Field '" + fieldName
                        + "' was not found on '" + targetClass.getName()
                        + "' or any of its superclasses. The field may have been renamed"
                        + " or removed; update the test to match the current model.");
    }

    /**
     * Reads a private (or otherwise non-accessible) field from a target
     * object. Walks the entire class hierarchy of the target until the
     * field is located. Provided for symmetry with {@link #setField};
     * useful for assertions on internal state that has no public getter.
     *
     * @param target    the object whose internal field must be read; must not be {@code null}
     * @param fieldName the declared name of the field; must not be {@code null} or blank
     * @return the field value (boxed for primitives)
     * @throws NullPointerException     if {@code target} or {@code fieldName} is {@code null}
     * @throws IllegalArgumentException if {@code fieldName} is blank
     * @throws IllegalStateException    if the field cannot be found or accessed
     */
    public static Object getField(Object target, String fieldName) {
        Objects.requireNonNull(target, "[ReflectionTestUtils.getField] target must not be null");
        Objects.requireNonNull(fieldName, "[ReflectionTestUtils.getField] fieldName must not be null");
        if (fieldName.isBlank()) {
            throw new IllegalArgumentException(
                    "[ReflectionTestUtils.getField] fieldName must not be blank");
        }

        Class<?> targetClass = target.getClass();
        Class<?> cursor = targetClass;
        while (cursor != null && cursor != Object.class) {
            try {
                Field field = cursor.getDeclaredField(fieldName);
                field.setAccessible(true);
                try {
                    return field.get(target);
                } catch (IllegalAccessException ex) {
                    throw new IllegalStateException(
                            "[ReflectionTestUtils.getField] Access denied while reading field '"
                                    + fieldName + "' on '" + targetClass.getName() + "'.", ex);
                }
            } catch (NoSuchFieldException ignored) {
                cursor = cursor.getSuperclass();
            }
        }

        throw new IllegalStateException(
                "[ReflectionTestUtils.getField] Field '" + fieldName
                        + "' was not found on '" + targetClass.getName()
                        + "' or any of its superclasses.");
    }
}

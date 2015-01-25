package eu.tobiasheine.bitcoinwatcher.di;

import android.util.Log;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import static org.mockito.Mockito.mock;

abstract class MockDependencies {

    private final Map<Class<?>,InjectedComponent<?>> dependencies = new HashMap<>();

    protected void replaceDependencies(Object component) {
        for (final Field field : component.getClass().getDeclaredFields()) {

            if (field.isAnnotationPresent(Inject.class)) {
                field.setAccessible(true);
                try {
                    field.set(component,getDependencyOrMock(field.getType()));
                } catch (IllegalAccessException e) {
                    Log.e("MockDependencies", Log.getStackTraceString(e));
                }
            }
        }
    }

    public <T> void replace(final Class<T> type, T dependency) {
        dependencies.put(type, new InjectedComponent<>(dependency));
    }


    protected <T> T getDependencyOrMock(final Class<T> dependency) {
        T component = getDependency(dependency);

        if (component == null) {
            component = mock(dependency);
        }

        return component;
    }

    private <T> T getDependency(final Class<T> type) {

        if (!dependencies.containsKey(type)) {
            return null;
        }

        InjectedComponent<T> component = (InjectedComponent<T>) dependencies.get(type);
        return component.getImplementation();
    }
}

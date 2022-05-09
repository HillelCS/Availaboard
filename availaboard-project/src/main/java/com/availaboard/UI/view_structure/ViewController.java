package com.availaboard.UI.view_structure;

import com.availaboard.engine.resource.Resource;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ViewController implements Subject {

     {
        final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AssignableTypeFilter(Observer.class));

        final Set<BeanDefinition> components = provider.findCandidateComponents("com/availaboard/UI/webpage");

        for (final BeanDefinition component : components) {
            try {
                final Observer observer = (Observer) Class.forName(component.getBeanClassName()).getConstructor().newInstance();
                observer.register(this);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }


    List<Observer> observerList = new ArrayList();

    @Override
    public void addObserver(Observer observer) {
        observerList.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observerList.remove(observer);
    }

    @Override
    public void notifiyObservers() {
        observerList.forEach(observer -> observer.update());
    }
}

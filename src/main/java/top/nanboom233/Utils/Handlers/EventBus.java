package top.nanboom233.Utils.Handlers;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import top.nanboom233.MinamiAddons;
import top.nanboom233.Utils.Handlers.Events.EventTemplate;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Consumer;

public class EventBus {
    private static final EventBus INSTANCE = new EventBus();
    private final Map<Class<? extends EventTemplate>, List<Object>> registeredHandlers = new HashMap<>();

    private final Map<Class<? extends EventTemplate>, List<Object>> dirtyHandlers = new HashMap<>();

    public EventBus() {
        MinamiAddons.logger.warn("[-] Starting registering event handlers.");

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("top.nanboom233"))
                .setScanners(Scanners.MethodsAnnotated));
        Set<Method> methods = reflections.getMethodsAnnotatedWith(SubscribeEvent.class);

        for (Method method : methods) {
            MinamiAddons.logger.warn("[--] Found annotated method/handler:" + method.getName());

            //确保方法为静态方法
            if (!Modifier.isStatic(method.getModifiers())) {
                throw new IllegalArgumentException("[-X] Method " + method.getName() + " must be static to use @SubscribeEvent,since handler must be static.");
            }

            //确保方法无返回值
            if (!method.getReturnType().equals(void.class)) {
                throw new IllegalArgumentException("[-X] Method " + method.getName() + " must have no return values as an event handler");
            }

            //确保方法的参数是事件类型
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != 1 || !EventTemplate.class.isAssignableFrom(parameterTypes[0])) {
                throw new IllegalArgumentException("[-X] Method " + method.getName() + " has a wrong parameter list! Parameter should only be a Event.");
            }

            //注册该Handler
            @SuppressWarnings("unchecked")
            Class<? extends EventTemplate> eventType = (Class<? extends EventTemplate>) parameterTypes[0];
            register(eventType, method);

            MinamiAddons.logger.info("[-O] Successfully registered annotated method/handler:" + method.getName());
            MinamiAddons.logger.info("");
        }
    }

    public void post(EventTemplate event) {
        List<Object> handlers = registeredHandlers.get(event.getClass());
        if (handlers != null) {
            List<Object> removalQueue;
            if ((removalQueue = dirtyHandlers.get(event.getClass())) != null && !removalQueue.isEmpty()) {
                for (Object dirtyHandler : removalQueue) {
                    handlers.remove(dirtyHandler);
                }
                dirtyHandlers.get(event.getClass()).clear();
            }
            for (Object handler : handlers) {
                try {
                    if (handler instanceof Method) {
                        // 调用静态方法时不需要实例对象，传 null
                        ((Method) handler).invoke(null, event);
                    } else if (handler instanceof Consumer<?>) {
                        ((Consumer<EventTemplate>) handler).accept(event);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    MinamiAddons.logger.fatal("Unable to invoke the handler when dealing events.");
                }
            }
        }
    }

    public void register(Class<? extends EventTemplate> eventType, Object handler) {
        //如果还未存在该handler对应的Event则新建List，否则直接add
        registeredHandlers.computeIfAbsent(eventType, k -> new ArrayList<>()).add(handler);
    }

    public void unregister(Class<? extends EventTemplate> eventType, Object handler) {
        //如果还未存在该handler对应的Event则新建List，否则直接add
        //mark dirty rather than simply remove to avoid ConcurrentModificationException
        dirtyHandlers.computeIfAbsent(eventType, k -> new ArrayList<>()).add(handler);
    }

    public static EventBus getInstance() {
        return INSTANCE;
    }
}

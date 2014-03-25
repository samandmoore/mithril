package com.samandmoore.mithril;

import java.util.concurrent.ConcurrentMap;

import com.google.common.collect.Maps;

/**
 * NOTE: this is a gross solution to thread-safety. it's just for lazy peeps that don't use IoC. so, please, use IoC.
 * 
 * @author Sam Moore
 * @since 3/22/14 9:30 PM
 */
public final class MithrilEmailTemplateEngineProvider {

    private static final EngineRegistry registry = new EngineRegistry();

    private static final Class<EmailTemplateEngine> DEFAULT_EMAIL_TEMPLATE_ENGINE_KEY = EmailTemplateEngine.class;

    public static EmailTemplateEngine getDefault() {

        final EmailTemplateEngine emailTemplateEngine = registry.get(DEFAULT_EMAIL_TEMPLATE_ENGINE_KEY);

        if (emailTemplateEngine != null) {

            return emailTemplateEngine;
        }

        throw new RuntimeException("No default email template engine configured." +
                " You must register an engine with the provider before you can create mailers using the default engine.");
    }

    public static void setDefault(final EmailTemplateEngine emailTemplateEngine) {

        registry.register(DEFAULT_EMAIL_TEMPLATE_ENGINE_KEY, emailTemplateEngine);
    }

    private static final class EngineRegistry {

        private final ConcurrentMap<Class<? extends EmailTemplateEngine>, EmailTemplateEngine> engineMap;

        private EngineRegistry() {

            engineMap = Maps.newConcurrentMap();
        }

        public <C extends EmailTemplateEngine, T extends EmailTemplateEngine> void register(Class<C> type, T instance) {

            engineMap.put(type, instance);
        }

        public <C extends EmailTemplateEngine> EmailTemplateEngine get(Class<C> type) {

            return engineMap.get(type);
        }
    }
}

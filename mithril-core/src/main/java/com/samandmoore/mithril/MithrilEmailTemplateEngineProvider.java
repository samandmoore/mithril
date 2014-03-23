package com.samandmoore.mithril;

/**
 * NOTE: this shiz is NOT thread-safe. it's just for lazy peeps that don't use IoC
 * 
 * @author Sam Moore
 * @since 3/22/14 9:30 PM
 */
public final class MithrilEmailTemplateEngineProvider {

    private static EmailTemplateEngine DEFAULT_EMAIL_TEMPLATE_ENGINE;

    public static EmailTemplateEngine getDefault() {

        return DEFAULT_EMAIL_TEMPLATE_ENGINE;
    }

    public static void setDefault(final EmailTemplateEngine emailTemplateEngine) {

        DEFAULT_EMAIL_TEMPLATE_ENGINE = emailTemplateEngine;
    }
}

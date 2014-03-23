package com.samandmoore.mithril.handlebars;

import java.io.IOException;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.samandmoore.mithril.EmailTemplate;
import com.samandmoore.mithril.EmailTemplateEngine;

/**
 * @author Sam Moore
 * @since 3/22/14 10:54 PM
 */
public class HandlebarsEmailTemplateEngine implements EmailTemplateEngine {

    private final Handlebars handlebars;

    public HandlebarsEmailTemplateEngine(final Handlebars handlebars) {

        this.handlebars = handlebars;
    }

    @Override
    public EmailTemplate compile(final String templateName) throws IOException {

        final Template handlebarsTemplate = this.handlebars.compile(templateName);

        return new HandlebarsEmailTemplate(handlebarsTemplate);
    }
}

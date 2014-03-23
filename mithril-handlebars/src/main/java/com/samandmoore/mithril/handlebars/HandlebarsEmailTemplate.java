package com.samandmoore.mithril.handlebars;

import java.io.IOException;
import java.util.Map;

import com.github.jknack.handlebars.Template;
import com.samandmoore.mithril.EmailTemplate;

/**
 * @author Sam Moore
 * @since 3/22/14 10:54 PM
 */
public class HandlebarsEmailTemplate implements EmailTemplate {

    private final Template handlebarsTemplate;

    public HandlebarsEmailTemplate(final Template handlebarsTemplate) {

        this.handlebarsTemplate = handlebarsTemplate;
    }

    @Override
    public String apply(final Map<String, Object> model) throws IOException {

        return this.handlebarsTemplate.apply(model);
    }
}

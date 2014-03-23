package com.samandmoore.mithril;

import java.io.IOException;

/**
 * @author Sam Moore
 * @since 3/22/14 7:57 PM
 */
public interface EmailTemplateEngine {

    EmailTemplate compile(String templateName) throws IOException;
}

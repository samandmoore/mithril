package com.samandmoore.mithril;

import java.io.IOException;
import java.util.Map;

/**
 * @author Sam Moore
 * @since 3/22/14 7:59 PM
 */
public interface EmailTemplate {

    String apply(Map<String, Object> model) throws IOException;
}

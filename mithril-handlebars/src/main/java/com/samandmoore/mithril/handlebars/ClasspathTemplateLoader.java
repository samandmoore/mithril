package com.samandmoore.mithril.handlebars;

import java.io.IOException;
import java.net.URL;

import com.github.jknack.handlebars.io.URLTemplateLoader;

/**
 * @author Sam Moore
 * @since 3/22/14 11:14 PM
 */
public class ClasspathTemplateLoader extends URLTemplateLoader {

    private final ClassLoader classLoader = this.getClass().getClassLoader();

    public ClasspathTemplateLoader(final String prefix, final String suffix) {

        super.setPrefix(prefix);
        super.setSuffix(suffix);
    }

    @Override
    protected URL getResource(final String location) throws IOException {

        return classLoader.getResource(location);
    }
}

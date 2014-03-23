package com.samandmoore.mithril;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author Sam Moore
 * @since 3/22/14 8:55 PM
 */
public class MithrilConfigTest extends AbstractTestBase {

    @Test
    public void loadsPropertiesFromDefaultFile() {

        final MithrilConfig defaultConfig = MithrilConfig.getDefault();

        assertTrue(defaultConfig.isEnabled());
        assertEquals("smtp.gmail.com", defaultConfig.getHost());
        assertEquals(465, defaultConfig.getPort());
        assertEquals(true, defaultConfig.isUseSsl());
        assertEquals("username", defaultConfig.getUsername());
        assertEquals("password", defaultConfig.getPassword());
    }

    @Test
    public void loadsPropertiesFromCustomFile() {

        final MithrilConfig defaultConfig = new MithrilConfig.Builder().buildFromPropertiesFile("mithril-test.properties");

        assertFalse(defaultConfig.isEnabled());
        assertEquals("smtp.notgmail.com", defaultConfig.getHost());
        assertEquals(1234, defaultConfig.getPort());
        assertEquals(false, defaultConfig.isUseSsl());
        assertEquals("testusername", defaultConfig.getUsername());
        assertEquals("testpassword", defaultConfig.getPassword());
    }

    @Test
    public void builderWorks() {

        final MithrilConfig config = new MithrilConfig.Builder()
                .withEnabled(true)
                .withHost("host")
                .withPassword("password")
                .withUsername("username")
                .withPort(67676)
                .withUseSsl(true)
                .build();

        assertEquals(true, config.isEnabled());
        assertEquals("host", config.getHost());
        assertEquals(67676, config.getPort());
        assertEquals(true, config.isUseSsl());
        assertEquals("username", config.getUsername());
        assertEquals("password", config.getPassword());
    }

    @Test(expected = NullPointerException.class)
    public void buildRequiresHost() {

        new MithrilConfig.Builder()
                .withPassword("adsfads")
                .withPort(1)
                .withUsername("adfasdf")
                .withHost(null)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildRequiresPortGreaterThanZero() {

        new MithrilConfig.Builder()
                .withPassword("adsfads")
                .withPort(-3443)
                .withUsername("adfasdf")
                .withHost("adfasdf")
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildRequiresPassword() {

        new MithrilConfig.Builder()
                .withPassword(null)
                .withPort(1)
                .withUsername("adfasdf")
                .withHost("Adfasdf")
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildRequiresUsername() {

        new MithrilConfig.Builder()
                .withPassword("adsfads")
                .withPort(1)
                .withUsername(null)
                .withHost("adfasdfasd")
                .build();
    }
}

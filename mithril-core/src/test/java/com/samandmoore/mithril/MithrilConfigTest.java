package com.samandmoore.mithril;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author Sam Moore
 * @since 3/22/14 8:55 PM
 */
public class MithrilConfigTest {

    @Test
    public void loadsPropertiesFromDefaultFile() {

        final MithrilConfig defaultConfig = MithrilConfig.getDefault();

        assertTrue(defaultConfig.isEnabled());
        assertEquals("smtp.gmail.com", defaultConfig.getHost());
        assertEquals(465, defaultConfig.getPort());
        assertEquals(true, defaultConfig.isUseSsl());
        assertEquals("username", defaultConfig.getUsername());
        assertEquals("password", defaultConfig.getPassword());
        assertEquals("foo@example.org", defaultConfig.getFromAddress());
        assertEquals("Mr. Foo", defaultConfig.getFromName());
    }

    @Test
    public void loadsPropertiesFromCustomFile() {

        final MithrilConfig config = new MithrilConfig.Builder().fromPropertiesFile("mithril-test.properties").build();

        assertFalse(config.isEnabled());
        assertEquals("smtp.notgmail.com", config.getHost());
        assertEquals(1234, config.getPort());
        assertEquals(false, config.isUseSsl());
        assertEquals("testusername", config.getUsername());
        assertEquals("testpassword", config.getPassword());
        assertEquals("foo.test@example.org", config.getFromAddress());
        assertEquals("Mr. Test Foo", config.getFromName());
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
                .withFromAddress("from address")
                .withFromName("from name")
                .build();

        assertEquals(true, config.isEnabled());
        assertEquals("host", config.getHost());
        assertEquals(67676, config.getPort());
        assertEquals(true, config.isUseSsl());
        assertEquals("username", config.getUsername());
        assertEquals("password", config.getPassword());
        assertEquals("from address", config.getFromAddress());
        assertEquals("from name", config.getFromName());
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

    @Test(expected = IllegalArgumentException.class)
    public void buildRequiresFromAddress() {

        new MithrilConfig.Builder()
                .withPassword("adsfads")
                .withPort(1)
                .withUsername("Adfasfd")
                .withHost("adfasdfasd")
                .withFromAddress(null)
                .build();
    }
}

package com.samandmoore.mithril;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.Validate;

import com.google.common.base.Strings;

/**
 * @author Sam Moore
 * @since 3/22/14 8:03 PM
 */
public final class MithrilConfig {

    private static final String PROPERTIES_FILE_NAME = "mithril.properties";

    private static final class DefaultHolder {
        static final MithrilConfig instance = new Builder().fromPropertiesFile(PROPERTIES_FILE_NAME).build();
    }

    public static MithrilConfig getDefault() {

        return DefaultHolder.instance;
    }

    //mithril.enabled:false
    private final boolean enabled;

    //mithril.host
    private final String host;

    //mithril.port:465
    private final int port;

    //mithril.ssl:true
    private final boolean useSsl;

    //mithril.username
    private final String username;

    //mithril.password
    private final String password;

    //mithril.fromAddress
    private final String fromAddress;

    //mithril.fromName
    private final String fromName;

    private MithrilConfig(final boolean enabled, final String host, final int port, final boolean useSsl, final String username, final String password,
            final String fromAddress, final String fromName) {

        this.enabled = enabled;
        this.host = host;
        this.port = port;
        this.useSsl = useSsl;
        this.username = username;
        this.password = password;
        this.fromAddress = fromAddress;
        this.fromName = fromName;
    }

    public boolean isEnabled() {

        return enabled;
    }

    public String getHost() {

        return host;
    }

    public int getPort() {

        return port;
    }

    public boolean isUseSsl() {

        return useSsl;
    }

    public String getUsername() {

        return username;
    }

    public String getPassword() {

        return password;
    }

    public String getFromAddress() {

        return fromAddress;
    }

    public String getFromName() {

        return fromName;
    }

    public static class Builder {

        private boolean enabled;
        private String host;
        private int port;
        private boolean useSsl;
        private String username;
        private String password;
        private String fromAddress;
        private String fromName;

        public Builder withEnabled(final boolean enabled) {

            this.enabled = enabled;
            return this;
        }

        public Builder withHost(final String host) {

            this.host = host;
            return this;
        }

        public Builder withPort(final int port) {

            this.port = port;
            return this;
        }

        public Builder withUseSsl(final boolean useSsl) {

            this.useSsl = useSsl;
            return this;
        }

        public Builder withUsername(final String username) {

            this.username = username;
            return this;
        }

        public Builder withPassword(final String password) {

            this.password = password;
            return this;
        }

        public Builder withFromAddress(final String fromAddress) {

            this.fromAddress = fromAddress;
            return this;
        }

        public Builder withFromName(final String fromName) {

            this.fromName = fromName;
            return this;
        }

        private Builder withProperties(final Properties properties) {

            withEnabled(Boolean.parseBoolean(properties.getProperty("mithril.enabled")));
            withHost(properties.getProperty("mithril.host"));
            withPort(Integer.parseInt(properties.getProperty("mithril.port")));
            withUseSsl(Boolean.parseBoolean(properties.getProperty("mithril.ssl")));
            withUsername(properties.getProperty("mithril.username"));
            withPassword(properties.getProperty("mithril.password"));
            withFromAddress(properties.getProperty("mithril.fromAddress"));
            withFromName(properties.getProperty("mithril.fromName"));

            return this;
        }

        public Builder fromPropertiesFile(final String propertiesFilename) {

            final Properties properties = new Properties();

            final InputStream stream = this.getClass().getClassLoader().getResourceAsStream(propertiesFilename);
            try {
                properties.load(stream);
            } catch (IOException e) {
                throw new RuntimeException(String.format("Unable to load '%s' file for configuration.", propertiesFilename), e);
            }

            return withProperties(properties);
        }

        public MithrilConfig build() {

            Validate.notNull(this.host);
            Validate.isTrue(this.port > 0);
            Validate.isTrue(!Strings.isNullOrEmpty(this.username));
            Validate.isTrue(!Strings.isNullOrEmpty(this.password));
            Validate.isTrue(!Strings.isNullOrEmpty(this.fromAddress));

            return new MithrilConfig(this.enabled, this.host, this.port, this.useSsl, this.username, this.password, this.fromAddress, this.fromName);
        }
    }
}

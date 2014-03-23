package com.samandmoore.mithril;

import static org.apache.commons.lang3.Validate.isTrue;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * @author Sam Moore
 * @since 3/22/14 7:49 PM
 */
public abstract class MailerBase {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final MithrilConfig config;
    private final EmailTemplateEngine emailTemplateEngine;

    private final Map<String, Object> model;
    private final Set<MailAddress> tos;
    private final Set<MailAddress> ccs;
    private final Set<MailAddress> bccs;
    private String subject;

    protected MailerBase() {

        this(MithrilConfig.getDefault(), MithrilEmailTemplateEngineProvider.getDefault());
    }

    protected MailerBase(final EmailTemplateEngine emailTemplateEngine) {

        this(MithrilConfig.getDefault(), emailTemplateEngine);
    }

    protected MailerBase(final MithrilConfig config, final EmailTemplateEngine emailTemplateEngine) {

        this.emailTemplateEngine = emailTemplateEngine;
        this.config = config;
        this.model = Maps.newHashMap();
        this.tos = Sets.newHashSet();
        this.ccs = Sets.newHashSet();
        this.bccs = Sets.newHashSet();
    }

    protected final void to(String address) {

        to(address, "");
    }

    protected final void to(String address, String name) {

        addAddressToCollection(tos, address, name);
    }

    protected final void cc(String address) {

        cc(address, "");
    }

    protected final void cc(String address, String name) {

        addAddressToCollection(ccs, address, name);
    }

    protected final void bcc(String address) {

        bcc(address, "");
    }

    protected final void bcc(String address, String name) {

        addAddressToCollection(bccs, address, name);
    }

    protected final void subject(final String subject, final Object... params) {

        this.subject = !Strings.isNullOrEmpty(subject)
                ? String.format(subject, params)
                : subject;
    }

    protected final void addToModel(final String key, final Object value) {

        this.model.put(key, value);
    }

    private void addAddressToCollection(Set<MailAddress> set, String address, String name) {

        set.add(new MailAddress(address, name));
    }

    protected final Deliverable mail(String template) {

        isTrue(!Strings.isNullOrEmpty(this.subject));
        isTrue(!this.tos.isEmpty());

        HtmlEmail email = buildEmailObject();

        try {
            email.setSubject(this.subject);
            email.setHtmlMsg(emailTemplateEngine.compile(template).apply(this.model));
            email.setCharset(Charsets.UTF_8.toString());
        } catch (EmailException | IOException e) {
            throw new RuntimeException("Unable to create email", e);
        }

        return createDeliverable(email);
    }

    protected Deliverable createDeliverable(final HtmlEmail email) {

        return new SimpleDeliverable(email, DEFAULT_MAIL_ACTION);
    }

    private HtmlEmail buildEmailObject() {

        HtmlEmail email = new HtmlEmail();

        email.setHostName(config.getHost());
        email.setSmtpPort(config.getPort());
        email.setSslSmtpPort(String.valueOf(config.getPort()));
        email.setSSLOnConnect(config.isUseSsl());
        email.setAuthentication(config.getUsername(), config.getPassword());

        try {
            email.setFrom(config.getFromAddress(), config.getFromName());
            //TODO: add support for reply-to

            for (MailAddress to : tos) {
                email.addTo(to.address, to.name);
            }

            for (MailAddress cc : ccs) {
                email.addCc(cc.address, cc.name);
            }

            for (MailAddress bcc : bccs) {
                email.addBcc(bcc.address, bcc.name);
            }
        } catch (EmailException e) {
            throw new IllegalStateException("Unable to build email", e);
        }

        return email;
    }

    private static class MailAddress {
        private final String name;
        private final String address;

        private MailAddress(String address, String name) {

            this.name = name;
            this.address = address;
        }

        @Override
        public boolean equals(Object o) {

            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            final MailAddress that = (MailAddress) o;

            if (!address.equals(that.address))
                return false;
            if (!name.equals(that.name))
                return false;

            return true;
        }

        @Override
        public int hashCode() {

            int result = name.hashCode();
            result = 31 * result + address.hashCode();
            return result;
        }
    }

    private final Predicate<Email> DEFAULT_MAIL_ACTION = new Predicate<Email>() {
        @Override
        public boolean apply(Email email) {

            if (!config.isEnabled()) {
                logger.info("Not sending email because emailing is disabled");

                Emails.print(email);

                return true;
            }

            try {
                email.send();
                return true;
            } catch (EmailException e) {
                throw new RuntimeException("Unable to send email", e);
            }
        }
    };

}

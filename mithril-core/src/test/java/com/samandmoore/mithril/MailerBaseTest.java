package com.samandmoore.mithril;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.mail.internet.InternetAddress;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.HtmlEmail;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

/**
 * @author Sam Moore
 * @since 3/22/14 8:44 PM
 */
public class MailerBaseTest {

    @Before
    public void before() {

        MithrilEmailTemplateEngineProvider.setDefault(FAKE_EMAIL_TEMPLATE_ENGINE);
    }

    @Test
    public void mailerProperlyConfiguresEmail() throws UnsupportedEncodingException {

        final TestMailerBase testMailer = new TestMailerBase() {
            @Override
            protected Deliverable testEmail(final Object foo) {

                to("sam@example.com", "Sam");
                cc("mike@example.com", "Mike");
                bcc("joe@example.com", "Joe");

                subject("Foo Mailer testing");

                addToModel("foo", foo);

                return mail("foos/created");
            }
        };

        final Object input = new Object();

        testMailer.testEmail(input);

        final HtmlEmail lastEmail = testMailer.lastEmail;

        assertNotNull(lastEmail);
        assertEquals("Foo Mailer testing", lastEmail.getSubject());
        assertTrue(Iterables.contains(lastEmail.getToAddresses(), new InternetAddress("sam@example.com", "Sam")));
        assertTrue(Iterables.contains(lastEmail.getCcAddresses(), new InternetAddress("mike@example.com", "Mike")));
        assertTrue(Iterables.contains(lastEmail.getBccAddresses(), new InternetAddress("joe@example.com", "Joe")));
    }

    private static abstract class TestMailerBase extends MailerBase {

        private HtmlEmail lastEmail;

        protected abstract Deliverable testEmail(final Object foo);

        @Override
        protected Deliverable createDeliverable(final HtmlEmail email) {

            lastEmail = email;

            return new SimpleDeliverable(email, Predicates.<Email> alwaysTrue());
        }
    }

    private static final EmailTemplateEngine FAKE_EMAIL_TEMPLATE_ENGINE = new EmailTemplateEngine() {
        @Override
        public EmailTemplate compile(final String templateName) throws IOException {

            return new EmailTemplate() {
                @Override
                public String apply(final Map<String, Object> model) {

                    return "This is not really an email...";
                }
            };
        }
    };
}

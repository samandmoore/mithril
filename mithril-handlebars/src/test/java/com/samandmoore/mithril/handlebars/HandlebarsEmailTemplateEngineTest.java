package com.samandmoore.mithril.handlebars;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.apache.commons.mail.HtmlEmail;
import org.junit.Before;
import org.junit.Test;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.samandmoore.mithril.Deliverable;
import com.samandmoore.mithril.Emails;
import com.samandmoore.mithril.MailerBase;
import com.samandmoore.mithril.MithrilEmailTemplateEngineProvider;

/**
 * @author Sam Moore
 * @since 3/22/14 11:05 PM
 */
public class HandlebarsEmailTemplateEngineTest extends AbstractTestBase {

    private Handlebars handlebars;

    @Before
    public void before() {

        handlebars = new Handlebars(new ClassPathTemplateLoader("mailer", ".hbs.html"));
        MithrilEmailTemplateEngineProvider.setDefault(new HandlebarsEmailTemplateEngine(handlebars));
    }

    @Test
    public void createsAnHtmlEmail() {

        final TestMailer testMailer = new TestMailer();

        final TestModel testModel = new TestModel("Sam Moore", new BigDecimal("199.99"));

        testMailer.testEmail(testModel);

        final String messageBody = Emails.getMessageBody(testMailer.lastEmail);

        assertEquals("text/plain: \n" +
                "<html>\n" +
                "<body>\n" +
                "<h1>Test Email Title</h1>\n" +
                "\n" +
                "<p>Some Name Sam Moore</p>\n" +
                "\n" +
                "<p>Some Numbers 199.99</p>\n" +
                "\n" +
                "</body>\n" +
                "</html>", messageBody);
    }

    private static class TestModel {
        private final String name;
        private final BigDecimal number;

        private TestModel(final String name, final BigDecimal number) {

            this.name = name;
            this.number = number;
        }

        public String getName() {

            return name;
        }

        public BigDecimal getNumber() {

            return number;
        }
    }

    private static class TestMailer extends MailerBase {

        private HtmlEmail lastEmail;

        @Override
        protected Deliverable createDeliverable(final HtmlEmail email) {

            lastEmail = email;
            return super.createDeliverable(email);
        }

        public Deliverable testEmail(final Object foo) {

            to("sam@example.com", "Sam");
            cc("mike@example.com", "Mike");
            bcc("joe@example.com", "Joe");

            subject("Foo Mailer testing");

            addToModel("foo", foo);

            return mail("foos/created");
        }
    };
}

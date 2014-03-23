package com.samandmoore.mithril;

/**
 * @author Sam Moore
 * @since 3/22/14 8:39 PM
 */
public class FooMailer extends MailerBase {

    public Deliverable fooCreated(final Object foo) {

        to("sam@betterment.com", "Sam Moore");

        subject("Foo Mailer testing");

        addToModel("foo", foo);

        return mail("foos/created");
    }
}

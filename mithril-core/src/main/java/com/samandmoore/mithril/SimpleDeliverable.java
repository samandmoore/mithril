package com.samandmoore.mithril;

import org.apache.commons.mail.Email;

import com.google.common.base.Predicate;

/**
 * @author Sam Moore
 * @since 3/22/14 7:47 PM
 */
public final class SimpleDeliverable implements Deliverable {

    private final Predicate<Email> mailAction;

    private final Email email;

    public SimpleDeliverable(final Email email, final Predicate<Email> mailAction) {

        this.email = email;
        this.mailAction = mailAction;
    }

    @Override
    public void deliver() {

        this.mailAction.apply(this.email);
    }
}

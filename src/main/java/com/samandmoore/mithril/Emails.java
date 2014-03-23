package com.samandmoore.mithril;

import java.io.IOException;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;

import com.google.common.base.Functions;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;

/**
 * @author Sam Moore
 * @since 3/22/14 7:54 PM
 */
final class Emails {

    //private static final Logger logger = LoggerFactory.getLogger(Emails.class);

    private Emails() {

        // nope!
    }

    public static void print(Email email) {

        final StringBuilder content = new StringBuilder();

        final Joiner addressJoiner = Joiner.on('|').skipNulls();

        content.append("Email Contents:\n");

        content.append("\n\tFrom: ").append(email.getFromAddress());
        content.append("\n\tReplyTo: ").append(addressJoiner.join(Iterables.transform(email.getReplyToAddresses(), Functions.toStringFunction())));
        content.append("\n\tTo: ").append(addressJoiner.join(Iterables.transform(email.getToAddresses(), Functions.toStringFunction())));
        content.append("\n\tCC: ").append(addressJoiner.join(Iterables.transform(email.getCcAddresses(), Functions.toStringFunction())));
        content.append("\n\tBCC: ").append(addressJoiner.join(Iterables.transform(email.getBccAddresses(), Functions.toStringFunction())));

        content.append("\n\tSubject: ").append(email.getSubject());
        content.append("\n\tBody: \n").append(getMessageBody(email));

        content.append("\n");

        //logger.info(content.toString());
    }

    private static String getMessageBody(Email email) {

        String body = "";
        try {
            if (email.getMimeMessage() == null) {
                email.buildMimeMessage();
            }
            body = getContent(email.getMimeMessage());
        } catch (IOException | MessagingException | EmailException e) {
            //logger.error("unable to read email message body", e);
        }

        return body;
    }

    private static String getContent(Part message) throws MessagingException,
            IOException {

        if (message.getContent() instanceof String) {
            return message.getContentType() + ": \n" + message.getContent();
        } else if (message.getContent() != null && message.getContent() instanceof Multipart) {
            Multipart part = (Multipart) message.getContent();
            String text = "";
            for (int i = 0; i < part.getCount(); i++) {
                BodyPart bodyPart = part.getBodyPart(i);
                if (!Message.ATTACHMENT.equals(bodyPart.getDisposition())) {
                    text += getContent(bodyPart);
                } else {
                    text += "attachment: " +
                            "\n\t\t name: " + (Strings.isNullOrEmpty(bodyPart.getFileName()) ? "none" : bodyPart.getFileName()) +
                            "\n\t\t disposition: " + bodyPart.getDisposition() +
                            "\n\t\t description: " + (Strings.isNullOrEmpty(bodyPart.getDescription()) ? "none" : bodyPart.getDescription());
                }
            }
            return text;
        }
        if (message.getContent() != null && message.getContent() instanceof Part) {
            if (!Message.ATTACHMENT.equals(message.getDisposition())) {
                return getContent((Part) message.getContent());
            } else {
                return "attachment: " +
                        "\n\t\t name: " + (Strings.isNullOrEmpty(message.getFileName()) ? "none" : message.getFileName()) +
                        "\n\t\t disposition: " + message.getDisposition() +
                        "\n\t\t description: " + (Strings.isNullOrEmpty(message.getDescription()) ? "none" : message.getDescription());
            }
        }

        return "";
    }
}

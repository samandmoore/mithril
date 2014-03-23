# Mithril

### send emails in java, ActiveMailer style

#### create a simple mailer class like this:
```
public class UserMailer extends MailerBase {

    public Deliverable welcome(User user) {

        addToModel("user", user);

        to(user.getEmail(), user.getName());
        subject("Welcome aboard, %s", user.getName());

        return mail("users/welcome");
    }
}
```

#### drop an html template (using the templating engine of your choice) into:
```
src/main/resources/mailer
```

#### wire-up a mithril.properties file on your classpath
```
mithril.enabled: true
mithril.host: smtp.gmail.com
mithril.port: 465
mithril.ssl: true
mithril.username: username
mithril.password: password
mithril.fromAddress: foo@example.org
mithril.fromName: Mr. Foo
```
#### or tell Mithril where to find those properties and pass it into your mailer
```
final MithrilConfig config = new MithrilConfig.Builder().fromPropertiesFile("my-cool-props-file.properties").build();
```

#### tell Mithril what template engine you want to use
##### you can write your own -- by implementing two simple interfaces -- or you can use our handlebars engine powered by [handlebars.java](https://github.com/jknack/handlebars.java).
```
handlebars = new Handlebars(new ClassPathTemplateLoader("mailer", ".hbs.html"));
MithrilEmailTemplateEngineProvider.setDefault(new HandlebarsEmailTemplateEngine(handlebars));
```

#### then consume that simple mailer class like this:
```
public class EmailSending {

    public void sendAnEmail() {
        final User aUser = new User("sam@example.org", "Sam");

        new UserMailer().welcome(aUser).deliver();
    }
}
```

package br.com.jkavdev.fullcycle.subscription.domain.account;

import br.com.jkavdev.fullcycle.subscription.domain.person.Address;
import br.com.jkavdev.fullcycle.subscription.domain.person.Document;
import br.com.jkavdev.fullcycle.subscription.domain.person.Email;
import br.com.jkavdev.fullcycle.subscription.domain.person.Name;

public sealed interface AccountCommand {

    record ChangeProfileCommand(Name aName, Address aAddress) implements AccountCommand {

    }

    record ChangeEmailCommand(Email anEmail) implements AccountCommand {

    }

    record ChangeDocumentCommand(Document aDocument) implements AccountCommand {

    }

}

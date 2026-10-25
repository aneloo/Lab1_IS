package ru.itmo.is.lab1.push;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.TransactionPhase;
import jakarta.faces.push.Push;
import jakarta.faces.push.PushContext;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;
import ru.itmo.is.lab1.event.ChangeEvent;

import java.util.logging.Logger;

@ApplicationScoped
public class ChangeNotifier {

    public static final String CHANNEL = "creatures";

    private static final Logger LOG = Logger.getLogger(ChangeNotifier.class.getName());

    @Inject
    @Push(channel = CHANNEL)
    private PushContext channel;

    public void onChange(@Observes(during = TransactionPhase.AFTER_SUCCESS) ChangeEvent event) {
        JsonObjectBuilder json = Json.createObjectBuilder()
                .add("type", event.type().name())
                .add("actor", event.actor() == null ? "" : event.actor());
        if (event.objectId() != null) {
            json.add("id", event.objectId());
        }
        if (event.secondId() != null) {
            json.add("secondId", event.secondId());
        }
        if (event.affected() != null) {
            json.add("affected", event.affected());
        }
        if (event.name() != null) {
            json.add("name", event.name());
        }
        try {
            channel.send(json.build().toString());
        } catch (RuntimeException e) {
            LOG.warning("Не удалось отправить уведомление: " + e);
        }
    }
}

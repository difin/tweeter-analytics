package actors;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import actors.TwitterSearchActorProtocol.Refresh;
import actors.TwitterSearchSchedulerActorProtocol.RefreshAll;
import actors.TwitterSearchSchedulerActorProtocol.Register;

/*
 * @author Mayank Acharya
 * @version 1.0.0
 */

public class TwitterSearchSchedulerActor extends AbstractActor {

    public final Set<ActorRef> twitterSearchActors;

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public void preStart() throws Exception {
        log.info("TwitterSearchSchedulerActor {}-{} started at " + LocalTime.now());
    }

    @Override
    public void postStop() throws Exception {
        log.info("TwitterSearchSchedulerActor {}-{} stopped at " + LocalTime.now());
    }

    public static Props props() {
        return Props.create(TwitterSearchSchedulerActor.class);
    }

    public TwitterSearchSchedulerActor() {
        twitterSearchActors = new HashSet<>();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(RefreshAll.class, p -> {
                    for (ActorRef actorRef : twitterSearchActors) {
                        actorRef.tell(new Refresh(), getSelf());

                    }
                })
                .match(Register.class, p -> {
                    twitterSearchActors.add(p.actorRef);
                    log.debug("new registerd Actor = {}", p.actorRef);
                    log.debug("actors = {}", twitterSearchActors);
                })
                .build();
    }
}
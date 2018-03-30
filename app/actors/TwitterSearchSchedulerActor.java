package actors;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import actors.TwitterSearchSchedulerActorProtocol.Refresh;
import actors.TwitterSearchSchedulerActorProtocol.Register;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Tweet;
import play.libs.Json;

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
//                .matchAny(p -> {
//                    //twitterSearchActors.add(p);
//                    log.debug("newActor = {}", p);
//                })
                .match(Refresh.class, p -> {
                    for (ActorRef actorRef : twitterSearchActors) {
                        actorRef.tell(new TwitterSearchActorProtocol.Refresh(), getSelf());

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
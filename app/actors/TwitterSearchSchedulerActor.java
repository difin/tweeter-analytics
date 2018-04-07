package actors;

import actors.TwitterSearchActorProtocol.Refresh;
import actors.TwitterSearchSchedulerActorProtocol.RefreshAll;
import actors.TwitterSearchSchedulerActorProtocol.Register;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Keeps a list of Actors to trigger by schedule
 * @author Mayank Acharya
 * @version 1.0.0
 */

public class TwitterSearchSchedulerActor extends AbstractActor {

    /**
     * A list of Actors to notify
     */
    private final Set<ActorRef> twitterSearchActors;

    /**
     * Logger instance
     */
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    /**
     * Creates a new TwitterSearchSchedulerActor
     */
    public TwitterSearchSchedulerActor() {
        twitterSearchActors = new HashSet<>();
    }

    /**
     * Configure props to create TwitterSearchSchedulerActor
     */
    public static Props props() {
        return Props.create(TwitterSearchSchedulerActor.class);
    }

    /**
     * Logs the start time
     */
    @Override
    public void preStart() {
        log.info("TwitterSearchSchedulerActor {}-{} started at " + LocalTime.now());
    }

    /**
     * Logs the stop time of the
     */
    @Override
    public void postStop() {
        log.info("TwitterSearchSchedulerActor {}-{} stopped at " + LocalTime.now());
    }

    /**
     * Handles refresh and search messages
     * Register message - register the Actor in the list Actors to notify
     * RefreshAll message - sends Refresh message to all Actors in a list
     */
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
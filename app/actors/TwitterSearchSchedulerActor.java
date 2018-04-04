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

	/**
	 * TwitterSearchSchedulerActor is to store reference of TwitterSearchActor
	 * created by keyword search call in to the List.
	 * A scheduler actor that will be executed 
	 * every X milliseconds to periodically refresh 
	 * twitter results based on previously 
	 * searched keywords on UI.
	 * <p>
	 * @author Mayank Acharya
	 * <p>
	 * @version 1.0.0
	 */

public class TwitterSearchSchedulerActor extends AbstractActor {

    public final Set<ActorRef> twitterSearchActors;

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    /**
 	 * preStart method to log the start time of the 
 	 * TwitterSearchSchedulerActor 
     */
    
    @Override
    public void preStart() throws Exception {
        log.info("TwitterSearchSchedulerActor {}-{} started at " + LocalTime.now());
    }
    
    /**
   	 * postStop method to log the stop time of the 
   	 * TwitterSearchSchedulerActor 
     */

    @Override
    public void postStop() throws Exception {
        log.info("TwitterSearchSchedulerActor {}-{} stopped at " + LocalTime.now());
    }
    
    /**
 	 * Configure props to create TwitterSearchSchedulerActor
     */

    public static Props props() {
        return Props.create(TwitterSearchSchedulerActor.class);
    }
    
    /**
	 * Creates a new TwitterSearchSchedulerActor
     */

    public TwitterSearchSchedulerActor() {
        twitterSearchActors = new HashSet<>();
    }
    
    /**
   	 * Implementation of method of abstract actor class to define initial 
   	 * receive behavior of TwitterSearchSchedulerActor
   	 * <p>
   	 * Uses RefreshAll and Register method of TwitterSearchSchedulerActorProtocol
   	 * to retrieve TwitterSearchActor reference from the list one by one
   	 * and call each of them to retrieve result and display on UI again.
   	 * <p>
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
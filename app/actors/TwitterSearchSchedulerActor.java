package actors;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Date;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/*
 * @author Mayank Acharya
 * @version 1.0.0
 */

public class TwitterSearchSchedulerActor extends AbstractActor {
	
	//logging  mechanism.
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(),this);
	
	//Start method.
	@Override
	public void preStart() throws Exception {
		// TODO Auto-generated method stub
		log.info("TwitterSearchSchedulerActor {}-{} started at " + LocalTime.now());
	}
	
	
	// Stop method.
	@Override
	public void postStop() throws Exception {
		// TODO Auto-generated method stub
		log.info("TwitterSearchSchedulerActor {}-{} stopped at " + LocalTime.now());
	}
	
	// Props method.
	public static Props props() {
		return Props.create(TwitterSearchSchedulerActor.class);
	}
	
	
	// main createReceive method.
	@Override
	public Receive createReceive() {
		// TODO Auto-generated method stub
		// Trigger available in Controller will trigger this actor and calls createReceive method.
		return receiveBuilder().matchEquals("CallFromController",p->{
			// Logic of call static TwitterSearchActor list and fetch element one by one and pass it to refresh.
		}).build();
	}

}
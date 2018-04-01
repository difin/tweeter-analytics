package services;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import com.google.inject.Inject;

import actors.TwitterSearchSchedulerActor;
import actors.TwitterSearchSchedulerActorProtocol.RefreshAll;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Scheduler;
import scala.concurrent.ExecutionContext;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

/**
 * 
 * @author Mayank Acharya
 * @version 1.0.0
 */

@Singleton
public class PushSchedulingService {
	
	private final ActorSystem actorSystem;
	private ActorRef schedulerActorRef;
	private Scheduler scheduler;
	
	@Inject
	public PushSchedulingService(ActorSystem actorSystem, SchedulerHolder schedulerHolder) {
		this.actorSystem = actorSystem;
		
		if (schedulerHolder.value != null)
			scheduler = schedulerHolder.value;
		else
			scheduler = actorSystem.scheduler();
	}
	
	public void startScheduler() {
        FiniteDuration initialDelay = Duration.create(0, TimeUnit.SECONDS);
        FiniteDuration interval = Duration.create(2, TimeUnit.SECONDS);
        schedulerActorRef = actorSystem.actorOf(TwitterSearchSchedulerActor.props(), "Scheduler");
        RefreshAll message = new RefreshAll();
        ExecutionContext executor = actorSystem.dispatcher();
        scheduler.schedule(initialDelay, interval, schedulerActorRef, message, executor, ActorRef.noSender());
	}
	
	public ActorRef getSchedulerActorRef() {
		return schedulerActorRef;
	}
	
    static class SchedulerHolder {
    	
        @Inject(optional=true) 
        Scheduler value;
      } 
}

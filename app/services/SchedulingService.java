package services;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

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
public class SchedulingService {
	
	private final ActorSystem actorSystem;
	
	@Inject
	public SchedulingService(ActorSystem actorSystem) {
		this.actorSystem = actorSystem;
	}
	
	public void startScheduler(Scheduler scheduler, ActorRef schedulerActorRef) {
        FiniteDuration initialDelay = Duration.create(0, TimeUnit.SECONDS);
        FiniteDuration interval = Duration.create(2, TimeUnit.SECONDS);
        RefreshAll message = new RefreshAll();
        ExecutionContext executor = actorSystem.dispatcher();
        scheduler.schedule(initialDelay, interval, schedulerActorRef, message, executor, ActorRef.noSender());
	}
}

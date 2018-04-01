package services;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import actors.TwitterSearchSchedulerActor;
import actors.TwitterSearchSchedulerActorProtocol.RefreshAll;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import scala.concurrent.ExecutionContext;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

@Singleton
public class PushSchedulingService {
	
	private final ActorSystem actorSystem;
	private ActorRef schedulerActorRef;
	
	@Inject
	public PushSchedulingService(ActorSystem actorSystem){
		this.actorSystem = actorSystem;
	}
	
	public void startScheduler() {
        FiniteDuration initialDelay = Duration.create(0, TimeUnit.SECONDS);
        FiniteDuration interval = Duration.create(2, TimeUnit.SECONDS);
        schedulerActorRef = this.actorSystem.actorOf(TwitterSearchSchedulerActor.props(), "Scheduler");
        RefreshAll message = new RefreshAll();
        ExecutionContext executor = actorSystem.dispatcher();
        this.actorSystem.scheduler().schedule(initialDelay, interval, schedulerActorRef, message, executor, ActorRef.noSender());
	}
	
	public ActorRef getSchedulerActorRef() {
		return schedulerActorRef;
	}
}

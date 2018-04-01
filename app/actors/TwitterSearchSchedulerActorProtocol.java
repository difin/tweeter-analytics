package actors;

import akka.actor.ActorRef;

public class TwitterSearchSchedulerActorProtocol {
	
	public static class Register{
		
		public final ActorRef actorRef;

		public Register(ActorRef actorRef) {
			this.actorRef = actorRef;
		}
	}
	
	public static class RefreshAll{
		public RefreshAll() {
			
		}
	}
}

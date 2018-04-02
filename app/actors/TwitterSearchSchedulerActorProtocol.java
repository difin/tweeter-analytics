package actors;

import akka.actor.ActorRef;

	/**
	 * TwitterSearchSchedulerActorProtocol to pass messages to TwitterSearchSchedulerActor
	 * <p>
	 * @author Mayank Acharya
	 * <p>
	 * @version 1.0.0
	 */

public class TwitterSearchSchedulerActorProtocol {
	
	/**
	 * TwitterSearchActorProtocol.Register for registering 
	 * the actors references
	 *<p>
	 */
	
	public static class Register{
		
		public final ActorRef actorRef;
		
		/**
		 * Registers actor references
		 *<p>
		 * @param actorRef		the reference to the actor that will be registered
		 */
		
		public Register(ActorRef actorRef) {
			this.actorRef = actorRef;
		}
		
	}
	
	/**
	 * RefreshAll class to periodically refresh all previously 
	 * searched keywords on the UI  
	 */
	
	public static class RefreshAll{
		public RefreshAll() {
			
		}
	}
}

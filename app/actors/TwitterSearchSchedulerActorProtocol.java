package actors;

import akka.actor.ActorRef;

/**
 * Defines message protocol for a TwitterSearchSchedulerActor
 * @author Dmitriy Fingerman
 * @version 1.0.0
 */

public class TwitterSearchSchedulerActorProtocol {

    /**
     * A message caries an Actor to register in TwitterSearchActor
     * @author Dmitriy Fingerman
     * @version 1.0.0
     */
    public static class Register {
        /**
         * Reference to the actor
         */
        public final ActorRef actorRef;

        /**
         * Creates a message
         * @param actorRef the reference to the Actor
         */
        public Register(ActorRef actorRef) {
            this.actorRef = actorRef;
        }
    }

    /**
     * A message to trigger refresh of all Actors
     * @author Dmitriy Fingerman
     * @version 1.0.0
     */
    public static class RefreshAll {
        /**
         * Creates a message
         */
        public RefreshAll() {
        }
    }
}

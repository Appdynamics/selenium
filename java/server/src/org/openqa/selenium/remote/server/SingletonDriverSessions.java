package org.openqa.selenium.remote.server;

import org.openqa.selenium.AppdynamicsCapability;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.Command;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.remote.server.handler.DeleteSession;
import org.openqa.selenium.remote.server.rest.RestishHandler;

import java.util.logging.Logger;

/**
 * Created by olivier.crameri on 2/26/16.
 */
public class SingletonDriverSessions extends DefaultDriverSessions {
    private static final Logger logger = Logger.getLogger(SingletonDriverSessions.class.getName());
    private volatile SessionId uniqueSessionId = null;

    public SessionId newSession(Capabilities desiredCapabilities) throws Exception {
        synchronized (this) {
            AppdynamicsCapability appdynamicsCapability = AppdynamicsCapability.extractFrom(desiredCapabilities);
            if (uniqueSessionId == null || appdynamicsCapability.flushSessions()) {
              flushAllExistingSessions();
              logger.info("NewSession: Creating new Session");
              uniqueSessionId = super.newSession(desiredCapabilities);
            }
        }
        logger.info("Session ID is " + uniqueSessionId.toString());
        return uniqueSessionId;
    }

    public void deleteSession(SessionId sessionId) {
        if (uniqueSessionId != null && uniqueSessionId.equals(sessionId))
            uniqueSessionId = null;
        super.deleteSession(sessionId);
    }

    private void flushAllExistingSessions() {
      logger.info("NewSession: Flushing all sessions");
      for (SessionId sessionId : super.getSessions()) {
        DeleteSession deleteSession = new DeleteSession(super.get(sessionId));
        try {
            deleteSession.call();
        } catch (Exception e) {
            logger.info("Quiting the session failed due to " + e.toString());
        }
        super.deleteSession(sessionId);
      }
    }
}

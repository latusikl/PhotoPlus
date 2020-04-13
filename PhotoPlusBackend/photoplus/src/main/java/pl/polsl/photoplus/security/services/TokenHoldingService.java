package pl.polsl.photoplus.security.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@Slf4j
public class TokenHoldingService
{
    private final ConcurrentMap<String,Date> loggedUsersTokens = new ConcurrentHashMap<>();

    public void addTokenOnLogin(final String token, final Date date)
    {
        loggedUsersTokens.put(token, date);
    }

    public boolean isNotLoggedOut(final String token)
    {
        return loggedUsersTokens.containsKey(token);
    }

    public void removeTokenOnLogout(final String token)
    {
        loggedUsersTokens.remove(token);
    }

    @Scheduled(fixedRate = 300000) //5 minutes
    private void removeOutdatedAndNotLoggedOut()
    {
        log.info("Starting scheduled token job removal");
        loggedUsersTokens.entrySet()
                .parallelStream()
                .filter(stringDateEntry -> stringDateEntry.getValue()
                        .compareTo(new Date(System.currentTimeMillis())) < 0)
                .forEach(stringDateEntry -> loggedUsersTokens.remove(stringDateEntry.getKey()));
        log.info("Finished scheduled token job removal");
    }

}

package com.pwang.kings.resources;

import com.pwang.kings.api.BoutService;
import com.pwang.kings.db.daos.BoutDao;
import com.pwang.kings.objects.action.Bout;
import com.pwang.kings.objects.action.ImmutableBout;
import com.pwang.kings.objects.model.KingsUser;
import org.apache.log4j.Logger;
import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException;

import javax.ws.rs.core.Response;

/**
 * @author pwang on 12/28/17.
 */
public final class BoutResource implements BoutService {

    private static Logger LOGGER = Logger.getLogger(BoutResource.class);

    private final BoutDao boutDao;

    public BoutResource(
            BoutDao boutDao) {

        this.boutDao = boutDao;
    }

    @Override
    public Response submit(KingsUser kingsUser, Long categoryId, long winnerContestantId, long loserContestantId) {
        Bout bout = ImmutableBout.builder()
                .categoryId(categoryId)
                .winnerContestantId(winnerContestantId)
                .loserContestantId(loserContestantId)
                .kingsUserId(kingsUser.getKingsUserId())
                .build();

        try {
            boutDao.create(bout);
        } catch (UnableToExecuteStatementException e) {
            LOGGER.error("could not create bout", e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return Response.ok().build();
    }

}

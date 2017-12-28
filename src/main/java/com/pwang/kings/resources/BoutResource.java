package com.pwang.kings.resources;

import com.pwang.kings.api.BoutService;
import com.pwang.kings.db.daos.BoutDao;
import com.pwang.kings.objects.action.Bout;
import com.pwang.kings.objects.action.ImmutableBout;
import com.pwang.kings.objects.model.KingsUser;

/**
 * @author pwang on 12/28/17.
 */
public final class BoutResource implements BoutService {

    private final BoutDao boutDao;

    public BoutResource(
            BoutDao boutDao) {

        this.boutDao = boutDao;
    }

    @Override
    public void submit(KingsUser kingsUser, long winnerContestantId, long loserContestantId) {

        Bout bout = ImmutableBout.builder()
                .winnerContestantId(winnerContestantId)
                .loserContestantId(loserContestantId)
                .kingsUserId(kingsUser.getKingsUserId())
                .build();

        boutDao.create(bout);
    }
}

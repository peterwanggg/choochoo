package com.pwang.kings.db.daos;

import com.pwang.kings.objects.model.Contestant;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.SingleValueResult;

import java.util.List;
import java.util.Optional;

/**
 * @author pwang on 12/27/17.
 */
public interface ContestantDao {

    @GetGeneratedKeys
    @SqlUpdate(
            "INSERT INTO common.contestant "
                    + "(category_id, contestant_name, image_url, api_provider_type, api_provider_id) VALUES "
                    + "(:contestant.categoryId, :contestant.contestantName, :imageUrl, :contestant.apiProviderType, :contestant.apiProviderId) "
                    + "ON CONFLICT ON CONSTRAINT contestant_api_key DO NOTHING"
    )
    Long create(@BindBean("contestant") Contestant contestant, @Bind("imageUrl") String imageUrl);

    @SingleValueResult(Contestant.class)
    @SqlQuery(
            "SELECT * FROM common.contestant WHERE api_provider_type = :api_provider_type AND api_provider_id = :api_provider_id"
    )
    Optional<Contestant> getByApiId(@Bind("api_provider_type") String apiProviderType, @Bind("api_provider_id") String apiProviderId);

    @SingleValueResult(Contestant.class)
    @SqlQuery(
            "SELECT * FROM common.contestant WHERE contestant_id = :contestant_id"
    )
    Optional<Contestant> getById(@Bind("contestant_id") Long contestantId);

    // don't give opponents chalenger_contestant_id has faced before
    @SqlQuery(
            "SELECT * FROM common.contestant WHERE " +
                    " category_id = :category_id AND " +
                    " contestant_id != :challenger_contestant_id AND" +
                    " contestant_id NOT IN " +
                    " (SELECT winner_contestant_id FROM common.bout WHERE " +
                    "    loser_contestant_id = :challenger_contestant_id AND " +
                    "    kings_user_id = :kings_user_id AND " +
                    "    category_id = :category_id UNION " +
                    "  SELECT loser_contestant_id FROM common.bout WHERE " +
                    "    winner_contestant_id = :challenger_contestant_id AND " +
                    "    kings_user_id = :kings_user_id AND " +
                    "    category_id = :category_id) " +
                    "    order by contestant_id asc limit :limit offset :offset"
    )
    List<Contestant> getNewChallengersForUser(
            @Bind("kings_user_id") Long kingsUserId,
            @Bind("category_id") Long categoryId,
            @Bind("challenger_contestant_id") Long challengerContestantId,
            @Bind("limit") Integer limit,
            @Bind("offset") Integer offset);

    // TODO: this is not quite right
    @SqlQuery(
            "SELECT * FROM common.contestant WHERE " +
                    " category_id = :category_id AND " +
                    " contestant_id NOT IN " +
                    " (SELECT winner_contestant_id FROM common.bout WHERE " +
                    "    kings_user_id = :kings_user_id AND " +
                    "    category_id = :category_id " +
                    "  UNION " +
                    "  SELECT loser_contestant_id FROM common.bout WHERE " +
                    "    kings_user_id = :kings_user_id AND " +
                    "    category_id = :category_id) " +
                    "    order by contestant_id asc limit :limit offset :offset"
    )
    List<Contestant> getNewContestantsForUser(
            @Bind("kings_user_id") Long kingsUserId,
            @Bind("category_id") Long categoryId,
            @Bind("limit") Integer limit,
            @Bind("offset") Integer offset);


}

package com.pwang.kings.matchers;

import com.pwang.kings.categories.CategoryTypeManager;
import com.pwang.kings.objects.model.Contestant;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Optional;

/**
 * @author pwang on 1/11/18.
 */
public interface ContestantMatcher {

    Optional<Contestant> findNextMatch(
            Long kingsUserId,
            Contestant contestant,
            CategoryTypeManager categoryTypeManager);

    Optional<Pair<Contestant, Contestant>> findNextBout(Long kingsUserId, Long categoryId, CategoryTypeManager categoryTypeManager);
}

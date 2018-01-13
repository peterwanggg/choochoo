package com.pwang.kings.matchers;

import com.pwang.kings.categories.CategoryTypeManager;
import com.pwang.kings.objects.action.Bout;
import com.pwang.kings.objects.model.Contestant;

import java.util.Optional;

/**
 * @author pwang on 1/11/18.
 */
public interface ContestantMatcher {

    Optional<Contestant> findNextMatch(
            Long kingsUserId,
            Contestant contestant,
            CategoryTypeManager categoryTypeManager);

    Bout findNextBout(Long kingsUserId, Long categoryId);
}

package com.pwang.kings.categories;

import com.pwang.kings.objects.model.Contestant;
import com.pwang.kings.objects.model.Location;
import com.pwang.kings.objects.model.User;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * @author pwang on 12/26/17.
 */
public interface CategoryManager {
    //
    Optional<Location> getLocation(double lat, double lon) throws IOException;

    List<Contestant> getContestants(User user, Location location, long categoryId);


//    List<Contestant> getContestants(User user, long categoryId, double lat, double lon);

//


}

import java.util.ArrayList;
import java.util.List;

public class FollowerPool {
  private List<FollowerThread> followers;

  public FollowerPool() {
    followers = new ArrayList<>();
    // Initialize the followers collection with FollowerThread instances
    // For example:
    // followers.add(new FollowerThread());
    // followers.add(new FollowerThread());
    // ...
  }

  public FollowerThread getAvailableFollower() {
    for (FollowerThread follower : followers) {
      if (follower.isAvailable()) {
        return follower;
      }
    }
    return null;
  }
}
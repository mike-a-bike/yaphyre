package yaphyre.util.scenereaders.utils;

import org.joox.Match;

public interface EntityHelper<T> {

  public T decodeEntity(Match entityMatch);

}

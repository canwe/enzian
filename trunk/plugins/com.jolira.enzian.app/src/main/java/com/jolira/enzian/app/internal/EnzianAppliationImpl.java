package com.jolira.enzian.app.internal;

import org.apache.wicket.Page;

import com.jolira.enzian.app.DefaultHomePage;
import com.jolira.enzian.app.EnzianApplication;

/**
 * @author Joachim Kainz
 *
 */
public final class EnzianAppliationImpl extends EnzianApplication {
  /**
   * @see EnzianApplication#getHomePage()
   */
  @Override
  public Class<? extends Page> getHomePage() {
    return DefaultHomePage.class;
  }

}

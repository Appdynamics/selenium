package org.openqa.selenium;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * Created by shashwat.srivastava on 5/30/17.
 */
public class AppdynamicsCapability {
  /**
   * AppDynamics extensions
   *
   * This capability forwards all commands to a specified end point before executing them.
   *
   * This capability affects the remote webdriver server, not the clients.
   *
   * @author Renault John Lecoultre <renault.lecoultre@appdynamics.com>
   * @since 21/10/16
   * Copyright 2016 AppDynamics inc. All rights reserved
   */
  public static  String COMMAND_WEBHOOK = "commandWebhook";
  /**
   * This capability sets the K9 output directory.
   */
  public static String OUTPUT_DIR = "outputDir";
  /**
   * This capability sets the K9 test id
   */
  public static String TEST_ID = "testId";

  private String outputDir;
  private String testID;
  private String commandWebhook;
  /**
   * AppDynamics extension
   *
   * This capability will make it possible to set environment variables for
   * the K9 process
   */
  public static final String APPDYNAMICS_CAPABILITIES = "appdynamicsCapabilities";

  public AppdynamicsCapability(Map<String, String> map) {
    if ( map.containsKey(OUTPUT_DIR) && !Strings.isNullOrEmpty(map.get(OUTPUT_DIR))) {
      this.outputDir = map.get(OUTPUT_DIR);
    }
    if (map.containsKey(TEST_ID) && !Strings.isNullOrEmpty(map.get(TEST_ID))) {
      this.testID = map.get(TEST_ID);
    }
    if (map.containsKey(COMMAND_WEBHOOK) && !Strings.isNullOrEmpty(map.get(COMMAND_WEBHOOK))) {
      this.commandWebhook = map.get(COMMAND_WEBHOOK);
    }
  }

  public AppdynamicsCapability() {
    this.commandWebhook = "http://localhost:8888/v1/webdriver/action";
  }

  public String getTestID() {
    return testID;
  }

  public String getOutputDir() {
    return outputDir;
  }

  public String getCommandWebhook() {
    return this.commandWebhook;
  }

  public ImmutableMap<String, String> getK9Environment() {
    ImmutableMap.Builder<String, String> mapBuilder = ImmutableMap.builder();
    if (outputDir != null) {
      mapBuilder.put("K9_OUTPUT_DIR", outputDir);
    }
    if (testID != null) {
      mapBuilder.put("K9_TEST_ID", testID);
    }
    return mapBuilder.build();
  }

  public static AppdynamicsCapability extractFrom(Capabilities capabilities) {
    Object rawAppdynamicsCapability = capabilities.getCapability(APPDYNAMICS_CAPABILITIES);
    AppdynamicsCapability appdynamicsCapability = new AppdynamicsCapability();
    if (rawAppdynamicsCapability != null) {
      if (rawAppdynamicsCapability instanceof AppdynamicsCapability) {
        appdynamicsCapability = (AppdynamicsCapability) rawAppdynamicsCapability;
      } else if (rawAppdynamicsCapability instanceof Map) {
        appdynamicsCapability = new AppdynamicsCapability((Map<String, String>) rawAppdynamicsCapability);
      } else {
        throw new RuntimeException(
          String.format("Failed to parse appdynamics capabilities correctly. List of capabilities is %s", capabilities.toString()));
      }
    }
    return appdynamicsCapability;
  }
}

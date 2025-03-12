package com.qa.utils;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServerHasNotBeenStartedLocallyException;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

import java.io.File;
import java.util.HashMap;

public class ServerManager {
    private static ThreadLocal<AppiumDriverLocalService> server = new ThreadLocal<>();
    TestUtils utils = new TestUtils();

    public AppiumDriverLocalService getServer() {
        return server.get();
    }

    public void startServer() {
        utils.log().info("Starting Appium server on Ubuntu");
        AppiumDriverLocalService server = UbuntuGetAppiumService();
        server.start();
        if (server == null || !server.isRunning()) {
            utils.log().fatal("Appium server not started. ABORT!!!");
            throw new AppiumServerHasNotBeenStartedLocallyException("Appium server not started. ABORT!!!");
        }
        server.clearOutPutStreams(); // Comment this if you want to see server logs in the console
        this.server.set(server);
        utils.log().info("Appium server started");
    }

    public AppiumDriverLocalService getAppiumServerDefault() {
        return AppiumDriverLocalService.buildDefaultService();
    }

    public AppiumDriverLocalService UbuntuGetAppiumService() {
        GlobalParams params = new GlobalParams();
        HashMap<String, String> environment = new HashMap<>();
        environment.put("PATH", System.getenv("PATH"));
        environment.put("ANDROID_HOME", "/home/shubham.joshi@brainvire.com/Downloads/platform-tools-latest-linux");
        environment.put("JAVA_HOME", "/usr/lib/jvm/java-11-openjdk-amd64/bin/java");

        return AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
                .usingDriverExecutable(new File("/home/shubham.joshi@brainvire.com/.nvm/versions/node/v21.7.3/bin/node"))
                .withAppiumJS(new File("/home/shubham.joshi@brainvire.com/.nvm/versions/node/v21.7.3/lib/node_modules/appium/build/lib/main.js"))
                .usingAnyFreePort()
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                .withEnvironment(environment)
                .withLogFile(new File(params.getPlatformName() + "_"
                        + params.getDeviceName() + File.separator + "Server.log")));
    }
}

package de.frinshhd.logiclobby.utils;

import app.simplecloud.controller.api.ControllerApi;

public class SimpleCloudWrapper {

    public static ControllerApi.Future getControllerApi() {
        return ControllerApi.createFutureApi();
    }

}

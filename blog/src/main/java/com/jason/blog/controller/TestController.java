package com.jason.blog.controller;

import com.jason.blog.common.util.JsonUtil;
import com.jason.blog.pojo.vo.ResponseModel;
import org.apache.log4j.Logger;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by jason on 2016/8/10.
 */
@Controller
@RequestMapping(value = "/test")
public class TestController {

    private static Logger logger = Logger.getLogger(TestController.class);

    @RequestMapping(value = "/toTest", method = RequestMethod.GET)
    public String toTest(){
        return "test";
    }

    @ResponseBody
    @RequestMapping(value = "/test",produces = "application/json;charset=UTF-8")
    public String test() {
        System.out.println(JsonUtil.objectToJsonStr(new ResponseModel(0, "你好")));
        return JsonUtil.objectToJsonStr(new ResponseModel(0, "你好"));
    }


    @RequestMapping("/mobile/device")
    public void home(SitePreference sitePreference, Device device) {
        if (device.isMobile()) {
            logger.info("Hello mobile user!");
        } else if (device.isTablet()) {
            logger.info("Hello tablet user!");
        } else {
            logger.info("Hello desktop user!");
        }
    }

    @RequestMapping("/mobile/site")
    public String home(SitePreference sitePreference) {
        if (sitePreference == SitePreference.NORMAL) {
            logger.info("Site preference is normal");
            return "home";
        } else if (sitePreference == SitePreference.MOBILE) {
            logger.info("Site preference is mobile");
            return "home-mobile";
        } else if (sitePreference == SitePreference.TABLET) {
            logger.info("Site preference is tablet");
            return "home-tablet";
        } else {
            logger.info("no site preference");
            return "home";
        }
    }





}

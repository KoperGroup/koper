package com.zhaimi.message.demo.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created on 16/8/17.
 */
public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        logger.info("Main args: " + args);
        if (null == args || args.length < 1) {
            logger.error("Please choose a Demo class");
            return;
        }
        Class clazz = Class.forName("com.zhaimi.message.demo.main." + args[0]);
        Method method = clazz.getMethod("main", String[].class);
        String[] actualArgs = Arrays.copyOfRange(args, 1, args.length);
        method.invoke(null, (Object) actualArgs);
    }
}
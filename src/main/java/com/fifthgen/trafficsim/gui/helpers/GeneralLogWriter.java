package com.fifthgen.trafficsim.gui.helpers;

import com.fifthgen.trafficsim.localization.Messages;

import java.text.SimpleDateFormat;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class GeneralLogWriter {

    private static Logger logger = Logger.getLogger("GeneralLog");
    private static String logPath = "";
    private static String logOldPath = "";
    private static FileHandler handler = null;
    private static String file_ = "";

    public static synchronized void log(String message, int mode) {
        try {
            logger.log(Level.FINEST, message);
        } catch (Exception new_e) {
            System.out.println(Messages.getString("ErrorLog.whileLogging") + message + ")! " + new_e.getLocalizedMessage());
            new_e.printStackTrace();
        }
    }

    public static synchronized void log(String message) {
        try {
            logger.log(Level.FINEST, message);
        } catch (Exception new_e) {
            System.out.println(Messages.getString("ErrorLog.whileLogging") + message + ")! " + new_e.getLocalizedMessage());
            new_e.printStackTrace();
        }
    }

    public static String getFile_() {
        return file_;
    }
}
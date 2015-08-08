package com.onemenu.server.timer;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.onemenu.server.util.GenerateIdUtil;

/**
 * 
 * <br>
 * 类描述: 定时器用来重置当天的提示限制条件 <br>
 * 功能详细描述:
 * 
 * @author linhang
 * @date [2012-12-17]
 */
public class ResetValueJob extends QuartzJobBean {
    private static double sTempLimitedMemory = 0;
    private static int sTempFailTime = 0;

    @Override
    protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
        // 每天凌晨重置为0
        sTempLimitedMemory = 0;
        sTempFailTime = 0;
        
        GenerateIdUtil.enterNextDay();
    }

    public static double getTempLimitedMemory() {
        return sTempLimitedMemory;
    }

    public static void setTempLimitedMemory(double sTempLimitedMemory) {
        ResetValueJob.sTempLimitedMemory = sTempLimitedMemory;
    }

    public static int getTempFailTime() {
        return sTempFailTime;
    }

    public static void setTempFailTime(int sTempFailTime) {
        ResetValueJob.sTempFailTime = sTempFailTime;
    }

}

package com.liren.lottery_system.common.filter;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import org.slf4j.event.Level;

/**
 * 只过滤Info级别的日志
 */
public class InfoLevelFilter extends Filter<ILoggingEvent> {
    @Override
    public FilterReply decide(ILoggingEvent event) {
        if(event.getLevel().toInt() == Level.INFO.toInt()) {
            return FilterReply.ACCEPT;
        }
        return FilterReply.DENY;
    }
}

package com.github.grishberg.performance

import com.github.grishberg.tests.common.RunnerLogger
import org.apache.logging.log4j.LogManager

class Log4JLogger : RunnerLogger {
    var log = LogManager.getLogger("runner")

    override fun i(tag: String, msg: String) {
        log.info("$tag: $msg")
    }

    override fun i(tag: String, format: String, vararg args: Any?) {
        log.info("$tag: $format", args)
    }

    override fun w(tag: String, msg: String) {
        log.warn("$tag: $msg")
    }

    override fun w(tag: String, format: String, vararg args: Any?) {
        log.warn("$tag: $format", args)
    }

    override fun e(tag: String, msg: String) {
        log.error("$tag: $msg")
    }

    override fun e(tag: String, msg: String, t: Throwable?) {
        log.error("$tag: $msg", t)
    }

    override fun d(tag: String, msg: String) {
        log.debug("$tag: $msg")
    }

    override fun d(tag: String, format: String, vararg args: Any?) {
        log.debug("$tag: $format", args)
    }
}
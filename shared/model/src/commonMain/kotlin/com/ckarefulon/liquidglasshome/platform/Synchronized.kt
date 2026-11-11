package com.ckarefulon.liquidglasshome.platform

/** 进程内互斥：Android / JVM 用 `synchronized`，Web 直接执行（单线程）。 */
expect fun <T> synchronizedBlock(lock: Any, block: () -> T): T

package com.zhangyingwei.spider.wiki;

import com.zhangyingwei.cockroach2.common.Task;
import com.zhangyingwei.cockroach2.core.CockroachContext;
import com.zhangyingwei.cockroach2.core.config.CockroachConfig;
import com.zhangyingwei.cockroach2.core.queue.QueueHandler;
import com.zhangyingwei.cockroach2.monitor.msg.consumer.CockroachMonitorConsumer;
import com.zhangyingwei.spider.wiki.store.WikiListStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpiderWikiApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(SpiderWikiApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        CockroachConfig config = new CockroachConfig()
                .appName("Wiki 爬虫")
                .threadSeep(1000)
                .threadSeepMin(300)
                .numThread(20)
                .addLogConsumer(CockroachMonitorConsumer.class)
                .store(WikiListStore.class);
        CockroachContext context = new CockroachContext(config);
        context.start(getQueue());
    }

    public static QueueHandler getQueue() {
        QueueHandler queueHandler = new QueueHandler.Builder(10000).withBlock(false).limit(100L)
                .withFilter(task -> task.getUrl() != null).build();
        queueHandler.add(new Task("https://dumps.wikimedia.org/zhwiki/latest/","ss"));
        return queueHandler;
    }
}

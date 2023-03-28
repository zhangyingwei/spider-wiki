package com.zhangyingwei.spider.wiki.store;

import com.zhangyingwei.cockroach2.common.Task;
import com.zhangyingwei.cockroach2.core.store.IStore;
import com.zhangyingwei.cockroach2.session.response.CockroachResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j
public class WikiListStore implements IStore {

    private WikiDownloadStore wikiDownloadStore = new WikiDownloadStore();
    @Override
    public void store(CockroachResponse response) {
        if (response.isGroup("ss")) {
            response.select("a").forEach(ele -> {
                String href = ele.attr("href");
                if (href != null && href.startsWith("zhwiki")) {
                    String url = response.getTask().getUrl().concat(href);
                    Task task = new Task(url, "download").tryNum(10);
                    task.setParams(
                            new HashMap<String, String>() {
                                {
                                    put("name", href);
                                }
                            }
                    );
//                    log.info("new task: " + task);
                    response.getQueue().add(task);
                }
            });
        } else if (response.isGroup("download")) {
            this.wikiDownloadStore.store(response);
        }
    }

    @Override
    public void faild(CockroachResponse response) {
        log.info("====================>  response: {}", response);
    }
}

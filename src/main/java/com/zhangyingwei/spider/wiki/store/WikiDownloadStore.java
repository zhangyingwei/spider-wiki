package com.zhangyingwei.spider.wiki.store;

import cn.hutool.core.io.FileUtil;
import com.zhangyingwei.cockroach2.core.store.IStore;
import com.zhangyingwei.cockroach2.session.response.CockroachResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Slf4j
public class WikiDownloadStore implements IStore {
    @Override
    public void store(CockroachResponse cockroachResponse) {
        try {
            InputStream input = cockroachResponse.getContent().getInputStream();
            Map<String, String> params = cockroachResponse.getTask().getParams();
            File file = new File("./wiki/" + params.get("name"));
            FileUtil.writeFromStream(input, file);
            log.info("download: " + params.get("name") + " size:" + FileUtil.size(file));
            input.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void faild(CockroachResponse cockroachResponse) {

    }
}

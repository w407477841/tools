package com.example;

import cn.hutool.core.collection.CollectionUtil;
import com.honghu.oauth2.client.Ignore;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IgnoreImpl implements Ignore {
    private static  final String[] IGNORE = {"/hah","/haha2"};
    @Override
    public List<String> ignore() throws Exception {
        return CollectionUtil.toList(IGNORE);
    }

}

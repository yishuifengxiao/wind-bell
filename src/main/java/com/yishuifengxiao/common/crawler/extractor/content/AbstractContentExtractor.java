package com.yishuifengxiao.common.crawler.extractor.content;



import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.yishuifengxiao.common.crawler.domain.model.ContentExtractRule;
import com.yishuifengxiao.common.crawler.domain.model.FieldExtractRule;

/**
 * 抽象内容提取器
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-14
 */
public abstract class AbstractContentExtractor implements ContentExtractor {

    protected ContentExtractRule contentRule;

    protected String name;


    /**
     * 获取所有的属性处理规则
     *
     * @return
     */
    protected List<FieldExtractRule> getFieldExtractRules() {
        return contentRule.getRules().stream().map(t -> {
            if (t.getSort() == null) {
                t.setSort(0);
            }
            return t;

        }).sorted(Comparator.comparing(FieldExtractRule::getSort)).collect(Collectors.toList());
    }

    public AbstractContentExtractor(ContentExtractRule contentRule) {
        this.name = contentRule.getFiledName();
        this.contentRule = contentRule;
    }


    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ContentExtractRule getContentRule() {
        return contentRule;
    }

    public void setContentRule(ContentExtractRule contentRule) {
        this.contentRule = contentRule;
    }
}

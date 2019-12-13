package com.yishuifengxiao.common.crawler.extractor.content;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.yishuifengxiao.common.crawler.domain.model.ContentItem;
import com.yishuifengxiao.common.crawler.domain.model.FieldExtractRule;

/**
 * 抽象内容提取器
 * 
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-14
 */
public abstract class AbstractContentExtractor implements ContentExtractor {

	protected ContentItem contentRule;

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

	@Override
	public Object extract(String input) {
		return extract(input, this.getFieldExtractRules());
	}

	/**
	 * 根据提取规则对输入数据进行提取
	 * 
	 * @param input             输入数据
	 * @param fieldExtractRules 已经按照从小到大排列好的提取规则
	 * @return 提取后的数据
	 */
	protected abstract Object extract(String input, List<FieldExtractRule> fieldExtractRules);

	public AbstractContentExtractor(ContentItem contentRule) {
		this.contentRule = contentRule;
	}

	@Override
	public String getName() {
		return this.contentRule.getFiledName();
	}

	public ContentItem getContentRule() {
		return contentRule;
	}

	public void setContentRule(ContentItem contentRule) {
		this.contentRule = contentRule;
	}
}

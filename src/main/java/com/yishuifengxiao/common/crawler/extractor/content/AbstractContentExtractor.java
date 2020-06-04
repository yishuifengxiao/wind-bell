package com.yishuifengxiao.common.crawler.extractor.content;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.yishuifengxiao.common.crawler.domain.entity.Page;
import com.yishuifengxiao.common.crawler.domain.model.ExtractFieldRule;
import com.yishuifengxiao.common.crawler.domain.model.ExtractRule;

/**
 * 抽象内容提取器
 * 
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-14
 */
public abstract class AbstractContentExtractor implements ContentExtractor {

	protected ExtractRule contentRule;

	/**
	 * 获取所有的属性处理规则，并对属性处理规则进行排序
	 *
	 * @return
	 */
	private List<ExtractFieldRule> getFieldExtractRules() {
		return contentRule.getRules().stream().map(t -> {
			if (t.getSort() == null) {
				t.setSort(0);
			}
			return t;

		}).sorted(Comparator.comparing(ExtractFieldRule::getSort)).collect(Collectors.toList());
	}

	/**
	 * 提取数据
	 * 
	 * @param page 下载后的网页对象
	 * @return 提取后的数据
	 */
	@Override
	public Object extract(Page page) {
		return extract(page, this.getFieldExtractRules());
	}

	/**
	 * 根据提取规则对输入数据进行提取
	 * 
	 * @param page              下载后的网页对象
	 * @param fieldExtractRules 已经按照从小到大排列好的提取规则
	 * @return 提取后的数据
	 */
	protected abstract Object extract(Page page, List<ExtractFieldRule> fieldExtractRules);

	public AbstractContentExtractor(ExtractRule contentRule) {
		this.contentRule = contentRule;
	}

	@Override
	public String getName() {
		return this.contentRule.getCode();
	}

}

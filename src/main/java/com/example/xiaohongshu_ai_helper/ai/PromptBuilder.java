package com.example.xiaohongshu_ai_helper.ai;

import org.springframework.stereotype.Component;

@Component
public class PromptBuilder {

    // 生成内容
    public String buildNotePrompt(String productName, String description, String category, double price,
                                  String targetUser, String scenario) {
        StringBuilder prompt = new StringBuilder();
        prompt.append(String.format("""
                请根据以下产品信息，生成一篇适合发布在小红书的完整笔记正文，不需要标题、不需要标签，不要解释说明：

                产品名称：%s
                产品描述：%s
                产品类别：%s
                产品价格：%.2f 元
                """, productName, description, category, price));

        if (targetUser != null && !targetUser.trim().isEmpty()) {
            prompt.append(String.format("\n            目标用户：%s", targetUser));
        }

        if (scenario != null && !scenario.trim().isEmpty()) {
            prompt.append(String.format("\n            使用场景：%s", scenario));
        }

        prompt.append("""

                要求：
                - 风格真实、亲切、有感染力
                - 语气自然，不要看起来像 AI 写的
                - 开头吸引人，中间突出卖点，结尾带一句推荐或行动引导
                - 如果有目标用户和使用场景信息，请结合这些信息生成更精准的内容
                """);

        return prompt.toString();
    }

    // 生成多个标题
    public String buildMultipleTitlesPrompt(String productName, String description, String category) {
        return String.format("""
                请为以下产品生成5个适合发布在小红书的笔记标题，每个标题一行，不要编号，不要添加任何解释说明：

                产品名称：%s
                产品描述：%s
                产品类别：%s

                要求：
                - 标题要吸引人，有小红书风格
                - 可以使用emoji表情
                - 突出产品特点和卖点
                - 每个标题风格略有不同
                """, productName, description, category);
    }

    // 生成标签（返回格式明确）
    public String buildTagsPrompt(String productName, String category) {
        return String.format("""
                请为以下产品生成8个适合小红书发布的热门标签，只输出标签本身，用中文逗号隔开，不要编号、不要解释、不要换行：

                产品名称：%s
                产品类别：%s
                """, productName, category);
    }

    // 生成产品分析
    public String buildAnalysisPrompt(String productName, String description, String category, double price,
                                      String targetUser, String scenario) {
        StringBuilder prompt = new StringBuilder();
        prompt.append(String.format("""
                请为以下产品生成一份简洁的产品卖点分析，用于小红书营销参考：

                产品名称：%s
                产品描述：%s
                产品类别：%s
                产品价格：%.2f 元
                """, productName, description, category, price));

        if (targetUser != null && !targetUser.trim().isEmpty()) {
            prompt.append(String.format("\n            目标用户：%s", targetUser));
        }

        if (scenario != null && !scenario.trim().isEmpty()) {
            prompt.append(String.format("\n            使用场景：%s", scenario));
        }

        prompt.append("""

                要求：
                - 分析核心卖点和优势
                - 目标用户群体分析
                - 适用场景分析
                - 价格竞争力
                - 营销建议
                - 语言简洁明了
                """);

        return prompt.toString();
    }
}

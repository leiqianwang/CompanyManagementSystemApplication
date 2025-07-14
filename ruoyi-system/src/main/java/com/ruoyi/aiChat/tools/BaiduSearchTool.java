package com.ruoyi.aiChat.tools;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import dev.langchain4j.agent.tool.Tool;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;

/**
 * 百度搜索工具
 * 为AI聊天提供网络搜索能力
 * 
 * @author ruoyi
 * @date 2025-07-03
 */
@Component
public class BaiduSearchTool {

    @Value("${ai.tools.baidu.enabled:false}")
    private boolean baiduSearchEnabled;

    private final WebClient webClient;

    public BaiduSearchTool() {
        this.webClient = WebClient.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024))
                .build();
    }

    /**
     * 使用百度搜索查询信息
     * 
     * @param query 搜索关键词
     * @return 搜索结果摘要
     */
    @Tool("通过百度搜索获取最新信息，当用户询问最新消息、实时信息或需要搜索时使用")
    public String searchBaidu(String query) {
        if (!baiduSearchEnabled) {
            return "搜索功能未启用。如需使用搜索功能，请在配置中启用 ai.tools.baidu.enabled=true";
        }

        try {
            // 对查询关键词进行URL编码
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
            
            // 构建百度搜索URL
            String searchUrl = "https://www.baidu.com/s?wd=" + encodedQuery + "&rn=10";
            
            // 发起HTTP请求获取搜索结果页面
            String htmlContent = webClient.get()
                    .uri(searchUrl)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if (htmlContent == null || htmlContent.isEmpty()) {
                return "搜索请求失败，无法获取搜索结果";
            }

            // 解析搜索结果 (简化版本)
            List<SearchResult> results = parseSearchResults(htmlContent);
            
            if (results.isEmpty()) {
                return "搜索关键词 \"" + query + "\" 没有找到相关结果";
            }

            // 格式化搜索结果
            StringBuilder summary = new StringBuilder();
            summary.append("关于 \"").append(query).append("\" 的搜索结果：\n\n");
            
            for (int i = 0; i < Math.min(results.size(), 5); i++) {
                SearchResult result = results.get(i);
                summary.append(i + 1).append(". ")
                       .append(result.getTitle()).append("\n")
                       .append("   ").append(result.getSnippet()).append("\n\n");
            }
            
            return summary.toString();

        } catch (Exception e) {
            return "搜索过程中发生错误: " + e.getMessage();
        }
    }

    /**
     * 解析百度搜索结果页面
     * 注意：这是一个简化的解析器，实际生产环境建议使用百度搜索API
     */
    private List<SearchResult> parseSearchResults(String htmlContent) {
        List<SearchResult> results = new ArrayList<>();
        
        try {
            // 简化的HTML解析 - 查找搜索结果
            // 百度搜索结果通常在class="result"的div中
            String[] parts = htmlContent.split("<div class=\"result");
            
            for (int i = 1; i < Math.min(parts.length, 6); i++) {
                String part = parts[i];
                
                // 提取标题
                String title = extractBetween(part, "<h3", "</h3>");
                title = cleanHtml(title);
                if (title.isEmpty()) {
                    title = "搜索结果 " + i;
                }
                
                // 提取摘要
                String snippet = extractBetween(part, "<span class=\"content-right_", "</span>");
                if (snippet.isEmpty()) {
                    snippet = extractBetween(part, "abstract\">", "</span>");
                }
                snippet = cleanHtml(snippet);
                if (snippet.isEmpty()) {
                    snippet = "暂无摘要信息";
                }
                
                // 限制摘要长度
                if (snippet.length() > 200) {
                    snippet = snippet.substring(0, 200) + "...";
                }
                
                results.add(new SearchResult(title, snippet));
            }
            
        } catch (Exception e) {
            // 如果解析失败，返回一个默认结果
            results.add(new SearchResult("搜索结果", "由于页面结构变化，无法解析详细结果，建议手动搜索"));
        }
        
        return results;
    }

    /**
     * 提取两个字符串之间的内容
     */
    private String extractBetween(String text, String start, String end) {
        try {
            int startIndex = text.indexOf(start);
            if (startIndex == -1) return "";
            
            startIndex = text.indexOf(">", startIndex) + 1;
            int endIndex = text.indexOf(end, startIndex);
            if (endIndex == -1) return "";
            
            return text.substring(startIndex, endIndex);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 清理HTML标签和特殊字符
     */
    private String cleanHtml(String html) {
        if (html == null) return "";
        
        return html.replaceAll("<[^>]+>", "")
                  .replaceAll("&nbsp;", " ")
                  .replaceAll("&lt;", "<")
                  .replaceAll("&gt;", ">")
                  .replaceAll("&amp;", "&")
                  .replaceAll("&quot;", "\"")
                  .replaceAll("\\s+", " ")
                  .trim();
    }

    /**
     * 搜索结果数据类
     */
    public static class SearchResult {
        private final String title;
        private final String snippet;

        public SearchResult(String title, String snippet) {
            this.title = title;
            this.snippet = snippet;
        }

        public String getTitle() {
            return title;
        }

        public String getSnippet() {
            return snippet;
        }
    }
}

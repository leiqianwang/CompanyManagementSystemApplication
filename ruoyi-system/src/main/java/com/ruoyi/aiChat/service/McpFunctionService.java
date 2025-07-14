package com.ruoyi.aiChat.service;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * MCP Function Service - 定义可供AI调用的函数
 *
 * @author ruoyi
 */
@Service
public class McpFunctionService {

    /**
     * 获取当前时间
     */
    @Tool("获取当前的日期和时间")
    public String getCurrentTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 计算器函数
     */
    @Tool("执行基本的数学计算，支持加减乘除运算")
    public String calculate(String expression) {
        try {
            // 简单的计算器实现
            String cleanExpression = expression.replaceAll("\\s+", "");

            // 处理基本运算
            if (cleanExpression.contains("+")) {
                String[] parts = cleanExpression.split("\\+");
                double result = Double.parseDouble(parts[0]) + Double.parseDouble(parts[1]);
                return String.valueOf(result);
            } else if (cleanExpression.contains("-")) {
                String[] parts = cleanExpression.split("-");
                if (parts.length == 2) {
                    double result = Double.parseDouble(parts[0]) - Double.parseDouble(parts[1]);
                    return String.valueOf(result);
                }
            } else if (cleanExpression.contains("*")) {
                String[] parts = cleanExpression.split("\\*");
                double result = Double.parseDouble(parts[0]) * Double.parseDouble(parts[1]);
                return String.valueOf(result);
            } else if (cleanExpression.contains("/")) {
                String[] parts = cleanExpression.split("/");
                if (Double.parseDouble(parts[1]) != 0) {
                    double result = Double.parseDouble(parts[0]) / Double.parseDouble(parts[1]);
                    return String.valueOf(result);
                } else {
                    return "错误：除数不能为零";
                }
            }

            return "无法识别的计算表达式";
        } catch (Exception e) {
            return "计算错误: " + e.getMessage();
        }
    }

    /**
     * 生成随机数
     */
    @Tool("生成指定范围内的随机数")
    public String generateRandomNumber(int min, int max) {
        if (min > max) {
            return "错误：最小值不能大于最大值";
        }

        Random random = new Random();
        int randomNumber = random.nextInt(max - min + 1) + min;
        return String.valueOf(randomNumber);
    }

    /**
     * 天气查询模拟函数
     */
    @Tool("查询指定城市的天气信息")
    public String getWeather(String city) {
        // 模拟天气数据
        Map<String, String> weatherData = new HashMap<>();
        weatherData.put("北京", "晴天，温度18-28°C，东南风2-3级");
        weatherData.put("上海", "多云，温度20-26°C，南风1-2级");
        weatherData.put("广州", "小雨，温度22-30°C，东风3-4级");
        weatherData.put("深圳", "阴天，温度24-31°C，南风2-3级");
        weatherData.put("杭州", "晴天，温度19-27°C，西南风1-2级");

        return weatherData.getOrDefault(city, "抱歉，暂时无法获取" + city + "的天气信息");
    }

    /**
     * 文本统计函数
     */
    @Tool("统计文本的字符数、单词数等信息")
    public String analyzeText(String text) {
        if (text == null || text.isEmpty()) {
            return "文本为空";
        }

        int charCount = text.length();
        int wordCount = text.trim().split("\\s+").length;
        int lineCount = text.split("\n").length;

        return String.format("文本统计结果：\n字符数：%d\n单词数：%d\n行数：%d",
                charCount, wordCount, lineCount);
    }

    /**
     * 翻译函数（模拟）
     */
    @Tool("将中文翻译成英文或将英文翻译成中文")
    public String translate(String text, String targetLanguage) {
        // 这里可以集成真实的翻译API
        Map<String, String> translations = new HashMap<>();
        translations.put("你好", "Hello");
        translations.put("谢谢", "Thank you");
        translations.put("再见", "Goodbye");
        translations.put("Hello", "你好");
        translations.put("Thank you", "谢谢");
        translations.put("Goodbye", "再见");

        String result = translations.get(text);
        if (result != null) {
            return result;
        }

        return "抱歉，无法翻译该文本。这是一个模拟翻译函数，支持的词汇有限。";
    }

    /**
     * 生成UUID
     */
    @Tool("生成一个唯一的UUID标识符")
    public String generateUUID() {
        return java.util.UUID.randomUUID().toString();
    }

    /**
     * 字符串格式化函数
     */
    @Tool("将字符串转换为指定格式（大写、小写、首字母大写）")
    public String formatString(String text, String format) {
        if (text == null || text.isEmpty()) {
            return "文本为空";
        }

        switch (format.toLowerCase()) {
            case "uppercase":
            case "大写":
                return text.toUpperCase();
            case "lowercase":
            case "小写":
                return text.toLowerCase();
            case "capitalize":
            case "首字母大写":
                return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
            default:
                return "不支持的格式类型，支持的格式：uppercase/大写, lowercase/小写, capitalize/首字母大写";
        }
    }
}

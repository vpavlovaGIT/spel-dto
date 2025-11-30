package ru.example.spel;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SpelContext {
    private final ExpressionParser parser = new SpelExpressionParser();

    public <T> T evaluateExpressionWithVariables(
            String expression,
            Object rootObject,
            Map<String, Object> variables,
            Class<T> type
    ) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setRootObject(rootObject);
        context.setVariable("root", rootObject);
        if (variables != null) {
            variables.forEach(context::setVariable);
        }
        Expression exp = parser.parseExpression(expression);
        return exp.getValue(context, type);
    }

    public <T> T evaluateTemplateExpressionWithVariables(
            String expression,
            Object rootObject,
            Map<String, Object> variables,
            Class<T> resultType
    ) {
        ParserContext pc = new TemplateParserContext("${", "}");
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setRootObject(rootObject);
        context.setVariable("root", rootObject);
        if (variables != null) {
            variables.forEach(context::setVariable);
        }
        Expression exp = parser.parseExpression(expression, pc);
        return exp.getValue(context, resultType);
    }

    public <T> T evaluateExpression(
            String expression,
            Object rootObject,
            Class<T> resultType
    ) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setRootObject(rootObject);
        context.setVariable("root", rootObject);
        Expression exp = parser.parseExpression(expression);
        return exp.getValue(context, resultType);
    }
}
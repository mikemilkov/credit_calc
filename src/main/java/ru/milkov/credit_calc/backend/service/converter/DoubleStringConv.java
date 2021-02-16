package ru.milkov.credit_calc.backend.service.converter;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public class DoubleStringConv implements Converter<String, Double> {

    @Override
    public Result<Double> convertToModel(String s, ValueContext valueContext) {
        try {
            return Result.ok(Double.valueOf(s));
        } catch (NumberFormatException e) {
            return Result.error("Enter a number");
        }
    }

    @Override
    public String convertToPresentation(Double aDouble, ValueContext valueContext) {
        return String.format("%.2f", aDouble);
    }
}

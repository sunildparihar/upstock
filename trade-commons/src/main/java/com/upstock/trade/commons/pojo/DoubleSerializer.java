package com.upstock.trade.commons.pojo;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;

public class DoubleSerializer extends JsonSerializer<Double> {

    @Override
    public void serialize(Double aDouble, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (getFractionalPart(aDouble) == 0l) {
            jsonGenerator.writeNumber(aDouble.intValue());
        } else {
            String doubleString = String.valueOf(aDouble);
            jsonGenerator.writeNumber(new BigDecimal(doubleString));
        }
    }

    private static long getFractionalPart(double doubleNumber) {
        String doubleAsString = String.valueOf(doubleNumber);
        String doubleString = new BigDecimal(doubleAsString).toPlainString();
        int indexOfDecimal = doubleAsString.indexOf(".");
        return Long.valueOf(doubleString.substring(indexOfDecimal+1));
    }
}

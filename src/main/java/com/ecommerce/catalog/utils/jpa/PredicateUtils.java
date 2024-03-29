package com.ecommerce.catalog.utils.jpa;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.List;

public class PredicateUtils {

    public static Predicate getPredicate(List<BooleanExpression> booleanExpressions) {
        if (booleanExpressions.size() == 0) {
            return null;
        }
        BooleanExpression booleanExpression = null;
        for (int i = 0; i < booleanExpressions.size(); i++) {
            if (i == 0) {
                booleanExpression = booleanExpressions.get(i);
            }
            booleanExpression = booleanExpression.and(booleanExpressions.get(i));
        }
        return booleanExpression;
    }

}

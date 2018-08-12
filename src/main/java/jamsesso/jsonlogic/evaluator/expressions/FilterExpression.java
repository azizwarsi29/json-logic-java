package jamsesso.jsonlogic.evaluator.expressions;

import jamsesso.jsonlogic.JsonLogic;
import jamsesso.jsonlogic.ast.JsonLogicArray;
import jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import jamsesso.jsonlogic.evaluator.JsonLogicEvaluator;
import jamsesso.jsonlogic.evaluator.JsonLogicExpression;
import jamsesso.jsonlogic.utils.IndexedStructure;

import java.util.ArrayList;
import java.util.List;

public class FilterExpression implements JsonLogicExpression {
  public static final FilterExpression INSTANCE = new FilterExpression();

  private FilterExpression() {
    // Use INSTANCE instead.
  }

  @Override
  public String key() {
    return "filter";
  }

  @Override
  public Object evaluate(JsonLogicEvaluator evaluator, JsonLogicArray arguments, Object data)
    throws JsonLogicEvaluationException {
    if (arguments.size() != 2) {
      throw new JsonLogicEvaluationException("filter expects exactly 2 arguments");
    }

    Object maybeArray = evaluator.evaluate(arguments.get(0), data);

    if (!IndexedStructure.isEligible(maybeArray)) {
      throw new JsonLogicEvaluationException("first argument to filter must be a valid array");
    }

    IndexedStructure list = new IndexedStructure(maybeArray);
    List<Object> result = new ArrayList<>();

    for (Object item : list) {
      if(JsonLogic.truthy(evaluator.evaluate(arguments.get(1), item))) {
        result.add(item);
      }
    }

    return result;
  }
}

package org.kevoree.kevscript.statement;

import org.kevoree.ContainerRoot;
import org.kevoree.Instance;
import org.kevoree.kevscript.Type;
import org.kevoree.kevscript.util.InstanceResolver;
import org.waxeye.ast.IAST;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leiko on 12/15/16.
 */
public class StartStmt {

    public static void interpret(IAST<Type> stmt, ContainerRoot model, Map<String, String> ctxVars) throws Exception {
        final List<Instance> instances = InstanceResolver.resolve(stmt.getChildren().get(0), model, ctxVars);
        for (final Instance i : instances) {
            i.setStarted(true);
        }
    }
}

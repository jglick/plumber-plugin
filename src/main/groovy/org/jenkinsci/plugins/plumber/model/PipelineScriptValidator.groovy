/*
 * The MIT License
 *
 * Copyright (c) 2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jenkinsci.plugins.plumber.model

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.jenkinsci.plugins.scriptsecurity.sandbox.whitelists.Whitelisted

/**
 * A special-case closure handler used exclusively to determine whether invalid steps are called within
 * the closure.
 */
@SuppressFBWarnings(value="SE_NO_SERIALVERSIONID")
public class PipelineScriptValidator implements Serializable, MethodMissingWrapper {
    @Whitelisted
    static List<String> INVALID_STEPS = ["stage", "node", "parallel"]

    @Whitelisted
    List<String> invalidStepsUsed = []

    @Whitelisted
    public PipelineScriptValidator() {

    }

    def methodMissing(String methodName, args) {
        if (methodName in INVALID_STEPS) {
            if (!invalidStepsUsed.contains(methodName)) {
                invalidStepsUsed.add(methodName)
            }
        }
    }
}

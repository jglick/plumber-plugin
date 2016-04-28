/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jenkinsci.plugins.plumber;

import hudson.model.Result;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.junit.Test;
import org.junit.runners.model.Statement;

public class BasicPlumberTest extends AbstractPlumberTest {
    @Test
    public void testSingleSimpleStep() throws Exception {
        prepRepoWithJenkinsfile("singleSimpleStep");

        story.addStep(new Statement() {
            @Override public void evaluate() throws Throwable {
                WorkflowRun b = getAndStartBuild();
                story.j.assertLogContains("hello",
                        story.j.assertBuildStatusSuccess(story.j.waitForCompletion(b)));
            }
        });
    }

    @Test
    public void testInvalidCode() throws Exception {
        prepRepoWithJenkinsfile("invalidCode");

        story.addStep(new Statement() {
            @Override public void evaluate() throws Throwable {
                WorkflowRun b = getAndStartBuild();
                story.j.assertBuildStatus(Result.FAILURE, story.j.waitForCompletion(b));
            }
        });
    }

    @Test
    public void testArbitraryCodeInAction() throws Exception {
        prepRepoWithJenkinsfile("arbitraryCodeInAction");

        story.addStep(new Statement() {
            @Override public void evaluate() throws Throwable {
                WorkflowRun b = getAndStartBuild();
                story.j.assertLogContains("echoing name == simpleEcho",
                        story.j.assertBuildStatusSuccess(story.j.waitForCompletion(b)));
                story.j.assertLogContains("echoing node == [echo:nested]", b);
            }
        });
    }

    @Test
    public void testTwoLinearSteps() throws Exception {
        prepRepoWithJenkinsfile("twoLinearSteps");

        story.addStep(new Statement() {
            @Override public void evaluate() throws Throwable {
                WorkflowRun b = getAndStartBuild();
                story.j.assertLogContains("hello",
                        story.j.assertBuildStatusSuccess(story.j.waitForCompletion(b)));
                story.j.assertLogNotContains("Multiple phase", b);
                story.j.assertLogContains("goodbye", b);


            }
        });
    }

    @Test
    public void testTwoParallelSteps() throws Exception {
        prepRepoWithJenkinsfile("twoParallelSteps");

        story.addStep(new Statement() {
            @Override public void evaluate() throws Throwable {
                WorkflowRun b = getAndStartBuild();
                story.j.assertLogContains("onePhase",
                        story.j.assertBuildStatusSuccess(story.j.waitForCompletion(b)));
                story.j.assertLogContains("Multiple phase", b);
                story.j.assertLogContains("twoPhase", b);
            }
        });
    }

    @Test
    public void testCodeGen() throws Exception {
        prepRepoWithJenkinsfile("codeGen");

        story.addStep(new Statement() {
            @Override public void evaluate() throws Throwable {
                WorkflowRun b = getAndStartBuild();
                story.j.assertLogContains("onePhase",
                        story.j.assertBuildStatusSuccess(story.j.waitForCompletion(b)));
                story.j.assertLogContains("Execute sub-workflows in parallel", b);
                story.j.assertLogContains("twoPhase", b);
            }
        });
    }

    @Test
    public void testArgsAsClosureFromNonCPS() throws Exception {
        prepRepoWithJenkinsfile("argsAsClosureFromNonCPS");

        story.addStep(new Statement() {
            @Override public void evaluate() throws Throwable {
                WorkflowRun b = getAndStartBuild();
                story.j.assertLogContains("onePhase",
                        story.j.assertBuildStatusSuccess(story.j.waitForCompletion(b)));
                story.j.assertLogContains("Multiple phase", b);
                story.j.assertLogContains("twoPhase", b);
            }
        });
    }

    @Test
    public void testClean() throws Exception {
        prepRepoWithJenkinsfile("clean");

        story.addStep(new Statement() {
            @Override public void evaluate() throws Throwable {
                WorkflowRun b = getAndStartBuild();
                story.j.assertBuildStatusSuccess(story.j.waitForCompletion(b));
            }
        });
    }

    @Test
    public void testArgsAsCpsClosure() throws Exception {
        prepRepoWithJenkinsfile("argsAsCpsClosure");

        story.addStep(new Statement() {
            @Override public void evaluate() throws Throwable {
                WorkflowRun b = getAndStartBuild();
                story.j.assertLogContains("onePhase",
                        story.j.assertBuildStatusSuccess(story.j.waitForCompletion(b)));
                story.j.assertLogContains("Multiple phase", b);
                story.j.assertLogContains("twoPhase", b);
            }
        });

    }

    @Test
    public void testNoActionNoPipeline() throws Exception {
        prepRepoWithJenkinsfile("noActionNoPipeline");

        story.addStep(new Statement() {
            @Override public void evaluate() throws Throwable {
                WorkflowRun b = getAndStartBuild();
                story.j.assertLogContains("No action or Pipeline code specified",
                        story.j.assertBuildStatus(Result.FAILURE, story.j.waitForCompletion(b)));
            }
        });
    }

    @Test
    public void testSimpleInlinePipeline() throws Exception {
        prepRepoWithJenkinsfile("simpleInlinePipeline");

        story.addStep(new Statement() {
            @Override public void evaluate() throws Throwable {
                WorkflowRun b = getAndStartBuild();
                story.j.assertLogContains("FOO is bar",
                        story.j.assertBuildStatusSuccess(story.j.waitForCompletion(b)));
            }
        });
    }

    @Test
    public void testInlinePipelineInvalidSteps() throws Exception {
        prepRepoWithJenkinsfile("inlinePipelineInvalidSteps");

        story.addStep(new Statement() {
            @Override public void evaluate() throws Throwable {
                WorkflowRun b = getAndStartBuild();
                story.j.assertLogContains("Illegal Pipeline steps used in inline Pipeline - stage, parallel, node",
                        story.j.assertBuildStatus(Result.FAILURE, story.j.waitForCompletion(b)));
            }
        });
    }

}

/*
 * Copyright 2008 Davy Verstappen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wintersleep.repository;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.IllegalTransactionStateException;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.hibernate.FlushMode;

public class TxTest extends AbstractDependencyInjectionSpringContextTests {

    private PlatformTransactionManager transactionManager;
    private PersonService personService;

    protected String getConfigPath() {
        return "/testApplicationContext.xml";
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    public void test() {

        for (int outerPropagation = TransactionDefinition.PROPAGATION_REQUIRED; outerPropagation <= TransactionDefinition.PROPAGATION_NESTED; outerPropagation++)
        {
            for (int innerPropagation = TransactionDefinition.PROPAGATION_REQUIRED; innerPropagation <= TransactionDefinition.PROPAGATION_NESTED; innerPropagation++)
            {
                TransactionTemplate outerTemplate = new TransactionTemplate(transactionManager);
                outerTemplate.setPropagationBehavior(outerPropagation);
                final TransactionTemplate innerTemplate = new TransactionTemplate(transactionManager);
                innerTemplate.setPropagationBehavior(innerPropagation);

                try {
                    outerTemplate.execute(new TransactionCallbackWithoutResult() {
                        protected void doInTransactionWithoutResult(TransactionStatus status) {
                            innerTemplate.execute(new TransactionCallbackWithoutResult() {
                                protected void doInTransactionWithoutResult(TransactionStatus status) {
                                    throw new IllegalArgumentException("hello");
                                }
                            });
                        }
                    });
                } catch (IllegalArgumentException e) {
                    assertEquals("hello", e.getMessage());
                } catch (IllegalTransactionStateException e) {
                    System.out.println(outerPropagation + ":" + innerPropagation);
                    e.printStackTrace(System.out);
                } catch (Throwable e) {
                    //System.out.println(outerPropagation + ":" + innerPropagation);
                    e.printStackTrace(System.out);
                    fail(outerPropagation + ":" + innerPropagation);
                }


            }

        }

    }

    public void testContextualSession() {
        FlushMode flushMode = personService.testContextualSession();
        assertEquals(FlushMode.NEVER, flushMode);
    }

}

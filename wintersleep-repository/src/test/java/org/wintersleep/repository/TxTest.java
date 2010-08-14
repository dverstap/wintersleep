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

import junit.framework.AssertionFailedError;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.springframework.core.Constants;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springframework.transaction.IllegalTransactionStateException;
import org.springframework.transaction.NestedTransactionNotSupportedException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.SQLException;

public class TxTest extends AbstractDependencyInjectionSpringContextTests {

    public static final String PREFIX_PROPAGATION = "PROPAGATION_";

    private TestDataSource dataSource;
    private PlatformTransactionManager transactionManager;
    private PersonRepository personRepository;
    private PersonService personService;


    final Constants constants = new Constants(TransactionDefinition.class);

    protected String getConfigPath() {
        return "/tx-testApplicationContext.xml";
    }

    public void setDataSource(TestDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void setPersonRepository(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

//    public void justChecking() {
//        UserTransaction userTransaction = new UserTransactionImple();
//        TransactionManager transactionManager = new TransactionManagerImple();
//    }

    public void testNoNesting() throws SQLException {

        for (int propagation = TransactionDefinition.PROPAGATION_REQUIRED; propagation <= TransactionDefinition.PROPAGATION_NESTED; propagation++) {
            dataSource.deleteAllData();
            assertEquals(0, dataSource.countAllRows(Person.class.getSimpleName()));

            TransactionTemplate template = new TransactionTemplate(transactionManager);
            template.setPropagationBehavior(propagation);
            template.setReadOnly(true);
            TxTestService service = new TxTestServiceImpl(template, personRepository, "myName");
            try {
                service.execute();
                assertEquals(1, dataSource.countAllRows(Person.class.getSimpleName()));
            } catch (AssertionFailedError e) {
                throw e;
            } catch (Throwable e) {
                String propagationName = constants.toCode(propagation, PREFIX_PROPAGATION);
                //assertEquals(propagationName, TransactionDefinition.PROPAGATION_MANDATORY, propagation);
                // notice: even under PROPAGATION_NEVER, it is still written!
                System.out.println(propagationName + ":" + e.getMessage());
                //e.printStackTrace(System.err);
            }
        }

    }

    public void testNestedTransactionalServicesWithException() {

        checkNestedWithException(TransactionDefinition.PROPAGATION_REQUIRED, TransactionDefinition.PROPAGATION_REQUIRED, IllegalArgumentException.class);
        checkNestedWithException(TransactionDefinition.PROPAGATION_REQUIRED, TransactionDefinition.PROPAGATION_SUPPORTS, IllegalArgumentException.class);
        checkNestedWithException(TransactionDefinition.PROPAGATION_REQUIRED, TransactionDefinition.PROPAGATION_MANDATORY, IllegalArgumentException.class);
        checkNestedWithException(TransactionDefinition.PROPAGATION_REQUIRED, TransactionDefinition.PROPAGATION_REQUIRES_NEW, IllegalArgumentException.class);
        checkNestedWithException(TransactionDefinition.PROPAGATION_REQUIRED, TransactionDefinition.PROPAGATION_NOT_SUPPORTED, IllegalArgumentException.class);
        checkNestedWithException(TransactionDefinition.PROPAGATION_REQUIRED, TransactionDefinition.PROPAGATION_NEVER, IllegalTransactionStateException.class);
        checkNestedWithException(TransactionDefinition.PROPAGATION_REQUIRED, TransactionDefinition.PROPAGATION_NESTED, NestedTransactionNotSupportedException.class);

        checkNestedWithException(TransactionDefinition.PROPAGATION_SUPPORTS, TransactionDefinition.PROPAGATION_REQUIRED, IllegalArgumentException.class);
        checkNestedWithException(TransactionDefinition.PROPAGATION_SUPPORTS, TransactionDefinition.PROPAGATION_SUPPORTS, IllegalArgumentException.class);
        checkNestedWithException(TransactionDefinition.PROPAGATION_SUPPORTS, TransactionDefinition.PROPAGATION_MANDATORY, IllegalTransactionStateException.class);
        checkNestedWithException(TransactionDefinition.PROPAGATION_SUPPORTS, TransactionDefinition.PROPAGATION_REQUIRES_NEW, IllegalArgumentException.class);
        checkNestedWithException(TransactionDefinition.PROPAGATION_SUPPORTS, TransactionDefinition.PROPAGATION_NOT_SUPPORTED, IllegalArgumentException.class);
        checkNestedWithException(TransactionDefinition.PROPAGATION_SUPPORTS, TransactionDefinition.PROPAGATION_NEVER, IllegalArgumentException.class);
        checkNestedWithException(TransactionDefinition.PROPAGATION_SUPPORTS, TransactionDefinition.PROPAGATION_NESTED, IllegalArgumentException.class);

        checkNestedWithException(TransactionDefinition.PROPAGATION_MANDATORY, TransactionDefinition.PROPAGATION_REQUIRED, IllegalTransactionStateException.class);
        checkNestedWithException(TransactionDefinition.PROPAGATION_MANDATORY, TransactionDefinition.PROPAGATION_SUPPORTS, IllegalTransactionStateException.class);
        checkNestedWithException(TransactionDefinition.PROPAGATION_MANDATORY, TransactionDefinition.PROPAGATION_MANDATORY, IllegalTransactionStateException.class);
        checkNestedWithException(TransactionDefinition.PROPAGATION_MANDATORY, TransactionDefinition.PROPAGATION_REQUIRES_NEW, IllegalTransactionStateException.class);
        checkNestedWithException(TransactionDefinition.PROPAGATION_MANDATORY, TransactionDefinition.PROPAGATION_NOT_SUPPORTED, IllegalTransactionStateException.class);
        checkNestedWithException(TransactionDefinition.PROPAGATION_MANDATORY, TransactionDefinition.PROPAGATION_NEVER, IllegalTransactionStateException.class);
        checkNestedWithException(TransactionDefinition.PROPAGATION_MANDATORY, TransactionDefinition.PROPAGATION_NESTED, IllegalTransactionStateException.class);

        checkNestedWithException(TransactionDefinition.PROPAGATION_REQUIRES_NEW, TransactionDefinition.PROPAGATION_REQUIRED, IllegalArgumentException.class);
        checkNestedWithException(TransactionDefinition.PROPAGATION_REQUIRES_NEW, TransactionDefinition.PROPAGATION_SUPPORTS, IllegalArgumentException.class);
        checkNestedWithException(TransactionDefinition.PROPAGATION_REQUIRES_NEW, TransactionDefinition.PROPAGATION_MANDATORY, IllegalArgumentException.class);
        checkNestedWithException(TransactionDefinition.PROPAGATION_REQUIRES_NEW, TransactionDefinition.PROPAGATION_REQUIRES_NEW, IllegalArgumentException.class);
        checkNestedWithException(TransactionDefinition.PROPAGATION_REQUIRES_NEW, TransactionDefinition.PROPAGATION_NOT_SUPPORTED, IllegalArgumentException.class);
        checkNestedWithException(TransactionDefinition.PROPAGATION_REQUIRES_NEW, TransactionDefinition.PROPAGATION_NEVER, IllegalTransactionStateException.class);
        checkNestedWithException(TransactionDefinition.PROPAGATION_REQUIRES_NEW, TransactionDefinition.PROPAGATION_NESTED, NestedTransactionNotSupportedException.class);

        checkNestedWithException(TransactionDefinition.PROPAGATION_NOT_SUPPORTED, TransactionDefinition.PROPAGATION_REQUIRED, IllegalArgumentException.class);
        checkNestedWithException(TransactionDefinition.PROPAGATION_NOT_SUPPORTED, TransactionDefinition.PROPAGATION_SUPPORTS, IllegalArgumentException.class);
        checkNestedWithException(TransactionDefinition.PROPAGATION_NOT_SUPPORTED, TransactionDefinition.PROPAGATION_MANDATORY, IllegalTransactionStateException.class);
        checkNestedWithException(TransactionDefinition.PROPAGATION_NOT_SUPPORTED, TransactionDefinition.PROPAGATION_REQUIRES_NEW, IllegalArgumentException.class);
        checkNestedWithException(TransactionDefinition.PROPAGATION_NOT_SUPPORTED, TransactionDefinition.PROPAGATION_NOT_SUPPORTED, IllegalArgumentException.class);
        checkNestedWithException(TransactionDefinition.PROPAGATION_NOT_SUPPORTED, TransactionDefinition.PROPAGATION_NEVER, IllegalArgumentException.class);
        checkNestedWithException(TransactionDefinition.PROPAGATION_NOT_SUPPORTED, TransactionDefinition.PROPAGATION_NESTED, IllegalArgumentException.class);

        checkNestedWithException(TransactionDefinition.PROPAGATION_NEVER, TransactionDefinition.PROPAGATION_REQUIRED, IllegalArgumentException.class);
        checkNestedWithException(TransactionDefinition.PROPAGATION_NEVER, TransactionDefinition.PROPAGATION_SUPPORTS, IllegalArgumentException.class);
        checkNestedWithException(TransactionDefinition.PROPAGATION_NEVER, TransactionDefinition.PROPAGATION_MANDATORY, IllegalTransactionStateException.class);
        checkNestedWithException(TransactionDefinition.PROPAGATION_NEVER, TransactionDefinition.PROPAGATION_REQUIRES_NEW, IllegalArgumentException.class);
        checkNestedWithException(TransactionDefinition.PROPAGATION_NEVER, TransactionDefinition.PROPAGATION_NOT_SUPPORTED, IllegalArgumentException.class);
        checkNestedWithException(TransactionDefinition.PROPAGATION_NEVER, TransactionDefinition.PROPAGATION_NEVER, IllegalArgumentException.class);
        checkNestedWithException(TransactionDefinition.PROPAGATION_NEVER, TransactionDefinition.PROPAGATION_NESTED, IllegalArgumentException.class);

        checkNestedWithException(TransactionDefinition.PROPAGATION_NESTED, TransactionDefinition.PROPAGATION_REQUIRED, IllegalArgumentException.class);
        checkNestedWithException(TransactionDefinition.PROPAGATION_NESTED, TransactionDefinition.PROPAGATION_SUPPORTS, IllegalArgumentException.class);
        checkNestedWithException(TransactionDefinition.PROPAGATION_NESTED, TransactionDefinition.PROPAGATION_MANDATORY, IllegalArgumentException.class);
        checkNestedWithException(TransactionDefinition.PROPAGATION_NESTED, TransactionDefinition.PROPAGATION_REQUIRES_NEW, IllegalArgumentException.class);
        checkNestedWithException(TransactionDefinition.PROPAGATION_NESTED, TransactionDefinition.PROPAGATION_NOT_SUPPORTED, IllegalArgumentException.class);
        checkNestedWithException(TransactionDefinition.PROPAGATION_NESTED, TransactionDefinition.PROPAGATION_NEVER, IllegalTransactionStateException.class);
        checkNestedWithException(TransactionDefinition.PROPAGATION_NESTED, TransactionDefinition.PROPAGATION_NESTED, NestedTransactionNotSupportedException.class);

//        for (int outerPropagation = TransactionDefinition.PROPAGATION_REQUIRED; outerPropagation <= TransactionDefinition.PROPAGATION_NESTED; outerPropagation++) {
//            for (int innerPropagation = TransactionDefinition.PROPAGATION_REQUIRED; innerPropagation <= TransactionDefinition.PROPAGATION_NESTED; innerPropagation++) {
//
//
//            }
//
//        }
    }

    private void checkNestedWithException(int outerPropagation, int innerPropagation, Class expectedExceptionClass) {
        String propagationDesc = constants.toCode(outerPropagation, PREFIX_PROPAGATION)
                + ":" + constants.toCode(outerPropagation, PREFIX_PROPAGATION);
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
        } catch (Throwable e) {
            if (e.getClass() == expectedExceptionClass) {
                if (e.getClass().equals(IllegalArgumentException.class)) {
                    assertEquals(propagationDesc, "hello", e.getMessage());
                }
            } else {
                e.printStackTrace(System.out);
                fail(propagationDesc);
            }
        }
    }

    public void testContextualSession() {
        try {
            FlushMode flushMode = personService.testContextualSession();
            //assertEquals(FlushMode.NEVER, flushMode);
            fail();
        } catch (HibernateException e) {
            assertEquals("Unable to locate current JTA transaction", e.getMessage());
        }
    }

}

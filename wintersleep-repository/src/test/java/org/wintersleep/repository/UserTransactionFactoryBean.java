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

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.DisposableBean;

import javax.transaction.UserTransaction;

import com.arjuna.ats.internal.jta.transaction.arjunacore.UserTransactionImple;

public class UserTransactionFactoryBean implements FactoryBean, InitializingBean, DisposableBean {

    private static UserTransaction userTransaction;


    public static UserTransaction getInstance() {
        if (userTransaction == null) {
            throw new IllegalStateException("expected userTransaction to be non-null");
        }
        return userTransaction;
    }

    public Object getObject() throws Exception {
        return getInstance();
    }

    public Class getObjectType() {
        return UserTransaction.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void afterPropertiesSet() throws Exception {
        if (userTransaction != null) {
            throw new IllegalStateException("expected userTransaction to be null");
        }
        userTransaction = new UserTransactionImple();
    }

    public void destroy() throws Exception {
        if (userTransaction == null) {
            throw new IllegalStateException("expected userTransaction to be non-null");
        }
        userTransaction = null;
    }

}
